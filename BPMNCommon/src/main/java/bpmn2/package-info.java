@javax.xml.bind.annotation.XmlSchema(
        xmlns = {
                @javax.xml.bind.annotation.XmlNs(prefix = "bpmn2", namespaceURI="http://www.omg.org/spec/BPMN/20100524/MODEL"),
                @javax.xml.bind.annotation.XmlNs(prefix = "bpmndi", namespaceURI="http://www.omg.org/spec/BPMN/20100524/DI"),
                @javax.xml.bind.annotation.XmlNs(prefix = "di", namespaceURI="http://www.omg.org/spec/DD/20100524/DI"),
                @javax.xml.bind.annotation.XmlNs(prefix = "dc", namespaceURI="http://www.omg.org/spec/DD/20100524/DC"),
                
                //@javax.xml.bind.annotation.XmlNs(prefix = "", namespaceURI="http://www.omg.org/bpmn20"),
                
                @javax.xml.bind.annotation.XmlNs(prefix = "xs", namespaceURI="http://www.w3.org/2001/XMLSchema"),
                @javax.xml.bind.annotation.XmlNs(prefix = "xsi", namespaceURI="http://www.w3.org/2001/XMLSchema-instance"),

                // drools (for jBPM)
                //@javax.xml.bind.annotation.XmlNs(prefix = "drools", namespaceURI="http://www.jboss.org/drools"),
                
                // eclipse
                @javax.xml.bind.annotation.XmlNs(prefix = "", namespaceURI="http://org.eclipse.bpmn2/default/collaboration"),
                @javax.xml.bind.annotation.XmlNs(prefix = "ext", namespaceURI="http://org.eclipse.bpmn2/ext")
        },
        //namespace = "http://www.omg.org/bpmn20",
        namespace = "http://org.eclipse.bpmn2/default/collaboration",
        elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED
)
package bpmn2;
