package handler;

import java.util.List;

import model.Subscription;
import db.VariablesDb;

public class SubscriptionHandler {

	public static boolean subscribe(List<Subscription> subscriptions) {
		boolean result = true;
		for (Subscription subscription : subscriptions) {
			result &= subscribe(subscription);
		}

		return result;
	}

	public static boolean subscribe(final Subscription subscription) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				VariablesDb db = new VariablesDb();
				if (!db.isValid() || !db.subscribe(subscription)) {
					System.out.println("Failed to store subscription in DB");
					return;
				}

				// notify jbpm web service about the subscription
				if (AlertHandler.processNewSubscriber(subscription))
					System.out
							.println("Successfully notified middleware jBPM web service about the new subscriber");
				else
					System.out
							.println("Failed to notify middleware jBPM web service about the new subscriber");
			}

		}).start();

		return true;
	}

	public static boolean unsubscribe(Subscription subscription) {
		VariablesDb db = new VariablesDb();
		return db.isValid() && db.unsubscribe(subscription);
	}

}
