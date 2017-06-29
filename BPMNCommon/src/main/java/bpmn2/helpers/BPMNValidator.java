package bpmn2.helpers;

import bpmn2.TDataObject;
import bpmn2.TDefinitions;
import bpmn2.TItemDefinition;
import bpmn2.TProcess;
import bpmn2.TProperty;
import exception.BPMNError;

public class BPMNValidator {

	public static void validateDataObjectType(TDefinitions definitions, TDataObject dataObject, String type) throws BPMNError {
		String dataObjectItemDefinitionId = dataObject.getItemSubjectRef().getLocalPart();
    	TItemDefinition itemDefinition = BPMNHelper.getItemDefinition(definitions, dataObjectItemDefinitionId);
    	
    	type = type.toLowerCase().trim();
    	String acceptedTypes[] = null;
    	switch (itemDefinition.getStructureRef().getLocalPart()) {
    	case "Boolean":
    		acceptedTypes = new String[]{"bool", "boolean"};
    		break;
    		
    	case "Integer":
    	case "Long":
    		acceptedTypes = new String[]{"byte", "short", "int", "integer", "long"};
    		break;
    		
    	case "Double":
    		acceptedTypes = new String[]{"float", "double", "decimal"};
    		break;
    		
    	case "String":
    		acceptedTypes = new String[]{"string"};
    		break;
    	}
    	
		for (String acceptedType : acceptedTypes) if (type.equals(acceptedType)) return;
    	
		throw new BPMNError("Data object (" + dataObject.getName() + 
				") should be of type " + type);
	}
	
	public static void validateObjectType(TDefinitions definitions, TProcess process, Object object, String type) throws BPMNError {
		type = type.toLowerCase().trim();
    	String acceptedTypes[] = null;
		if (object instanceof Number) {
			if (object instanceof Float || object instanceof Double)
				acceptedTypes = new String[]{"float", "double"};
			else
				acceptedTypes = new String[]{"byte", "short", "int", "integer", "long"};
		}
		else if (object instanceof Boolean)
			acceptedTypes = new String[]{"bool", "boolean"};
		else if (BPMNHelper.hasProperty(process, object.toString())) {
			validatePropertyType(definitions, process, BPMNHelper.getProperty(process, object.toString()), type);
			return;
		}
		else
			acceptedTypes = new String[]{"string"};
		
		for (String acceptedType : acceptedTypes) if (type.equals(acceptedType)) return;
    	
		throw new BPMNError("Object (" + object + ") should be of type " + type);
	}
	
	public static void validatePropertyType(TDefinitions definitions, TProcess process, TProperty property, String type) throws BPMNError {
		String propertyItemDefinitionId = property.getItemSubjectRef().getLocalPart();
    	TItemDefinition itemDefinition = BPMNHelper.getItemDefinition(definitions, propertyItemDefinitionId);
    	
    	type = type.toLowerCase().trim();
    	String acceptedTypes[] = null;
    	switch (itemDefinition.getStructureRef().getLocalPart()) {
    	case "Boolean":
    		acceptedTypes = new String[]{"bool", "boolean"};
    		break;
    		
    	case "Integer":
    	case "Long":
    		acceptedTypes = new String[]{"byte", "short", "int", "integer", "long"};
    		break;
    		
    	case "Double":
    		acceptedTypes = new String[]{"float", "double", "decimal"};
    		break;
    		
    	case "String":
    		acceptedTypes = new String[]{"string"};
    		break;
    	}
    	
		for (String acceptedType : acceptedTypes) if (type.equals(acceptedType)) return;
    	
		throw new BPMNError("Property (" + property.getName() + ") of process (" + process.getName() + 
				") should be of type " + type);
	}
	
}
