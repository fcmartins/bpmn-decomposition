package decompose;

import java.util.HashSet;
import java.util.Set;

import bpmn2.TFlowElement;

public class Node implements Comparable<Node> {

	/**
	 * The value of this Node.
	 */
	private TFlowElement flowElement;
	private String pool;
	private Set<Node> next;
	private Set<Node> previous;

	
	/**
	 * Creates a new Node
	 * 
	 * @param tFlowElement
	 *            - the value associated to this Node.
	 */
	public Node(TFlowElement flowElement, String pool) {
		this.flowElement = flowElement;
		this.pool = pool;
		next = new HashSet<>();
		previous = new HashSet<>();
	}

	/**
	 * Gets the value of this Node.
	 * 
	 * @return this Node's value.
	 */
	public TFlowElement getValue() {
		return flowElement;
	}

	@Override
	public String toString() {
		return flowElement.getName();
	}

	/**
	 * Visits this Node.
	 * 
	 * @param visitor
	 */
	public void accept(IGraphVisitor visitor) {
		visitor.visitNode(this);
	}

	/**
	 * Sets the value of this Node.
	 * 
	 * @param value
	 */
	public void setValue(TFlowElement flowElement) {
		this.flowElement = flowElement;
	}

	@Override
	public int compareTo(Node o) {		
		return (this == o && this.flowElement == o.flowElement && this.pool == o.pool && this.previous == o.previous && this.next == o.next) ? 0 : -1;
	}
	
	@Override
	public boolean equals(Object other){
	    if (other == null){
	    	return false;
	    }
	    if (other == this){
	    	return true;
	    }
	    if (!(other instanceof Node)){
	    	return false;
	    }
	    Node otherNode = (Node)other;
	    return this.getValue() == otherNode.getValue() && this.getPool() == otherNode.getPool()&& this.getPrevious() == otherNode.getPrevious() && this.getNext() == otherNode.getNext();
	}
/*
	@Override
	public int hashCode(){
		int result = 17;
		result *= 31 + flowElement.hashCode();
		result *= 31 + pool.hashCode();
		for(Node prev : previous){
			result *= 31 + prev.hashCode();
		}
		for(Node nextNode : next){
			result *= 31 + nextNode.hashCode();
		}
		return result;
	}*/
	
	public String getPool(){
		return pool;
	}
	
	public void setPool(String pool){
		this.pool = pool;
	}
	
	public Set<Node> getNext(){
		return next;
	}
	
	public void addNext(Node add){
		next.add(add);
	}
	
	public Set<Node> getPrevious(){
		return previous;
	}
	
	public void addPrevious(Node add){
		previous.add(add);
	} 
}
