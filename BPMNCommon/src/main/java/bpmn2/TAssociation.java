
package bpmn2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java class for tAssociation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tAssociation">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/spec/BPMN/20100524/MODEL}tArtifact">
 *       &lt;attribute name="sourceRef" use="required" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="targetRef" use="required" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;attribute name="associationDirection" type="{http://www.omg.org/spec/BPMN/20100524/MODEL}tAssociationDirection" default="None" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tAssociation", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class TAssociation
    extends TArtifact
{

    @XmlAttribute(name = "sourceRef", required = true)
    protected QName sourceRef;
    @XmlAttribute(name = "targetRef", required = true)
    protected QName targetRef;
    @XmlAttribute(name = "associationDirection")
    protected TAssociationDirection associationDirection;

    /**
     * Gets the value of the sourceRef property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getSourceRef() {
        return sourceRef;
    }

    /**
     * Sets the value of the sourceRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setSourceRef(QName value) {
        this.sourceRef = value;
    }

    /**
     * Gets the value of the targetRef property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getTargetRef() {
        return targetRef;
    }

    /**
     * Sets the value of the targetRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setTargetRef(QName value) {
        this.targetRef = value;
    }

    /**
     * Gets the value of the associationDirection property.
     * 
     * @return
     *     possible object is
     *     {@link TAssociationDirection }
     *     
     */
    public TAssociationDirection getAssociationDirection() {
        if (associationDirection == null) {
            return TAssociationDirection.NONE;
        } else {
            return associationDirection;
        }
    }

    /**
     * Sets the value of the associationDirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link TAssociationDirection }
     *     
     */
    public void setAssociationDirection(TAssociationDirection value) {
        this.associationDirection = value;
    }

}
