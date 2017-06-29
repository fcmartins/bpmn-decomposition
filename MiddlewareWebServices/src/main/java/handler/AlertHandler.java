package handler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jbpm.JBPMHelper;
import model.Subscription;
import model.Variable;

import common.webservice.model.VariableData;

import db.VariablesDb;

public class AlertHandler {

    private static Variable getVariableForSubscription(Subscription subscription) {
        VariablesDb db = new VariablesDb();

        if (subscription.getSensorDataNthEntry() > 0) {
            // obtain the Nth recent variable (or < N if there are less than N entries in db)
            System.out.println("Obtain the " + subscription.getSensorDataNthEntry() + "th entry");
            return db.getNthRecentVariable(subscription.getWsnId(), subscription.getVariable(),
                    subscription.getSensorDataNthEntry());
        }
        else {
            // obtain the variable more recent than N minutes ago (including N minutes ago)
            System.out.println("Obtain the entry not older than " + subscription.getSensorDataValidityDateWindow() + 
            		" minutes");
            return db.getVariableMoreRecentThan(subscription.getWsnId(), subscription.getVariable(),
                    subscription.getSensorDataValidityDateWindow());
        }
    }

    private static List<Subscription> getAllSubscriptionsSatisfiedByVariable(String wsnId, String variableName, Date date) {
        VariablesDb db = new VariablesDb();
        return db.getAllSubscriptionsSatisfiedByVariable(wsnId, variableName, date);
    }

    private static boolean sendVariableToSubscribers(List<Subscription> subscriptions, Object content) {
        boolean result = true;
        for (Subscription subscription : subscriptions) {
            System.out.println("Sending (" + content + ") to " + subscription);
            result &= sendVariableToSubscriber(subscription, content);
        }

        return result;
    }

    private static boolean sendVariableToSubscriber(Subscription subscription, Object content) {
        try {
            JBPMHelper helper = new JBPMHelper(new URL(subscription.getJbpmServerAddress()), subscription.getDeploymentId());

            Map<String, Object> variablesMap = new HashMap<>();
            variablesMap.put(subscription.getVariable(), content);
            if (helper.setVariables(subscription.getProcessInstanceId(), variablesMap)) {
                // sent variable successfully to subscriber, so can remove subscriber now
                if (SubscriptionHandler.unsubscribe(subscription))
                	System.out.println("Successfully requested middleware subsriber web service to unsubscribe subscriber");
                else
                	System.out.println("Failed to request middleware subsriber web service to unsubscribe subscriber");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean processNewSubscriber(Subscription subscription) {
        Variable variable = getVariableForSubscription(subscription);

        if (variable != null) {
            // there is already a variable available

            // send variable to the subscriber
            sendVariableToSubscriber(subscription, variable.getContent());
        }

        return true;
    }

    public static boolean processNewVariable(VariableData data) {
        Variable variable = data.getVariable();
        List<Subscription> subscriptions = getAllSubscriptionsSatisfiedByVariable(variable.getWsnId(),
                variable.getVariableName(), variable.getDate());

        return sendVariableToSubscribers(subscriptions, variable.getContent());
    }

}
