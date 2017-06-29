
package bpmn2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Scenario complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Scenario">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="ScenarioParameters" type="{http://www.bpsim.org/schemas/1.0}ScenarioParameters" minOccurs="0"/>
 *         &lt;element name="ElementParameters" type="{http://www.bpsim.org/schemas/1.0}ElementParameters" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Calendar" type="{http://www.bpsim.org/schemas/1.0}Calendar" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="VendorExtension" type="{http://www.bpsim.org/schemas/1.0}VendorExtension" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="author" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="vendor" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="inherits" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="result" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="created" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="modified" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Scenario", namespace = "http://www.bpsim.org/schemas/1.0", propOrder = {
    "scenarioParameters",
    "elementParameters",
    "calendar",
    "vendorExtension"
})
public class Scenario {

    @XmlElement(name = "ScenarioParameters", namespace = "http://www.bpsim.org/schemas/1.0")
    protected ScenarioParameters scenarioParameters;
    @XmlElement(name = "ElementParameters", namespace = "http://www.bpsim.org/schemas/1.0")
    protected List<ElementParameters> elementParameters;
    @XmlElement(name = "Calendar", namespace = "http://www.bpsim.org/schemas/1.0")
    protected List<Calendar> calendar;
    @XmlElement(name = "VendorExtension", namespace = "http://www.bpsim.org/schemas/1.0")
    protected List<VendorExtension> vendorExtension;
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "name")
    protected String name;
    @XmlAttribute(name = "description")
    protected String description;
    @XmlAttribute(name = "author")
    protected String author;
    @XmlAttribute(name = "vendor")
    protected String vendor;
    @XmlAttribute(name = "version")
    protected String version;
    @XmlAttribute(name = "inherits")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object inherits;
    @XmlAttribute(name = "result")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object result;
    @XmlAttribute(name = "created")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar created;
    @XmlAttribute(name = "modified")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar modified;

    /**
     * Gets the value of the scenarioParameters property.
     * 
     * @return
     *     possible object is
     *     {@link ScenarioParameters }
     *     
     */
    public ScenarioParameters getScenarioParameters() {
        return scenarioParameters;
    }

    /**
     * Sets the value of the scenarioParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link ScenarioParameters }
     *     
     */
    public void setScenarioParameters(ScenarioParameters value) {
        this.scenarioParameters = value;
    }

    /**
     * Gets the value of the elementParameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elementParameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElementParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ElementParameters }
     * 
     * 
     */
    public List<ElementParameters> getElementParameters() {
        if (elementParameters == null) {
            elementParameters = new ArrayList<ElementParameters>();
        }
        return this.elementParameters;
    }

    /**
     * Gets the value of the calendar property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the calendar property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCalendar().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Calendar }
     * 
     * 
     */
    public List<Calendar> getCalendar() {
        if (calendar == null) {
            calendar = new ArrayList<Calendar>();
        }
        return this.calendar;
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
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the author property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the value of the author property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthor(String value) {
        this.author = value;
    }

    /**
     * Gets the value of the vendor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * Sets the value of the vendor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendor(String value) {
        this.vendor = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the inherits property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getInherits() {
        return inherits;
    }

    /**
     * Sets the value of the inherits property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setInherits(Object value) {
        this.inherits = value;
    }

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setResult(Object value) {
        this.result = value;
    }

    /**
     * Gets the value of the created property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreated() {
        return created;
    }

    /**
     * Sets the value of the created property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreated(XMLGregorianCalendar value) {
        this.created = value;
    }

    /**
     * Gets the value of the modified property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getModified() {
        return modified;
    }

    /**
     * Sets the value of the modified property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setModified(XMLGregorianCalendar value) {
        this.modified = value;
    }

}
