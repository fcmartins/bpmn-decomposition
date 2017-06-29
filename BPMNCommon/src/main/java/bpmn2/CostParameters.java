
package bpmn2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CostParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CostParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="FixedCost" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *         &lt;element name="UnitCost" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CostParameters", namespace = "http://www.bpsim.org/schemas/1.0", propOrder = {
    "fixedCost",
    "unitCost"
})
public class CostParameters {

    @XmlElement(name = "FixedCost", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter fixedCost;
    @XmlElement(name = "UnitCost", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter unitCost;

    /**
     * Gets the value of the fixedCost property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getFixedCost() {
        return fixedCost;
    }

    /**
     * Sets the value of the fixedCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setFixedCost(Parameter value) {
        this.fixedCost = value;
    }

    /**
     * Gets the value of the unitCost property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getUnitCost() {
        return unitCost;
    }

    /**
     * Sets the value of the unitCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setUnitCost(Parameter value) {
        this.unitCost = value;
    }

}
