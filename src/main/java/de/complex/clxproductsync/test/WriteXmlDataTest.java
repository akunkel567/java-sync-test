/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.test;

import de.complex.exception.ExceptionHelper;
import de.complex.clxproductsync.xml.XmlCol;
import de.complex.clxproductsync.xml.XmlRow;
import de.complex.clxproductsync.xml.XmlData;
import de.complex.clxproductsync.xml.XmlTable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

//import org.apache.xml.serialize.OutputFormat;
//import org.apache.xml.serialize.XMLSerializer;
/**
 *
 * @author kunkel
 */
public class WriteXmlDataTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new WriteXmlDataTest();
    }

    public WriteXmlDataTest() {

//		System.out.println("running...!\n");
//
//		XmlTable tbl = new XmlTable();
//		tbl.setName("ak");
//		tbl.setPkName("akid");
//
//		XmlRow ak = new XmlRow();
//		ak.setPkValue("4711");
//		ak.addCol(new XmlCol("akid", "4711"));
//		ak.addCol(new XmlCol("adaid", "123"));
//		ak.addCol(new XmlCol("datum", "2009-01-30"));
//		tbl.addRow(ak);
//
//		XmlTable tblAz = new XmlTable();
//		tblAz.setName("az");
//		tblAz.setPkName("azid");
//		XmlRow az = new XmlRow();
//		az.setPkValue("1234");
//		az.addCol(new XmlCol("azid", "1234"));
//		az.addCol(new XmlCol("artid", "7777"));
//		az.addCol(new XmlCol("menge", "1"));
//
//		XmlTable tblArt = new XmlTable();
//		tblArt.setName("art");
//		tblArt.setPkName("artid");
//		XmlRow art = new XmlRow();
//		art.setPkValue("7777");
//		art.addCol(new XmlCol("artid", "7777"));
//		art.addCol(new XmlCol("name", "ST 7050"));
//		art.addCol(new XmlCol("hersteller", "Grundig"));
//		tblArt.addRow(art);
//		az.addSubTable(tblArt);
//
//		tblAz.addRow(az);
//
//		ak.addSubTable(tblAz);
//
//		XmlData xmlData = new XmlData();
//		xmlData.setTable(tbl);
//
//
//		String xml = null;
//		Writer w = null;
//
//		try {
//			JAXBContext context = JAXBContext.newInstance(XmlData.class);
//			Marshaller m = context.createMarshaller();
//			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//
//			try {
//				XMLSerializer serializer = getXMLSerializer();
//				try {
//					m.marshal(xmlData, serializer.asContentHandler());
//				} catch (IOException ex) {
//					Logger.getLogger(WriteXmlDataTest.class.getName()).log(Level.SEVERE, null, ex);
//				}
//
//
////				w = new StringWriter();
////				m.marshal(xmlData, w);
////				System.out.println(w.toString());
////				xml = w.toString();
//
//				xml = serializer.toString();
//			} finally {
//				try {
//					w.close();
//				} catch (Exception e) {
//				}
//			}
//		} catch (JAXBException ex) {
//			System.out.println(ExceptionHelper.ExceptionToString(ex));
//		}
//
//		Writer fw = null;
//
//		try {
//			try {
//				fw = new FileWriter(new File("bjTest.xml"));
//				fw.write(xml);
//			} finally {
//				fw.close();
//			}
//		} catch (IOException ex) {
//			System.out.println(ExceptionHelper.ExceptionToString(ex));
//		}
    }

//	private static XMLSerializer getXMLSerializer() {
//		// configure an OutputFormat to handle CDATA
//	OutputFormat of = new OutputFormat();
//
//      // specify which of your elements you want to be handled as CDATA.
//      // The use of the '^' between the namespaceURI and the localname
//      // seems to be an implementation detail of the xerces code.
//		// When processing xml that doesn't use namespaces, simply omit the
//		// namespace prefix as shown in the third CDataElement below.
//        of.setCDataElements(
//			    new String[] { "ns1^foo",   // <ns1:foo>
//					   "ns2^bar",   // <ns2:bar>
//					   "^col" });   // <baz>
//
//      // set any other options you'd like
//        of.setPreserveSpace(false);
//        of.setIndenting(true);
//
//      // create the serializer
//        XMLSerializer serializer = new XMLSerializer(of);
//        serializer.setOutputByteStream(System.out);
//
//        return serializer;
//    }
}
