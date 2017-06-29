package jaxb;

import java.io.*;

public class JaxbHelper {
    public static byte[] marshall(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] result;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            result = bos.toByteArray();
        }
        finally {
            if (out != null) out.close();
            bos.close();
        }

        return result;
    }

    public static Object unmarshall(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        Object result;
        try {
            in = new ObjectInputStream(bis);
            result = in.readObject();
        }
        finally {
            if (in != null) in.close();
            bis.close();
        }

        return result;
    }
}
