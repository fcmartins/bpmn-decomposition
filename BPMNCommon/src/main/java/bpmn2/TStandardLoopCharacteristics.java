
package bpmn2;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tStandardLoopCharacteristics complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tStandardLoopCharacteristics">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/spec/BPMN/20100524/MODEL}tLoopCharacteristics">
 *       &lt;sequence>
 *         &lt;element name="loopCondition" type="{http://www.omg.org/spec/BPMN/20100524/MODEL}tExpression" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="testBefore" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="loopMaximum" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tStandardLoopCharacteristics", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", propOrder = {
    "loopCondition"
})
public class TStandardLoopCharacteristics
    extends TLoopCharacteristics
{

    @XmlElement(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
    protected TExpression loopCondition;
    @XmlAttribute(name = "testBefore")
    protected Boolean testBefore;
    @XmlAttribute(name = "loopMaximum")
    protected BigInteger loopMaximum;

    /**
     * Gets the value of the loopCondition property.
     * 
     * @return
     *     possible object is
     *     {@link TExpression }
     *     
     */
    public TExpression getLoopCondition() {
        return loopCondition;
    }

    /**
     * Sets the value of the loopCondition property.
     * 
     * @param value
     *     allowed object is
     *     {@link TExpression }
     *     
     */
    public void setLoopCondition(TExpression value) {
        this.loopCondition = value;
    }

    /**
     * Gets the value of the testBefore property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isTestBefore() {
        if (testBefore == null) {
            return false;
        } else {
            return testBefore;
        }
    }

    /**
     * Sets the value of the testBefore property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTestBefore(Boolean value) {
        this.testBefore = value;
    }

    /**
     * Gets the value of the loopMaximum property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLoopMaximum() {
        return loopMaximum;
    }

    /**
     * Sets the value of the loopMaximum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLoopMaximum(BigInteger value) {
        this.loopMaximum = value;
    }

}
