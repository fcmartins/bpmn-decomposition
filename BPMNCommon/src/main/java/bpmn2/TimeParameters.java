
package bpmn2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TimeParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TimeParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="TransferTime" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *         &lt;element name="QueueTime" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *         &lt;element name="WaitTime" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *         &lt;element name="SetUpTime" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *         &lt;element name="ProcessingTime" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *         &lt;element name="ValidationTime" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *         &lt;element name="ReworkTime" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TimeParameters", namespace = "http://www.bpsim.org/schemas/1.0", propOrder = {
    "transferTime",
    "queueTime",
    "waitTime",
    "setUpTime",
    "processingTime",
    "validationTime",
    "reworkTime"
})
public class TimeParameters {

    @XmlElement(name = "TransferTime", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter transferTime;
    @XmlElement(name = "QueueTime", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter queueTime;
    @XmlElement(name = "WaitTime", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter waitTime;
    @XmlElement(name = "SetUpTime", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter setUpTime;
    @XmlElement(name = "ProcessingTime", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter processingTime;
    @XmlElement(name = "ValidationTime", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter validationTime;
    @XmlElement(name = "ReworkTime", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter reworkTime;

    /**
     * Gets the value of the transferTime property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getTransferTime() {
        return transferTime;
    }

    /**
     * Sets the value of the transferTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setTransferTime(Parameter value) {
        this.transferTime = value;
    }

    /**
     * Gets the value of the queueTime property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getQueueTime() {
        return queueTime;
    }

    /**
     * Sets the value of the queueTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setQueueTime(Parameter value) {
        this.queueTime = value;
    }

    /**
     * Gets the value of the waitTime property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getWaitTime() {
        return waitTime;
    }

    /**
     * Sets the value of the waitTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setWaitTime(Parameter value) {
        this.waitTime = value;
    }

    /**
     * Gets the value of the setUpTime property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getSetUpTime() {
        return setUpTime;
    }

    /**
     * Sets the value of the setUpTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setSetUpTime(Parameter value) {
        this.setUpTime = value;
    }

    /**
     * Gets the value of the processingTime property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getProcessingTime() {
        return processingTime;
    }

    /**
     * Sets the value of the processingTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setProcessingTime(Parameter value) {
        this.processingTime = value;
    }

    /**
     * Gets the value of the validationTime property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getValidationTime() {
        return validationTime;
    }

    /**
     * Sets the value of the validationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setValidationTime(Parameter value) {
        this.validationTime = value;
    }

    /**
     * Gets the value of the reworkTime property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getReworkTime() {
        return reworkTime;
    }

    /**
     * Sets the value of the reworkTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setReworkTime(Parameter value) {
        this.reworkTime = value;
    }

}
