
package bpmn2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResourceParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResourceParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="Selection" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *         &lt;element name="Availability" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *         &lt;element name="Quantity" type="{http://www.bpsim.org/schemas/1.0}Parameter" minOccurs="0"/>
 *         &lt;element name="Role" type="{http://www.bpsim.org/schemas/1.0}Parameter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResourceParameters", namespace = "http://www.bpsim.org/schemas/1.0", propOrder = {
    "selection",
    "availability",
    "quantity",
    "role"
})
public class ResourceParameters {

    @XmlElement(name = "Selection", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter selection;
    @XmlElement(name = "Availability", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter availability;
    @XmlElement(name = "Quantity", namespace = "http://www.bpsim.org/schemas/1.0")
    protected Parameter quantity;
    @XmlElement(name = "Role", namespace = "http://www.bpsim.org/schemas/1.0")
    protected List<Parameter> role;

    /**
     * Gets the value of the selection property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getSelection() {
        return selection;
    }

    /**
     * Sets the value of the selection property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setSelection(Parameter value) {
        this.selection = value;
    }

    /**
     * Gets the value of the availability property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getAvailability() {
        return availability;
    }

    /**
     * Sets the value of the availability property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setAvailability(Parameter value) {
        this.availability = value;
    }

    /**
     * Gets the value of the quantity property.
     * 
     * @return
     *     possible object is
     *     {@link Parameter }
     *     
     */
    public Parameter getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Parameter }
     *     
     */
    public void setQuantity(Parameter value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the role property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the role property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRole().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Parameter }
     * 
     * 
     */
    public List<Parameter> getRole() {
        if (role == null) {
            role = new ArrayList<Parameter>();
        }
        return this.role;
    }

}
