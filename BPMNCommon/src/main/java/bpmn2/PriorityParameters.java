
package bpmn2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PriorityParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PriorityParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="Interruptible" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *         &lt;element name="Priority" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PriorityParameters", namespace = "http://www.bpsim.org/schemas/1.0", propOrder = {
    "interruptible",
    "priority"
})
public class PriorityParameters {

    @XmlElement(name = "Interruptible", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter interruptible;
    @XmlElement(name = "Priority", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter priority;

    /**
     * Gets the value of the interruptible property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getInterruptible() {
        return interruptible;
    }

    /**
     * Sets the value of the interruptible property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setInterruptible(Parameter value) {
        this.interruptible = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setPriority(Parameter value) {
        this.priority = value;
    }

}
