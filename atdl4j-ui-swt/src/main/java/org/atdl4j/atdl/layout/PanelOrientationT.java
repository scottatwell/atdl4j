//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.02.23 at 01:35:41 �ߌ� JST 
//


package org.atdl4j.atdl.layout;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PanelOrientation_t.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PanelOrientation_t">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="HORIZONTAL"/>
 *     &lt;enumeration value="VERTICAL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PanelOrientation_t")
@XmlEnum
public enum PanelOrientationT {

    HORIZONTAL,
    VERTICAL;

    public String value() {
        return name();
    }

    public static PanelOrientationT fromValue(String v) {
        return valueOf(v);
    }

}
