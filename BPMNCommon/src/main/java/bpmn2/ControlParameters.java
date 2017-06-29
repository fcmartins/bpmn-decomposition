
package bpmn2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ControlParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ControlParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="Probability" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *         &lt;element name="Condition" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *         &lt;element name="InterTriggerTimer" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *         &lt;element name="TriggerCount" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ControlParameters", namespace = "http://www.bpsim.org/schemas/1.0", propOrder = {
    "probability",
    "condition",
    "interTriggerTimer",
    "triggerCount"
})
public class ControlParameters {

    @XmlElement(name = "Probability", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter probability;
    @XmlElement(name = "Condition", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter condition;
    @XmlElement(name = "InterTriggerTimer", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter interTriggerTimer;
    @XmlElement(name = "TriggerCount", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter triggerCount;

    /**
     * Gets the value of the probability property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getProbability() {
        return probability;
    }

    /**
     * Sets the value of the probability property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setProbability(Parameter value) {
        this.probability = value;
    }

    /**
     * Gets the value of the condition property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getCondition() {
        return condition;
    }

    /**
     * Sets the value of the condition property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setCondition(Parameter value) {
        this.condition = value;
    }

    /**
     * Gets the value of the interTriggerTimer property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getInterTriggerTimer() {
        return interTriggerTimer;
    }

    /**
     * Sets the value of the interTriggerTimer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setInterTriggerTimer(Parameter value) {
        this.interTriggerTimer = value;
    }

    /**
     * Gets the value of the triggerCount property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getTriggerCount() {
        return triggerCount;
    }

    /**
     * Sets the value of the triggerCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setTriggerCount(Parameter value) {
        this.triggerCount = value;
    }

}
