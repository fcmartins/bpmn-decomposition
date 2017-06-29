
package bpmn2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.bpsim.org/schemas/1.0}ParameterValue">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element ref="{http://www.bpsim.org/schemas/1.0}ParameterValue"/>
 *       &lt;/sequence>
 *     &lt;/extension>
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
public class EnumParameter
    extends ParameterValue
{

    @XmlElementRef(name = "ParameterValue", namespace = "http://www.bpsim.org/schemas/1.0", type = JAXBElement.class)
    protected List<JAXBElement<? extends ParameterValue>> parameterValue;

    /**
     * Gets the value of the parameterValue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameterValue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameterValue().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link EnumParameter }{@code >}
     * {@link JAXBElement }{@code <}{@link GammaDistribution }{@code >}
     * {@link JAXBElement }{@code <}{@link FloatingParameter }{@code >}
     * {@link JAXBElement }{@code <}{@link WeibullDistribution }{@code >}
     * {@link JAXBElement }{@code <}{@link BinomialDistribution }{@code >}
     * {@link JAXBElement }{@code <}{@link UniformDistribution }{@code >}
     * {@link JAXBElement }{@code <}{@link NegativeExponentialDistribution }{@code >}
     * {@link JAXBElement }{@code <}{@link NumericParameter }{@code >}
     * {@link JAXBElement }{@code <}{@link DateTimeParameter }{@code >}
     * {@link JAXBElement }{@code <}{@link NormalDistribution }{@code >}
     * {@link JAXBElement }{@code <}{@link BetaDistribution }{@code >}
     * {@link JAXBElement }{@code <}{@link LogNormalDistribution }{@code >}
     * {@link JAXBElement }{@code <}{@link PoissonDistribution }{@code >}
     * {@link JAXBElement }{@code <}{@link TriangularDistribution }{@code >}
     * {@link JAXBElement }{@code <}{@link StringParameter }{@code >}
     * {@link JAXBElement }{@code <}{@link UserDistribution }{@code >}
     * {@link JAXBElement }{@code <}{@link ParameterValue }{@code >}
     * {@link JAXBElement }{@code <}{@link BooleanParameter }{@code >}
     * {@link JAXBElement }{@code <}{@link ExpressionParameter }{@code >}
     * {@link JAXBElement }{@code <}{@link ErlangDistribution }{@code >}
     * {@link JAXBElement }{@code <}{@link TruncatedNormalDistribution }{@code >}
     * {@link JAXBElement }{@code <}{@link DurationParameter }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends ParameterValue>> getParameterValue() {
        if (parameterValue == null) {
            parameterValue = new ArrayList<JAXBElement<? extends ParameterValue>>();
        }
        return this.parameterValue;
    }

}
