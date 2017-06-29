
package bpmn2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Parameter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Parameter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;element name="ResultRequest" type="{http://www.bpsim.org/schemas/1.0}ResultType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.bpsim.org/schemas/1.0}ParameterValue" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="kpi" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="sla" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Parameter", namespace = "http://www.bpsim.org/schemas/1.0", propOrder = {
    "resultRequest",
    "parameterValue"
})
@XmlSeeAlso({
    bpmn2.PropertyParameters.Property.class
})
public class Parameter {

    @XmlElement(name = "ResultRequest", namespace = "http://www.bpsim.org/schemas/1.0")
    protected List<ResultType> resultRequest;
    @XmlElementRef(name = "ParameterValue", namespace = "http://www.bpsim.org/schemas/1.0", type = JAXBElement.class, required = false)
    protected List<JAXBElement<? extends ParameterValue>> parameterValue;
    @XmlAttribute(name = "kpi")
    protected Boolean kpi;
    @XmlAttribute(name = "sla")
    protected Boolean sla;

    /**
     * Gets the value of the resultRequest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the resultRequest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getResultRequest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResultType }
     * 
     * 
     */
    public List<ResultType> getResultRequest() {
        if (resultRequest == null) {
            resultRequest = new ArrayList<ResultType>();
        }
        return this.resultRequest;
    }

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

    /**
     * Gets the value of the kpi property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isKpi() {
        if (kpi == null) {
            return false;
        } else {
            return kpi;
        }
    }

    /**
     * Sets the value of the kpi property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setKpi(Boolean value) {
        this.kpi = value;
    }

    /**
     * Gets the value of the sla property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isSla() {
        if (sla == null) {
            return false;
        } else {
            return sla;
        }
    }

    /**
     * Sets the value of the sla property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSla(Boolean value) {
        this.sla = value;
    }

}
