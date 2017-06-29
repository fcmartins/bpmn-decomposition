
package bpmn2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java class for tParticipantAssociation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tParticipantAssociation">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/spec/BPMN/20100524/MODEL}tBaseElement">
 *       &lt;sequence>
 *         &lt;element name="innerParticipantRef" type="{http://www.w3.org/2001/XMLSchema}QName"/>
 *         &lt;element name="outerParticipantRef" type="{http://www.w3.org/2001/XMLSchema}QName"/>
 *       &lt;/sequence>
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tParticipantAssociation", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", propOrder = {
    "innerParticipantRef",
    "outerParticipantRef"
})
public class TParticipantAssociation
    extends TBaseElement
{

    @XmlElement(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", required = true)
    protected QName innerParticipantRef;
    @XmlElement(namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", required = true)
    protected QName outerParticipantRef;

    /**
     * Gets the value of the innerParticipantRef property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getInnerParticipantRef() {
        return innerParticipantRef;
    }

    /**
     * Sets the value of the innerParticipantRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setInnerParticipantRef(QName value) {
        this.innerParticipantRef = value;
    }

    /**
     * Gets the value of the outerParticipantRef property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getOuterParticipantRef() {
        return outerParticipantRef;
    }

    /**
     * Sets the value of the outerParticipantRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setOuterParticipantRef(QName value) {
        this.outerParticipantRef = value;
    }

}
