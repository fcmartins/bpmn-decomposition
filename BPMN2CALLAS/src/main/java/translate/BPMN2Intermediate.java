package translate;


import intermediate.AbSyn;
import intermediate.Blink;
import intermediate.Command;
import intermediate.GetAccel;
import intermediate.GetAccelX;
import intermediate.GetAccelY;
import intermediate.GetAccelZ;
import intermediate.GetBatteryLevel;
import intermediate.GetInclX;
import intermediate.GetInclY;
import intermediate.GetInclZ;
import intermediate.GetLuminosity;
import intermediate.GetSensorID;
import intermediate.GetTemp;
import intermediate.GetTime;
import intermediate.GuardedCommand;
import intermediate.If;
import intermediate.Join;
import intermediate.LogDouble;
import intermediate.LogLong;
import intermediate.LogString;
import intermediate.MacAddr;
import intermediate.Process;
import intermediate.Receive;
import intermediate.ReturnCommand;
import intermediate.Send;
import intermediate.SetLEDCollor;
import intermediate.SetLEDOn;
import intermediate.TimedProcess;
import intermediate.Type;
import intermediate.TypeBool;
import intermediate.TypeDouble;
import intermediate.TypeLong;
import intermediate.TypeString;
import intermediate.Value;
import intermediate.VarDecl;
import intermediate.Variable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import main.FunctionIds;
import problems.BPMNError;
import problems.GatewayNotSupportedError;
import problems.IfBranchEmptyNameError;
import problems.InvalidFunctionNameError;
import problems.MissingPropertyError;
import bpmn2.TArtifact;
import bpmn2.TAssociation;
import bpmn2.TCatchEvent;
import bpmn2.TDataObject;
import bpmn2.TDataOutputAssociation;
import bpmn2.TEndEvent;
import bpmn2.TEventDefinition;
import bpmn2.TFlowElement;
import bpmn2.TGateway;
import bpmn2.TGatewayDirection;
import bpmn2.TProcess;
import bpmn2.TProperty;
import bpmn2.TReceiveTask;
import bpmn2.TSendTask;
import bpmn2.TSequenceFlow;
import bpmn2.TStartEvent;
import bpmn2.TTask;
import bpmn2.TTimerEventDefinition;
import bpmn2.helpers.BPMNGenerator;
import bpmn2.helpers.BPMNHelper;
import bpmn2.helpers.wsn.WsnBPMNHelper;


public class BPMN2Intermediate {
	
	private TProcess tp;
	private Process process;
	private HashMap<TFlowElement,AbSyn> hm;
	private HashMap<TDataObject,VarDecl> dataObj;
	private List <BPMNError> errors;
	
	public BPMN2Intermediate(TProcess t, List <BPMNError> l) {
		this.tp = t;
		errors = l;
	}
		
	public List<Process> translate() throws exception.BPMNError {
		List<Process> processes = new LinkedList<Process>();
		for (JAXBElement<? extends TFlowElement> a : tp.getFlowElement()) {
			if (a.getValue() instanceof TStartEvent) {
				TStartEvent startEvent = (TStartEvent) a.getValue();

				hm = new HashMap<TFlowElement, AbSyn>();
				dataObj = new HashMap<TDataObject, VarDecl>();
			
				initializeEmptyProcess(startEvent);
	
				if (errors.isEmpty())
					declareVariables();		
	
				if (errors.isEmpty())
					createCommands();
	
				if (errors.isEmpty())
					linkElements();
				
				if (errors.isEmpty())
					finalizeProcess(startEvent);
				
				processes.add(process);
			}
		}
		
		return processes;
	}
	
	private void initializeEmptyProcess(TStartEvent startEvent) throws exception.BPMNError {
//		TStartEvent startEvent = null;
//		for (JAXBElement<? extends TFlowElement> a : tp.getFlowElement()) {
//			if (a.getValue() instanceof TStartEvent)
//				startEvent = (TStartEvent) a.getValue();
//		}
		
		if (!BPMNHelper.hasProperty(startEvent, "ID")) {
//			errors.add(new MissingPropertyError("Every Start Event in WSN pool should have a property called ID", startEvent));
			int id = FunctionIds.next();
			System.err.println("Start event (" + startEvent.getId() + ") has no property called ID, adding one with data state name " + id);
			TProperty idProperty = BPMNGenerator.generatePropertyWithDataState("ID", String.valueOf(id));
			startEvent.getProperty().add(idProperty);
		}
		
		hm.put(startEvent, null);
		
		for (JAXBElement<? extends TEventDefinition> a : startEvent.getEventDefinition()) {
			TEventDefinition ed = a.getValue();
			
			// support timed processes
			if (ed instanceof TTimerEventDefinition) {
				TTimerEventDefinition ted = (TTimerEventDefinition)ed;
				Variable periodicity = new Variable(ted, ted.getTimeCycle().getContent().get(0).toString());
				process = new TimedProcess(tp, periodicity);
				process.setName(WsnBPMNHelper.getSubProcessName(startEvent));
				return;
			}
		}
		
		process = new Process(startEvent);
		process.setName(WsnBPMNHelper.getSubProcessName(startEvent));
	}
	
