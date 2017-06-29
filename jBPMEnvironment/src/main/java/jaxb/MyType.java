package jaxb;

import javax.xml.bind.annotation.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

@XmlRootElement(name="my-type")
@XmlAccessorType(XmlAccessType.FIELD)
public class MyType implements Serializable {

    @XmlElement
    private byte[] bytes;

    public MyType() {
        // default constructor
    }

    public void setObject(Object object) throws IOException {
        bytes = JaxbHelper.marshall(object);
    }

    public Object getObject() throws IOException, ClassNotFoundException {
        return JaxbHelper.unmarshall(bytes);
    }

    public String toString() {
        try {
            Object object = getObject();
            if (object instanceof Boolean) return "Boolean(" + object + ")";
            else if (object instanceof Number) return "Number(" + object + ")";
            else if (object instanceof String) return "String(" + object + ")";
            else if (object instanceof Object[]) return Arrays.deepToString((Object[]) object);
            return object.toString();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}