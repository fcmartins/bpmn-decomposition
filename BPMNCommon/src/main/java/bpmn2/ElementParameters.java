
package bpmn2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for ElementParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ElementParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="TimeParameters" type="{http://www.bpsim.org/schemas/1.0}TimeParameters" minOccurs="0"/>
 *         &lt;element name="ControlParameters" type="{http://www.bpsim.org/schemas/1.0}ControlParameters" minOccurs="0"/>
 *         &lt;element name="ResourceParameters" type="{http://www.bpsim.org/schemas/1.0}ResourceParameters" minOccurs="0"/>
 *         &lt;element name="PriorityParameters" type="{http://www.bpsim.org/schemas/1.0}PriorityParameters" minOccurs="0"/>
 *         &lt;element name="CostParameters" type="{http://www.bpsim.org/schemas/1.0}CostParameters" minOccurs="0"/>
 *         &lt;element name="PropertyParameters" type="{http://www.bpsim.org/schemas/1.0}PropertyParameters" minOccurs="0"/>
 *         &lt;element name="VendorExtension" type="{http://www.bpsim.org/schemas/1.0}VendorExtension" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="elementRef" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ElementParameters", namespace = "http://www.bpsim.org/schemas/1.0", propOrder = {
    "timeParameters",
    "controlParameters",
    "resourceParameters",
    "priorityParameters",
    "costParameters",
    "propertyParameters",
    "vendorExtension"
})
public class ElementParameters {

    @XmlElement(name = "TimeParameters", namespace = "http://www.bpsim.org/schemas/1.0")
    protected TimeParameters timeParameters;
    @XmlElement(name = "ControlParameters", namespace = "http://www.bpsim.org/schemas/1.0")
    protected ControlParameters controlParameters;
    @XmlElement(name = "ResourceParameters", namespace = "http://www.bpsim.org/schemas/1.0")
    protected ResourceParameters resourceParameters;
    @XmlElement(name = "PriorityParameters", namespace = "http://www.bpsim.org/schemas/1.0")
    protected PriorityParameters priorityParameters;
    @XmlElement(name = "CostParameters", namespace = "http://www.bpsim.org/schemas/1.0")
    protected CostParameters costParameters;
    @XmlElement(name = "PropertyParameters", namespace = "http://www.bpsim.org/schemas/1.0")
    protected PropertyParameters propertyParameters;
    @XmlElement(name = "VendorExtension", namespace = "http://www.bpsim.org/schemas/1.0")
    protected List<VendorExtension> vendorExtension;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "elementRef")
    protected String elementRef;

    /**
     * Gets the value of the timeParameters property.
     * 
     * @return
     *     possible object is
     *     {@link TimeParameters }
     *     
     */
    public TimeParameters getTimeParameters() {
        return timeParameters;
    }

    /**
     * Sets the value of the timeParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeParameters }
     *     
     */
    public void setTimeParameters(TimeParameters value) {
        this.timeParameters = value;
    }

    /**
     * Gets the value of the controlParameters property.
     * 
     * @return
     *     possible object is
     *     {@link ControlParameters }
     *     
     */
    public ControlParameters getControlParameters() {
        return controlParameters;
    }

    /**
     * Sets the value of the controlParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlParameters }
     *     
     */
    public void setControlParameters(ControlParameters value) {
        this.controlParameters = value;
    }

    /**
     * Gets the value of the resourceParameters property.
     * 
     * @return
     *     possible object is
     *     {@link ResourceParameters }
     *     
     */
    public ResourceParameters getResourceParameters() {
        return resourceParameters;
    }

    /**
     * Sets the value of the resourceParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResourceParameters }
     *     
     */
    public void setResourceParameters(ResourceParameters value) {
        this.resourceParameters = value;
    }

    /**
     * Gets the value of the priorityParameters property.
     * 
     * @return
     *     possible object is
     *     {@link PriorityParameters }
     *     
     */
    public PriorityParameters getPriorityParameters() {
        return priorityParameters;
    }

    /**
     * Sets the value of the priorityParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link PriorityParameters }
     *     
     */
    public void setPriorityParameters(PriorityParameters value) {
        this.priorityParameters = value;
    }

    /**
     * Gets the value of the costParameters property.
     * 
     * @return
     *     possible object is
     *     {@link CostParameters }
     *     
     */
    public CostParameters getCostParameters() {
        return costParameters;
    }

    /**
     * Sets the value of the costParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link CostParameters }
     *     
     */
    public void setCostParameters(CostParameters value) {
        this.costParameters = value;
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
     * Gets the value of the vendorExtension property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vendorExtension property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVendorExtension().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VendorExtension }
     * 
     * 
     */
    public List<VendorExtension> getVendorExtension() {
        if (vendorExtension == null) {
            vendorExtension = new ArrayList<VendorExtension>();
        }
        return this.vendorExtension;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the elementRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getElementRef() {
        return elementRef;
    }

    /**
     * Sets the value of the elementRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setElementRef(String value) {
        this.elementRef = value;
    }

}
