
package bpmn2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;


/**
 * <p>Java class for tErrorEventDefinition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tErrorEventDefinition">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/spec/BPMN/20100524/MODEL}tEventDefinition">
 *       &lt;attribute name="errorRef" type="{http://www.w3.org/2001/XMLSchema}QName" />
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tErrorEventDefinition", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
public class TErrorEventDefinition
    extends TEventDefinition
{

    @XmlAttribute(name = "errorRef")
    protected QName errorRef;

    /**
     * Gets the value of the errorRef property.
     * 
     * @return
     *     possible object is
     *     {@link QName }
     *     
     */
    public QName getErrorRef() {
        return errorRef;
    }

    /**
     * Sets the value of the errorRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link QName }
     *     
     */
    public void setErrorRef(QName value) {
        this.errorRef = value;
    }

}
