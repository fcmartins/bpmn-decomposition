
package bpmn2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tChoreographyLoopType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="tChoreographyLoopType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="Standard"/>
 *     &lt;enumeration value="MultiInstanceSequential"/>
 *     &lt;enumeration value="MultiInstanceParallel"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "tChoreographyLoopType", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
@XmlEnum
public enum TChoreographyLoopType {

    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Standard")
    STANDARD("Standard"),
    @XmlEnumValue("MultiInstanceSequential")
    MULTI_INSTANCE_SEQUENTIAL("MultiInstanceSequential"),
    @XmlEnumValue("MultiInstanceParallel")
    MULTI_INSTANCE_PARALLEL("MultiInstanceParallel");
    private final String value;

    TChoreographyLoopType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TChoreographyLoopType fromValue(String v) {
        for (TChoreographyLoopType c: TChoreographyLoopType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
