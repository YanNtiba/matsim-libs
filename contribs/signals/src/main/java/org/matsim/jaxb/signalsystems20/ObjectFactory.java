//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2011.09.20 at 07:21:49 PM MESZ
//


package org.matsim.jaxb.signalsystems20;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.matsim.jaxb.signalsystems20 package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public final class ObjectFactory {

    private final static QName _Coordinate_QNAME = new QName("https://www.matsim.org/files/dtd", "coordinate");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.matsim.jaxb.signalsystems20
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link XMLSignalType.XMLLane }
     *
     */
    public XMLSignalType.XMLLane createXMLSignalTypeXMLLane() {
        return new XMLSignalType.XMLLane();
    }

    /**
     * Create an instance of {@link XMLActLocationType }
     *
     */
    public XMLActLocationType createXMLActLocationType() {
        return new XMLActLocationType();
    }

    /**
     * Create an instance of {@link XMLSignalType.XMLTurningMoveRestrictions }
     *
     */
    public XMLSignalType.XMLTurningMoveRestrictions createXMLSignalTypeXMLTurningMoveRestrictions() {
        return new XMLSignalType.XMLTurningMoveRestrictions();
    }

    /**
     * Create an instance of {@link XMLLocation }
     *
     */
    public XMLLocation createXMLLocation() {
        return new XMLLocation();
    }

    /**
     * Create an instance of {@link XMLSignalSystemType }
     *
     */
    public XMLSignalSystemType createXMLSignalSystemType() {
        return new XMLSignalSystemType();
    }

    /**
     * Create an instance of {@link XMLSignalSystemType.XMLSignals }
     *
     */
    public XMLSignalSystemType.XMLSignals createXMLSignalSystemTypeXMLSignals() {
        return new XMLSignalSystemType.XMLSignals();
    }

    /**
     * Create an instance of {@link XMLMatsimObjectType }
     *
     */
    public XMLMatsimObjectType createXMLMatsimObjectType() {
        return new XMLMatsimObjectType();
    }

    /**
     * Create an instance of {@link XMLSignalType }
     *
     */
    public XMLSignalType createXMLSignalType() {
        return new XMLSignalType();
    }

    /**
     * Create an instance of {@link XMLLinkId }
     *
     */
    public XMLLinkId createXMLLinkId() {
        return new XMLLinkId();
    }

    /**
     * Create an instance of {@link XMLMatsimParameterType }
     *
     */
    public XMLMatsimParameterType createXMLMatsimParameterType() {
        return new XMLMatsimParameterType();
    }

    /**
     * Create an instance of {@link XMLSignalSystems }
     *
     */
    public XMLSignalSystems createXMLSignalSystems() {
        return new XMLSignalSystems();
    }

    /**
     * Create an instance of {@link XMLMatsimTimeAttributeType }
     *
     */
    public XMLMatsimTimeAttributeType createXMLMatsimTimeAttributeType() {
        return new XMLMatsimTimeAttributeType();
    }

    /**
     * Create an instance of {@link XMLIdRefType }
     *
     */
    public XMLIdRefType createXMLIdRefType() {
        return new XMLIdRefType();
    }

    /**
     * Create an instance of {@link XMLFacilityId }
     *
     */
    public XMLFacilityId createXMLFacilityId() {
        return new XMLFacilityId();
    }

    /**
     * Create an instance of {@link XMLCoordinateType }
     *
     */
    public XMLCoordinateType createXMLCoordinateType() {
        return new XMLCoordinateType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLCoordinateType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "https://www.matsim.org/files/dtd", name = "coordinate")
    public JAXBElement<XMLCoordinateType> createCoordinate(XMLCoordinateType value) {
        return new JAXBElement<XMLCoordinateType>(_Coordinate_QNAME, XMLCoordinateType.class, null, value);
    }

}
