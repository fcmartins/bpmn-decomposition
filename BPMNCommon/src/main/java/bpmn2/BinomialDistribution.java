
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
 *       &lt;attribute name="probability" type="{http://www.w3.org/2001/XMLSchema}double" />
 *       &lt;attribute name="trials" type="{http://www.w3.org/2001/XMLSchema}long" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
public class BinomialDistribution
    extends DistributionParameter
{

    @XmlAttribute(name = "probability")
    protected Double probability;
    @XmlAttribute(name = "trials")
    protected Long trials;

    /**
     * Gets the value of the probability property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getProbability() {
        return probability;
    }

    /**
     * Sets the value of the probability property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setProbability(Double value) {
        this.probability = value;
    }

    /**
     * Gets the value of the trials property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTrials() {
        return trials;
    }

    /**
     * Sets the value of the trials property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTrials(Long value) {
        this.trials = value;
    }

}
