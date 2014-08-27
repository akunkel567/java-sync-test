/**
 * TableIdList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package de.complex.clxproductsync.soap.tablesync;

public class TableIdList  implements java.io.Serializable {
    private java.lang.String tablename;

    private java.lang.String idList;

    private int size;

    public TableIdList() {
    }

    public TableIdList(
           java.lang.String tablename,
           java.lang.String idList,
           int size) {
           this.tablename = tablename;
           this.idList = idList;
           this.size = size;
    }


    /**
     * Gets the tablename value for this TableIdList.
     * 
     * @return tablename
     */
    public java.lang.String getTablename() {
        return tablename;
    }


    /**
     * Sets the tablename value for this TableIdList.
     * 
     * @param tablename
     */
    public void setTablename(java.lang.String tablename) {
        this.tablename = tablename;
    }


    /**
     * Gets the idList value for this TableIdList.
     * 
     * @return idList
     */
    public java.lang.String getIdList() {
        return idList;
    }


    /**
     * Sets the idList value for this TableIdList.
     * 
     * @param idList
     */
    public void setIdList(java.lang.String idList) {
        this.idList = idList;
    }


    /**
     * Gets the size value for this TableIdList.
     * 
     * @return size
     */
    public int getSize() {
        return size;
    }


    /**
     * Sets the size value for this TableIdList.
     * 
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TableIdList)) return false;
        TableIdList other = (TableIdList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.tablename==null && other.getTablename()==null) || 
             (this.tablename!=null &&
              this.tablename.equals(other.getTablename()))) &&
            ((this.idList==null && other.getIdList()==null) || 
             (this.idList!=null &&
              this.idList.equals(other.getIdList()))) &&
            this.size == other.getSize();
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
        if (getTablename() != null) {
            _hashCode += getTablename().hashCode();
        }
        if (getIdList() != null) {
            _hashCode += getIdList().hashCode();
        }
        _hashCode += getSize();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TableIdList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:Tablesync", "TableIdList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tablename");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tablename"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idList");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("size");
        elemField.setXmlName(new javax.xml.namespace.QName("", "size"));
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
