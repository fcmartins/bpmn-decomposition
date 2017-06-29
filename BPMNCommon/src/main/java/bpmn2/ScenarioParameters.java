
package bpmn2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ScenarioParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ScenarioParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="Start" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *         &lt;element name="Duration" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *         &lt;element name="PropertyParameters" type="{http://www.bpsim.org/schemas/1.0}PropertyParameters" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="replication" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="seed" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="baseTimeUnit" type="{http://www.bpsim.org/schemas/1.0}TimeUnit" />
 *       &lt;attribute name="baseCurrencyUnit" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ScenarioParameters", namespace = "http://www.bpsim.org/schemas/1.0", propOrder = {
    "start",
    "duration",
    "propertyParameters"
})
public class ScenarioParameters {

    @XmlElement(name = "Start", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter start;
    @XmlElement(name = "Duration", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter duration;
    @XmlElement(name = "PropertyParameters", namespace = "http://www.bpsim.org/schemas/1.0")
    protected PropertyParameters propertyParameters;
    @XmlAttribute(name = "replication")
    protected Integer replication;
    @XmlAttribute(name = "seed")
    protected Long seed;
    @XmlAttribute(name = "baseTimeUnit")
    protected TimeUnit baseTimeUnit;
    @XmlAttribute(name = "baseCurrencyUnit")
    protected String baseCurrencyUnit;

    /**
     * Gets the value of the start property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getStart() {
        return start;
    }

    /**
     * Sets the value of the start property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setStart(Parameter value) {
        this.start = value;
    }

    /**
     * Gets the value of the duration property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getDuration() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setDuration(Parameter value) {
        this.duration = value;
    }

    /**
     * Gets the value of the propertyParameters property.
     * 
     * @return
     *     possible object is
     *     {@link PropertyParameters }
     *     
     */
    public PropertyParameters getPropertyParameters() {
        return propertyParameters;
    }

    /**
     * Sets the value of the propertyParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertyParameters }
     *     
     */
    public void setPropertyParameters(PropertyParameters value) {
        this.propertyParameters = value;
    }

    /**
     * Gets the value of the replication property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getReplication() {
        return replication;
    }

    /**
     * Sets the value of the replication property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setReplication(Integer value) {
        this.replication = value;
    }

    /**
     * Gets the value of the seed property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSeed() {
        return seed;
    }

    /**
     * Sets the value of the seed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSeed(Long value) {
        this.seed = value;
    }

    /**
     * Gets the value of the baseTimeUnit property.
     * 
     * @return
     *     possible object is
     *     {@link TimeUnit }
     *     
     */
    public TimeUnit getBaseTimeUnit() {
        return baseTimeUnit;
    }

    /**
     * Sets the value of the baseTimeUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeUnit }
     *     
     */
    public void setBaseTimeUnit(TimeUnit value) {
        this.baseTimeUnit = value;
    }

    /**
     * Gets the value of the baseCurrencyUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseCurrencyUnit() {
        return baseCurrencyUnit;
    }

    /**
     * Sets the value of the baseCurrencyUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseCurrencyUnit(String value) {
        this.baseCurrencyUnit = value;
    }

}
