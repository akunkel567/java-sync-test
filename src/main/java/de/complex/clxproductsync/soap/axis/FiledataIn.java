/**
 * FiledataIn.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.complex.clxproductsync.soap.axis;

public class FiledataIn  implements java.io.Serializable {
    private java.lang.String filename;

    private java.lang.String fremdkz;

    private long fremdid;

    private byte[] content;

    public FiledataIn() {
    }

    public FiledataIn(
           java.lang.String filename,
           java.lang.String fremdkz,
           long fremdid,
           byte[] content) {
           this.filename = filename;
           this.fremdkz = fremdkz;
           this.fremdid = fremdid;
           this.content = content;
    }


    /**
     * Gets the filename value for this FiledataIn.
     * 
     * @return filename
     */
    public java.lang.String getFilename() {
        return filename;
    }


    /**
     * Sets the filename value for this FiledataIn.
     * 
     * @param filename
     */
    public void setFilename(java.lang.String filename) {
        this.filename = filename;
    }


    /**
     * Gets the fremdkz value for this FiledataIn.
     * 
     * @return fremdkz
     */
    public java.lang.String getFremdkz() {
        return fremdkz;
    }


    /**
     * Sets the fremdkz value for this FiledataIn.
     * 
     * @param fremdkz
     */
    public void setFremdkz(java.lang.String fremdkz) {
        this.fremdkz = fremdkz;
    }


    /**
     * Gets the fremdid value for this FiledataIn.
     * 
     * @return fremdid
     */
    public long getFremdid() {
        return fremdid;
    }


    /**
     * Sets the fremdid value for this FiledataIn.
     * 
     * @param fremdid
     */
    public void setFremdid(long fremdid) {
        this.fremdid = fremdid;
    }


    /**
     * Gets the content value for this FiledataIn.
     * 
     * @return content
     */
    public byte[] getContent() {
        return content;
    }


    /**
     * Sets the content value for this FiledataIn.
     * 
     * @param content
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FiledataIn)) return false;
        FiledataIn other = (FiledataIn) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.filename==null && other.getFilename()==null) || 
             (this.filename!=null &&
              this.filename.equals(other.getFilename()))) &&
            ((this.fremdkz==null && other.getFremdkz()==null) || 
             (this.fremdkz!=null &&
              this.fremdkz.equals(other.getFremdkz()))) &&
            this.fremdid == other.getFremdid() &&
            ((this.content==null && other.getContent()==null) || 
             (this.content!=null &&
              java.util.Arrays.equals(this.content, other.getContent())));
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
        if (getFilename() != null) {
            _hashCode += getFilename().hashCode();
        }
        if (getFremdkz() != null) {
            _hashCode += getFremdkz().hashCode();
        }
        _hashCode += new Long(getFremdid()).hashCode();
        if (getContent() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getContent());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getContent(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FiledataIn.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:Transfer", "FiledataIn"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("filename");
        elemField.setXmlName(new javax.xml.namespace.QName("", "filename"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fremdkz");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fremdkz"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fremdid");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fremdid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("content");
        elemField.setXmlName(new javax.xml.namespace.QName("", "content"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"));
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
