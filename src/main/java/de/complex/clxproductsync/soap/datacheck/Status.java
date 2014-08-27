/**
 * Status.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.complex.clxproductsync.soap.datacheck;

public class Status  implements java.io.Serializable {
    private java.lang.String sadt;

    private java.lang.String now;

    private int sekunden;

    private int countsnjobs;

    private int countwebspools;

    private int countsnjobwebs;

    public Status() {
    }

    public Status(
           java.lang.String sadt,
           java.lang.String now,
           int sekunden,
           int countsnjobs,
           int countwebspools,
           int countsnjobwebs) {
           this.sadt = sadt;
           this.now = now;
           this.sekunden = sekunden;
           this.countsnjobs = countsnjobs;
           this.countwebspools = countwebspools;
           this.countsnjobwebs = countsnjobwebs;
    }


    /**
     * Gets the sadt value for this Status.
     * 
     * @return sadt
     */
    public java.lang.String getSadt() {
        return sadt;
    }


    /**
     * Sets the sadt value for this Status.
     * 
     * @param sadt
     */
    public void setSadt(java.lang.String sadt) {
        this.sadt = sadt;
    }


    /**
     * Gets the now value for this Status.
     * 
     * @return now
     */
    public java.lang.String getNow() {
        return now;
    }


    /**
     * Sets the now value for this Status.
     * 
     * @param now
     */
    public void setNow(java.lang.String now) {
        this.now = now;
    }


    /**
     * Gets the sekunden value for this Status.
     * 
     * @return sekunden
     */
    public int getSekunden() {
        return sekunden;
    }


    /**
     * Sets the sekunden value for this Status.
     * 
     * @param sekunden
     */
    public void setSekunden(int sekunden) {
        this.sekunden = sekunden;
    }


    /**
     * Gets the countsnjobs value for this Status.
     * 
     * @return countsnjobs
     */
    public int getCountsnjobs() {
        return countsnjobs;
    }


    /**
     * Sets the countsnjobs value for this Status.
     * 
     * @param countsnjobs
     */
    public void setCountsnjobs(int countsnjobs) {
        this.countsnjobs = countsnjobs;
    }


    /**
     * Gets the countwebspools value for this Status.
     * 
     * @return countwebspools
     */
    public int getCountwebspools() {
        return countwebspools;
    }


    /**
     * Sets the countwebspools value for this Status.
     * 
     * @param countwebspools
     */
    public void setCountwebspools(int countwebspools) {
        this.countwebspools = countwebspools;
    }


    /**
     * Gets the countsnjobwebs value for this Status.
     * 
     * @return countsnjobwebs
     */
    public int getCountsnjobwebs() {
        return countsnjobwebs;
    }


    /**
     * Sets the countsnjobwebs value for this Status.
     * 
     * @param countsnjobwebs
     */
    public void setCountsnjobwebs(int countsnjobwebs) {
        this.countsnjobwebs = countsnjobwebs;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Status)) return false;
        Status other = (Status) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.sadt==null && other.getSadt()==null) || 
             (this.sadt!=null &&
              this.sadt.equals(other.getSadt()))) &&
            ((this.now==null && other.getNow()==null) || 
             (this.now!=null &&
              this.now.equals(other.getNow()))) &&
            this.sekunden == other.getSekunden() &&
            this.countsnjobs == other.getCountsnjobs() &&
            this.countwebspools == other.getCountwebspools() &&
            this.countsnjobwebs == other.getCountsnjobwebs();
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
        if (getSadt() != null) {
            _hashCode += getSadt().hashCode();
        }
        if (getNow() != null) {
            _hashCode += getNow().hashCode();
        }
        _hashCode += getSekunden();
        _hashCode += getCountsnjobs();
        _hashCode += getCountwebspools();
        _hashCode += getCountsnjobwebs();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Status.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:Datacheck", "Status"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("sekunden");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sekunden"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("countsnjobs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "countsnjobs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("countwebspools");
        elemField.setXmlName(new javax.xml.namespace.QName("", "countwebspools"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("countsnjobwebs");
        elemField.setXmlName(new javax.xml.namespace.QName("", "countsnjobwebs"));
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
