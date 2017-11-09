/**
 * SoapAnswer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */
package de.complex.clxproductsync.soap.axis;

public class SoapAnswer implements java.io.Serializable {

    private int status;

    private java.lang.String msg;

    private int rowsok;

    private de.complex.clxproductsync.soap.axis.Returnnok[] returnnok;

    public SoapAnswer() {
    }

    public SoapAnswer(
            int status,
            java.lang.String msg,
            int rowsok,
            de.complex.clxproductsync.soap.axis.Returnnok[] returnnok) {
        this.status = status;
        this.msg = msg;
        this.rowsok = rowsok;
        this.returnnok = returnnok;
    }

    /**
     * Gets the status value for this SoapAnswer.
     *
     * @return status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status value for this SoapAnswer.
     *
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Gets the msg value for this SoapAnswer.
     *
     * @return msg
     */
    public java.lang.String getMsg() {
        return msg;
    }

    /**
     * Sets the msg value for this SoapAnswer.
     *
     * @param msg
     */
    public void setMsg(java.lang.String msg) {
        this.msg = msg;
    }

    /**
     * Gets the rowsok value for this SoapAnswer.
     *
     * @return rowsok
     */
    public int getRowsok() {
        return rowsok;
    }

    /**
     * Sets the rowsok value for this SoapAnswer.
     *
     * @param rowsok
     */
    public void setRowsok(int rowsok) {
        this.rowsok = rowsok;
    }

    /**
     * Gets the returnnok value for this SoapAnswer.
     *
     * @return returnnok
     */
    public de.complex.clxproductsync.soap.axis.Returnnok[] getReturnnok() {
        return returnnok;
    }

    /**
     * Sets the returnnok value for this SoapAnswer.
     *
     * @param returnnok
     */
    public void setReturnnok(de.complex.clxproductsync.soap.axis.Returnnok[] returnnok) {
        this.returnnok = returnnok;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SoapAnswer)) {
            return false;
        }
        SoapAnswer other = (SoapAnswer) obj;
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true
                && this.status == other.getStatus()
                && ((this.msg == null && other.getMsg() == null)
                || (this.msg != null
                && this.msg.equals(other.getMsg())))
                && this.rowsok == other.getRowsok()
                && ((this.returnnok == null && other.getReturnnok() == null)
                || (this.returnnok != null
                && java.util.Arrays.equals(this.returnnok, other.getReturnnok())));
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
        _hashCode += getStatus();
        if (getMsg() != null) {
            _hashCode += getMsg().hashCode();
        }
        _hashCode += getRowsok();
        if (getReturnnok() != null) {
            for (int i = 0;
                    i < java.lang.reflect.Array.getLength(getReturnnok());
                    i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReturnnok(), i);
                if (obj != null
                        && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc
            = new org.apache.axis.description.TypeDesc(SoapAnswer.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:Transfer", "SoapAnswer"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("msg");
        elemField.setXmlName(new javax.xml.namespace.QName("", "msg"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rowsok");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rowsok"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("returnnok");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Returnnok"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:Transfer", "Returnnok"));
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
        return new org.apache.axis.encoding.ser.BeanSerializer(
                _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
            java.lang.String mechType,
            java.lang.Class _javaType,
            javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanDeserializer(
                _javaType, _xmlType, typeDesc);
    }

}
