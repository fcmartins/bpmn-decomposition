
package bpmn2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DistributionParameter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DistributionParameter">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.bpsim.org/schemas/1.0}ParameterValue">
 *       &lt;attribute name="timeUnit" type="{http://www.bpsim.org/schemas/1.0}TimeUnit" />
 *       &lt;attribute name="currencyUnit" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DistributionParameter", namespace = "http://www.bpsim.org/schemas/1.0")
@XmlSeeAlso({
    UserDistribution.class,
    TruncatedNormalDistribution.class,
    NegativeExponentialDistribution.class,
    BetaDistribution.class,
    WeibullDistribution.class,
    GammaDistribution.class,
    LogNormalDistribution.class,
    PoissonDistribution.class,
    ErlangDistribution.class,
    BinomialDistribution.class,
    UniformDistribution.class,
    NormalDistribution.class,
    TriangularDistribution.class
})
public class DistributionParameter
    extends ParameterValue
{

    @XmlAttribute(name = "timeUnit")
    protected TimeUnit timeUnit;
    @XmlAttribute(name = "currencyUnit")
    protected String currencyUnit;

    /**
     * Gets the value of the timeUnit property.
     * 
     * @return
     *     possible object is
     *     {@link TimeUnit }
     *     
     */
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    /**
     * Sets the value of the timeUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeUnit }
     *     
     */
    public void setTimeUnit(TimeUnit value) {
        this.timeUnit = value;
    }

    /**
     * Gets the value of the currencyUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyUnit() {
        return currencyUnit;
    }

    /**
     * Sets the value of the currencyUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyUnit(String value) {
        this.currencyUnit = value;
    }

}
