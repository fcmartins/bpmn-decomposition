package jaxb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public class XMLMarshaller {
    public XMLMarshaller() {
    }

    @SuppressWarnings("restriction")
    public <T> T unmarshall(String filePath, Class<T> targetClass, Class<?>... extraClasses) {
        try {
            List<Class<?>> neededClasses = new LinkedList<>();
            neededClasses.add(targetClass);
            neededClasses.addAll(Arrays.asList(extraClasses));

            Class[] neededClassesArray = new Class[neededClasses.size()];
            neededClasses.toArray(neededClassesArray);

            JAXBContext context = JAXBContext.newInstance(neededClassesArray);
            Unmarshaller u = context.createUnmarshaller();
            JAXBElement parsed = (JAXBElement) u.unmarshal(new FileInputStream(filePath));
            return (T) parsed.getValue();
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("restriction")
	public void marshall(Object object, String filePath, Class<?>... classes) throws SAXException {
        try {
            JAXBContext context = JAXBContext.newInstance(classes);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.FALSE);
//            m.setSchema(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
//            		.newSchema(this.getClass().getClassLoader().getResource("schema/xsd/BPMN20.xsd")));
            m.marshal(object, new FileOutputStream(filePath));
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
