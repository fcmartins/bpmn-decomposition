package jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

public class JaxbHelper {
	
    public static <T> JAXBElement<T> toJAXBElement(Namespace ns, String xmlName, T object) {
        QName qname = new QName(ns.getNamespaceURI(), xmlName);
        return new JAXBElement<T>(qname, (Class<T>) object.getClass(), object);
    }
    
}
