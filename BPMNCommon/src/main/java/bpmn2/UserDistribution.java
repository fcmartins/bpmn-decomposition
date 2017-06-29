
package bpmn2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.bpsim.org/schemas/1.0}DistributionParameter">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element ref="{http://www.bpsim.org/schemas/1.0}UserDistributionDataPoint"/>
 *       &lt;/sequence>
 *       &lt;attribute name="discrete" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "userDistributionDataPoint"
})
public class UserDistribution
    extends DistributionParameter
{

    @XmlElement(name = "UserDistributionDataPoint", namespace = "http://www.bpsim.org/schemas/1.0", required = true)
    protected List<UserDistributionDataPoint> userDistributionDataPoint;
    @XmlAttribute(name = "discrete")
    protected Boolean discrete;

    /**
     * Gets the value of the userDistributionDataPoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the userDistributionDataPoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserDistributionDataPoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserDistributionDataPoint }
     * 
     * 
     */
    public List<UserDistributionDataPoint> getUserDistributionDataPoint() {
        if (userDistributionDataPoint == null) {
            userDistributionDataPoint = new ArrayList<UserDistributionDataPoint>();
        }
        return this.userDistributionDataPoint;
    }

    /**
     * Gets the value of the discrete property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDiscrete() {
        if (discrete == null) {
            return false;
        } else {
            return discrete;
        }
    }

    /**
     * Sets the value of the discrete property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDiscrete(Boolean value) {
        this.discrete = value;
    }

}