	/**
	 * Finalizes the process parsing.
	 * It performs the following actions: set the input arguments of the process.
	 */
	private void finalizeProcess(TCatchEvent startEvent) {
		List<TDataOutputAssociation> out = startEvent.getDataOutputAssociation();
		for(TDataOutputAssociation dataOutp : out) {
			Object o = dataOutp.getTargetRef();
			if (o instanceof TDataObject){
				process.addArgument(dataObj.get((TDataObject)o));
			}
		}
	}
	
	public Set<VarDecl> getVarDecl() {
		Set<VarDecl> v = new HashSet<VarDecl>();
		for (VarDecl var : dataObj.values())
			v.add(var);		
		return v;
	}
	

	private void createCommands() {
		for (JAXBElement<? extends TFlowElement> a : tp.getFlowElement())
			createCommand(a.getValue());
	}

	private void createCommand(TFlowElement value) {

		/*if (value instanceof TStartEvent) {
			TStartEvent v = (TStartEvent) value;		
			hm.put(v, null);
			
		}  else */if (value instanceof TEndEvent) {			
			TEndEvent v = (TEndEvent)value;
			ReturnCommand rc = new ReturnCommand(v, new  HashSet<Variable>(), 
					new  HashSet<Variable>());			
			hm.put(v, rc);
			
		}  else if (value instanceof TTask) {
			
			TTask v = (TTask)value;
			
			HashSet<Variable> inputVar = getVariablesInput(v.getId()); 			

			List<TDataOutputAssociation> out = v.getDataOutputAssociation();
			HashSet<Variable> outputVar = new HashSet<Variable>();
			
			for(TDataOutputAssociation dataOutp : out) {
				Object o = dataOutp.getTargetRef();
				if (o instanceof TDataObject){
					outputVar.add(dataObj.get((TDataObject)o).var);
				}
			}			
			
			if (v.getName().equals("Send") || v instanceof TSendTask) {						
				Send sd = new Send(v, inputVar, outputVar);			
				hm.put(v, sd);								
			} else if (v.getName().equals("Receive") || v instanceof TReceiveTask) {
				Receive r = new Receive(v, inputVar, outputVar);
				hm.put(v, r);
			} else if (v.getName().equals("GetTemp")) {				
				GetTemp gt = new GetTemp(v, inputVar, outputVar);				
				hm.put(v, gt);				
			} else if (v.getName().equals("GetSensorID")) {
				GetSensorID gid = new GetSensorID(v, inputVar, outputVar);
				hm.put(v, gid);
			} else if (v.getName().equals("Blink")) {
				Blink bl = new Blink(v, inputVar, outputVar);
				hm.put(v, bl);
			} else if (v.getName().equals("GetAccel")) {
				GetAccel accel = new GetAccel(v, inputVar, outputVar);
				hm.put(v, accel);
			} else if (v.getName().equals("GetAccelX")) {
				GetAccelX accelX = new GetAccelX(v, inputVar, outputVar);
				hm.put(v, accelX);
			} else if (v.getName().equals("GetAccelY")) {
				GetAccelY accelY = new GetAccelY(v, inputVar, outputVar);
				hm.put(v, accelY);
			} else if (v.getName().equals("GetAccelZ")) {
				GetAccelZ accelZ = new GetAccelZ(v, inputVar, outputVar);
				hm.put(v, accelZ);
			} else if (v.getName().equals("GetBatteryLevel")) {
				GetBatteryLevel battery = new GetBatteryLevel(v, inputVar, 
						outputVar);
				hm.put(v, battery);
			} else if (v.getName().equals("GetInclX")) {
				GetInclX inclX = new GetInclX(v, inputVar, outputVar);
				hm.put(v, inclX);
			} else if (v.getName().equals("GetInclY")) {
				GetInclY inclY = new GetInclY(v, inputVar, outputVar);
				hm.put(v, inclY);
			} else if (v.getName().equals("GetInclZ")) {
				GetInclZ inclZ = new GetInclZ(v, inputVar, outputVar);
				hm.put(v, inclZ);
			} else if (v.getName().equals("GetLuminosity")) {
				GetLuminosity lumi = new GetLuminosity(v, inputVar, outputVar);
				hm.put(v, lumi);
			} else if (v.getName().equals("GetTime")) {
				GetTime time = new GetTime(v, inputVar, outputVar);
				hm.put(v, time);
			} else if (v.getName().equals("LogDouble")) {
				LogDouble log = new LogDouble(v, inputVar, outputVar);
				hm.put(v, log);
			} else if (v.getName().equals("LogLong")) {
				LogLong log = new LogLong(v, inputVar, outputVar);
				hm.put(v, log);
			} else if (v.getName().equals("LogString")) {
				LogString log = new LogString(v, inputVar, outputVar);
				hm.put(v, log);
			} else if (v.getName().toLowerCase().equals("macaddr")) {
				MacAddr mac = new MacAddr(v, inputVar, outputVar);
				hm.put(v, mac);
			} else if (v.getName().equals("SetLEDCollor")) {
				SetLEDCollor collor = new SetLEDCollor(v, inputVar, outputVar);
				hm.put(v, collor);
			} else if (v.getName().equals("SetLEDOn")) {
				SetLEDOn on = new SetLEDOn(v, inputVar, outputVar);
				hm.put(v, on);
			} else {
				InvalidFunctionNameError functionError =
						new InvalidFunctionNameError("Function name '" + v.getName() + 
								"' not supported: ", v);
				errors.add(functionError);
			}
		} else if (value instanceof TGateway) {
			TGateway gat = (TGateway)value;
			
			if (gat.getGatewayDirection()
					.equals(TGatewayDirection.CONVERGING)) {
				Join join = new Join(gat, new HashSet<Variable>(),
						new HashSet<Variable>());
				hm.put(gat, join);
			} else if (gat.getGatewayDirection()
					.equals(TGatewayDirection.DIVERGING)){
				If ifc = new If(gat, new HashSet<Variable>(),
						new HashSet<Variable>());
				hm.put(gat, ifc);
			} else {
				GatewayNotSupportedError gatewayError = 
						new GatewayNotSupportedError("Gateway not supported:", 
								gat);
				errors.add(gatewayError);
			}
		} 
	}
	
