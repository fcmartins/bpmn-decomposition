
package bpmn2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute name="mean" type="{http://www.w3.org/2001/XMLSchema}double" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class PoissonDistribution
    extends DistributionParameter
{

    @XmlAttribute(name = "mean")
    protected Double mean;

    /**
     * Gets the value of the mean property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMean() {
        return mean;
    }

    /**
     * Sets the value of the mean property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMean(Double value) {
        this.mean = value;
    }

}