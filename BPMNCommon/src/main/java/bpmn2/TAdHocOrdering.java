
package bpmn2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tAdHocOrdering.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tAdHocOrdering">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Parallel"/>
 *     &lt;enumeration value="Sequential"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tAdHocOrdering", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
@XmlEnum
public enum TAdHocOrdering {

    @XmlEnumValue("Parallel")
    PARALLEL("Parallel"),
    @XmlEnumValue("Sequential")
    SEQUENTIAL("Sequential");
    private final String value;

    TAdHocOrdering(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TAdHocOrdering fromValue(String v) {
        for (TAdHocOrdering c: TAdHocOrdering.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
