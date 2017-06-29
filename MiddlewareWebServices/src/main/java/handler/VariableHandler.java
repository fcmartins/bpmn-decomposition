package handler;

import model.Variable;
import common.webservice.model.VariableData;
import db.VariablesDb;

public class VariableHandler {

    public static boolean addNewVariableValue(Variable variable) {
    	System.out.println("Add variable: " + variable);
    	
        VariablesDb db = new VariablesDb();
        if (!db.isValid() || !db.addNewVariableValue(variable)) return false;

        // notify jbpm web service about the variable
        VariableData data = new VariableData(variable);
        if (AlertHandler.processNewVariable(data))
        	System.out.println("Successfully notified middleware jBPM web service about the new variable");
        else
        	System.out.println("Failed to notify middleware jBPM web service about the new variable");

        return true;
    }

}
