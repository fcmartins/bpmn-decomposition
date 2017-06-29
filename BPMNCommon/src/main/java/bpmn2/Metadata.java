
package bpmn2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.jboss.org/drools}metaentry" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "metaentry"
})
@XmlRootElement(name = "metadata", namespace = "http://www.jboss.org/drools")
public class Metadata {

    @XmlElement(namespace = "http://www.jboss.org/drools", required = true)
    protected List<Metaentry> metaentry;

    /**
     * Gets the value of the metaentry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the metaentry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMetaentry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Metaentry }
     * 
     * 
     */
    public List<Metaentry> getMetaentry() {
        if (metaentry == null) {
            metaentry = new ArrayList<Metaentry>();
        }
        return this.metaentry;
    }

}