	private TFlowElement getElement(String name) {
		Object[] arr = hm.keySet().toArray();
		
		TFlowElement elem = null;

		for (Object el : arr){
			TFlowElement e = (TFlowElement)el;
			if(e.toString().equals(name))
				elem = e;		
		}
		
		return elem;
	}
	
	
	private void linkElements() {
		for (JAXBElement<? extends TFlowElement> a : tp.getFlowElement())
			if (a.getValue() instanceof TSequenceFlow)
					dealSequence((TSequenceFlow)a.getValue());
	}
	
	private void dealSequence(TSequenceFlow v) {
			//objects in bpmn representation
			TFlowElement src = getElement(v.getSourceRef().toString());
			TFlowElement trg = getElement(v.getTargetRef().toString());

			//objects in our representation
			AbSyn srcI = hm.get(src);
			AbSyn trgI = hm.get(trg);
			
			if (src instanceof TStartEvent) {
				process.setInitialCommand((Command)trgI);
				return;
			}	
			
			//check if this sequence flow belongs to an if
			//restriction to set name only for if cases
//			boolean isIF = v.getName() != null;
			boolean isIF = srcI instanceof If;
			
			if(isIF) {
				If i = (If)srcI;
				if (v.getName() != null) {
					if (v.getName().equals("default")) {
						i.setContinuation((Command)trgI);
					} else {
						GuardedCommand gc = new GuardedCommand(
								new Value(v,v.getName()),
								(Command)trgI, v, new HashSet<Variable>(), 
								new HashSet<Variable>());
						i.addGuardedCommand(gc);
					}
				}
				else {
					IfBranchEmptyNameError emptyNameError = 
							new IfBranchEmptyNameError(
									"Sequence flow originating from a gateway should "+
											"have an expression (or \"default\")", v);
					errors.add(emptyNameError);
				}
			} else if (srcI != null)
				((Command) srcI).setContinuation((Command)trgI);	
	}	
	
	private void declareVariables() {
		for (JAXBElement<? extends TFlowElement> a : tp.getFlowElement())
			if(a.getValue() instanceof TDataObject)
				declareVariable((TDataObject) a.getValue());
	}
	
	private void declareVariable(TDataObject data) {		
		String name = data.getName();
		Type t = null;
		if(data.getOtherAttributes().values().contains("Boolean"))
			t = new TypeBool();
		else if(data.getOtherAttributes().values().contains("Float"))
			t = new TypeDouble();			
		else if(data.getOtherAttributes().values().contains("Double"))
			t = new TypeDouble();			
		else if(data.getOtherAttributes().values().contains("Long"))
			t = new TypeLong();			
		else if (data.getOtherAttributes().values().contains("Integer")) 
			t = new TypeLong();
		else //pag 236 standard - types are underspecified
			t = new TypeString();
		
		if (data.getDataState() != null) {
			// has data state so consider this is a constant
			if (t instanceof TypeString) name = "\"" + data.getDataState().getName() + "\"";
			else name = data.getDataState().getName();
		}

		VarDecl var = new VarDecl(new Variable(data, name), t, data);
		process.addVarDecl(var);
		dataObj.put(data, var);
	}
	
	/**
	 * Search input variables for specified element ID 
	 * @param elemID ID of element to be searched
	 * @return A Set of Variables of element
	 */
	private  HashSet<Variable> getVariablesInput(String elemID) {
		HashSet<Variable> vars = new HashSet<Variable>();		
		for (JAXBElement<? extends TArtifact> a : tp.getArtifact()) {
			TArtifact e = a.getValue();
			if (e instanceof TAssociation) {
				String varId = ((TAssociation) e).
						getSourceRef().getLocalPart();
				if (elemID.equals(((TAssociation) e).getTargetRef().
						getLocalPart())) {
					for (TDataObject d : dataObj.keySet())
						if(d.getId().equals(varId))
							vars.add(dataObj.get(d).var);
				}
			}
		}		
		return vars;
	}
}
