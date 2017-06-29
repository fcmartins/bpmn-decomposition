
package bpmn2;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TimeUnit.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TimeUnit">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="ms"/>
 *     &lt;enumeration value="s"/>
 *     &lt;enumeration value="min"/>
 *     &lt;enumeration value="hour"/>
 *     &lt;enumeration value="day"/>
 *     &lt;enumeration value="year"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TimeUnit", namespace = "http://www.bpsim.org/schemas/1.0")
@XmlEnum
public enum TimeUnit {

    @XmlEnumValue("ms")
    MS("ms"),
    @XmlEnumValue("s")
    S("s"),
    @XmlEnumValue("min")
    MIN("min"),
    @XmlEnumValue("hour")
    HOUR("hour"),
    @XmlEnumValue("day")
    DAY("day"),
    @XmlEnumValue("year")
    YEAR("year");
    private final String value;

    TimeUnit(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TimeUnit fromValue(String v) {
        for (TimeUnit c: TimeUnit.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
