
package bpmn2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for tRootElement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tRootElement">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.omg.org/spec/BPMN/20100524/MODEL}tBaseElement">
 *       &lt;anyAttribute processContents='lax' namespace='##other'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tRootElement", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL")
@XmlSeeAlso({
    TItemDefinition.class,
    TCategory.class,
    TEndPoint.class,
    TPartnerRole.class,
    TPartnerEntity.class,
    TCollaboration.class,
    TSignal.class,
    TEventDefinition.class,
    TDataStore.class,
    TError.class,
    TResource.class,
    TInterface.class,
    TCorrelationProperty.class,
    TMessage.class,
    TCallableElement.class,
    TEscalation.class
})
public abstract class TRootElement
    extends TBaseElement
{


}
