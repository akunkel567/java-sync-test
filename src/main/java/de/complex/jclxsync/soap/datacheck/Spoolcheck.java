/**
 * Spoolcheck.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.complex.jclxsync.soap.datacheck;

public class Spoolcheck  implements java.io.Serializable {
    private java.lang.String name;

    private int anzahl;

    private java.lang.String sadt;

    private java.lang.String now;

    private int idsum;

    private int id;

    public Spoolcheck() {
    }

    public Spoolcheck(
           java.lang.String name,
           int anzahl,
           java.lang.String sadt,
           java.lang.String now,
           int idsum,
           int id) {
           this.name = name;
           this.anzahl = anzahl;
           this.sadt = sadt;
           this.now = now;
           this.idsum = idsum;
           this.id = id;
    }


    /**
     * Gets the name value for this Spoolcheck.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this Spoolcheck.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the anzahl value for this Spoolcheck.
     * 
     * @return anzahl
     */
    public int getAnzahl() {
        return anzahl;
    }


    /**
     * Sets the anzahl value for this Spoolcheck.
     * 
     * @param anzahl
     */
    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }


    /**
     * Gets the sadt value for this Spoolcheck.
     * 
     * @return sadt
     */
    public java.lang.String getSadt() {
        return sadt;
    }


    /**
     * Sets the sadt value for this Spoolcheck.
     * 
     * @param sadt
     */
    public void setSadt(java.lang.String sadt) {
        this.sadt = sadt;
    }


    /**
     * Gets the now value for this Spoolcheck.
     * 
     * @return now
     */
    public java.lang.String getNow() {
        return now;
    }


    /**
     * Sets the now value for this Spoolcheck.
     * 
     * @param now
     */
    public void setNow(java.lang.String now) {
        this.now = now;
    }


    /**
     * Gets the idsum value for this Spoolcheck.
     * 
     * @return idsum
     */
    public int getIdsum() {
        return idsum;
    }


    /**
     * Sets the idsum value for this Spoolcheck.
     * 
     * @param idsum
     */
    public void setIdsum(int idsum) {
        this.idsum = idsum;
    }


    /**
     * Gets the id value for this Spoolcheck.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }


    /**
     * Sets the id value for this Spoolcheck.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Spoolcheck)) return false;
        Spoolcheck other = (Spoolcheck) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            this.anzahl == other.getAnzahl() &&
            ((this.sadt==null && other.getSadt()==null) || 
             (this.sadt!=null &&
              this.sadt.equals(other.getSadt()))) &&
            ((this.now==null && other.getNow()==null) || 
             (this.now!=null &&
              this.now.equals(other.getNow()))) &&
            this.idsum == other.getIdsum() &&
            this.id == other.getId();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        _hashCode += getAnzahl();
        if (getSadt() != null) {
            _hashCode += getSadt().hashCode();
        }
        if (getNow() != null) {
            _hashCode += getNow().hashCode();
        }
        _hashCode += getIdsum();
        _hashCode += getId();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Spoolcheck.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:Datacheck", "Spoolcheck"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("anzahl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "anzahl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sadt");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sadt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("now");
        elemField.setXmlName(new javax.xml.namespace.QName("", "now"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idsum");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idsum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
