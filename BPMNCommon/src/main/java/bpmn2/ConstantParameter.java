
package bpmn2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ConstantParameter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConstantParameter">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.bpsim.org/schemas/1.0}ParameterValue">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConstantParameter", namespace = "http://www.bpsim.org/schemas/1.0")
@XmlSeeAlso({
    StringParameter.class,
    BooleanParameter.class,
    DurationParameter.class,
    FloatingParameter.class,
    NumericParameter.class,
    DateTimeParameter.class
})
public class ConstantParameter
    extends ParameterValue
{


}
