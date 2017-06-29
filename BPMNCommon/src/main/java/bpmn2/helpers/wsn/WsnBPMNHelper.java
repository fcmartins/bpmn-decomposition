package bpmn2.helpers.wsn;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bpmn2.TDefinitions;
import bpmn2.TEvent;
import bpmn2.TMessageFlow;
import bpmn2.TParticipant;
import bpmn2.TReceiveTask;
import bpmn2.TSendTask;
import bpmn2.helpers.BPMNGenerator;
import bpmn2.helpers.BPMNHelper;
import exception.BPMNError;

/**
 * This class has methods to help when handling with BPMN models.
 * Every method has no side-effects.
 * These methods follow ideas specific to the project and don't represent, at all,
 * the BPMN specification.
 *
 */
public class WsnBPMNHelper {

    private static final Pattern PATTERN_WSN_POOL_NAME = Pattern.compile("WSN=(.*)");

    public static TParticipant getCentralPool(TDefinitions definitions) throws BPMNError {
    	List<TParticipant> pools = BPMNHelper.getPools(definitions);
        for (TParticipant pool : pools) {
        	if (!isWsnPool(definitions, pool)) return pool;
        }
        
        throw new BPMNError("There is no central pool");
    }
    
    public static String getSourceWsnId(TDefinitions definitions, TReceiveTask receiveTask) throws BPMNError {
    	TMessageFlow messageFlow = BPMNHelper.getMessageFlowWithTaskAsTarget(definitions, receiveTask);
    	TParticipant pool = BPMNHelper.getSourcePoolOfMessageFlow(definitions, messageFlow);
    	return getWsnId(definitions, pool);
    }
    
    public static String getSubProcessName(TEvent event) throws BPMNError {
    	Object id = BPMNHelper.getProperty(event, "ID", false);
    	if (id == null) id = BPMNGenerator.generateRandomId();
    	return "wsn_function" + id;
    }
    
    public static String getTargetWsnId(TDefinitions definitions, TSendTask sendTask) throws BPMNError {
    	TMessageFlow messageFlow = BPMNHelper.getMessageFlowWithTaskAsSource(definitions, sendTask);
    	TParticipant pool = BPMNHelper.getTargetPoolOfMessageFlow(definitions, messageFlow);
    	return getWsnId(definitions, pool);
    }
    
    /**
     * 
     * @requires isWsnPool(definitions, pool);
     */
    public static String getWsnId(TDefinitions definitions, TParticipant pool) throws BPMNError {
        Matcher m = PATTERN_WSN_POOL_NAME.matcher(pool.getName());
        m.find();
        return m.group(1);
    }
    
    public static List<TParticipant> getWsnPools(TDefinitions definitions) throws BPMNError {
    	List<TParticipant> wsnPools = new LinkedList<TParticipant>();
    	List<TParticipant> pools = BPMNHelper.getPools(definitions);
        for (TParticipant pool : pools) {
            if (isWsnPool(definitions, pool)) wsnPools.add(pool);
        }
        
        return wsnPools;
    }

    public static boolean isWsnPool(TDefinitions definitions, TParticipant pool) throws BPMNError {
        Matcher m = PATTERN_WSN_POOL_NAME.matcher(pool.getName());
        return m.find();
    }
    
}
