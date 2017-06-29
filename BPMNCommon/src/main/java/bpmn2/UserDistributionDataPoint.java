
package bpmn2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
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
 *         &lt;element ref="{http://www.bpsim.org/schemas/1.0}ParameterValue"/>
 *       &lt;/sequence>
 *       &lt;attribute name="probability" type="{http://www.w3.org/2001/XMLSchema}float" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "parameterValue"
})
@XmlRootElement(name = "UserDistributionDataPoint", namespace = "http://www.bpsim.org/schemas/1.0")
public class UserDistributionDataPoint {

    @XmlElementRef(name = "ParameterValue", namespace = "http://www.bpsim.org/schemas/1.0", type = JAXBElement.class)
    protected JAXBElement<? extends ParameterValue> parameterValue;
    @XmlAttribute(name = "probability")
    protected Float probability;

    /**
     * Gets the value of the parameterValue property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EnumParameter }{@code >}
     *     {@link JAXBElement }{@code <}{@link GammaDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link FloatingParameter }{@code >}
     *     {@link JAXBElement }{@code <}{@link WeibullDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link BinomialDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link UniformDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link NegativeExponentialDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link NumericParameter }{@code >}
     *     {@link JAXBElement }{@code <}{@link DateTimeParameter }{@code >}
     *     {@link JAXBElement }{@code <}{@link NormalDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link BetaDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link LogNormalDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link PoissonDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link TriangularDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link StringParameter }{@code >}
     *     {@link JAXBElement }{@code <}{@link UserDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link ParameterValue }{@code >}
     *     {@link JAXBElement }{@code <}{@link BooleanParameter }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionParameter }{@code >}
     *     {@link JAXBElement }{@code <}{@link ErlangDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link TruncatedNormalDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link DurationParameter }{@code >}
     *     
     */
    public JAXBElement<? extends ParameterValue> getParameterValue() {
        return parameterValue;
    }

    /**
     * Sets the value of the parameterValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EnumParameter }{@code >}
     *     {@link JAXBElement }{@code <}{@link GammaDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link FloatingParameter }{@code >}
     *     {@link JAXBElement }{@code <}{@link WeibullDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link BinomialDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link UniformDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link NegativeExponentialDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link NumericParameter }{@code >}
     *     {@link JAXBElement }{@code <}{@link DateTimeParameter }{@code >}
     *     {@link JAXBElement }{@code <}{@link NormalDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link BetaDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link LogNormalDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link PoissonDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link TriangularDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link StringParameter }{@code >}
     *     {@link JAXBElement }{@code <}{@link UserDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link ParameterValue }{@code >}
     *     {@link JAXBElement }{@code <}{@link BooleanParameter }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExpressionParameter }{@code >}
     *     {@link JAXBElement }{@code <}{@link ErlangDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link TruncatedNormalDistribution }{@code >}
     *     {@link JAXBElement }{@code <}{@link DurationParameter }{@code >}
     *     
     */
    public void setParameterValue(JAXBElement<? extends ParameterValue> value) {
        this.parameterValue = value;
    }

    /**
     * Gets the value of the probability property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getProbability() {
        return probability;
    }

    /**
     * Sets the value of the probability property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setProbability(Float value) {
        this.probability = value;
    }

}
