package jaxb;

public enum Namespace {
    //ROOT("http://www.omg.org/bpmn20"),
    ROOT_BPMN2("http://www.omg.org/bpmn20"),
    ROOT_JBPM("http://www.omg.org/bpmn20"),
    ROOT("http://org.eclipse.bpmn2/default/collaboration"),
	BPMN2("http://www.omg.org/spec/BPMN/20100524/MODEL"),
    BPMNDI("http://www.omg.org/spec/BPMN/20100524/DI"),
    COLLABORATION("http://org.eclipse.bpmn2/default/collaboration"),
    DROOLS("http://www.jboss.org/drools"),
    XS("http://www.w3.org/2001/XMLSchema");

    private String namespaceURI;

    Namespace(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    public String getNamespaceURI() {
        return namespaceURI;
    }
}