//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.02.23 at 01:35:41 �ߌ� JST 
//


package org.atdl4j.atdl.core;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * Derived parameter type corresponding to the FIX "Char" type defined in the FIX specification.
 * 
 * <p>Java class for Char_t complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Char_t">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.fixprotocol.org/ATDL-1-1/Core}Parameter_t">
 *       &lt;attribute name="constValue" type="{http://www.fixprotocol.org/ATDL-1-1/Core}char" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Char_t")
public class CharT
    extends ParameterT
{

    @XmlAttribute
    protected String constValue;

    /**
     * Gets the value of the constValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConstValue() {
        return constValue;
    }

    /**
     * Sets the value of the constValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConstValue(String value) {
        this.constValue = value;
    }

}
