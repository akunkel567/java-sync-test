/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.test;

import de.complex.exception.ExceptionHelper;
import de.complex.clxproductsync.xml.XmlCol;
import de.complex.clxproductsync.xml.XmlData;
import de.complex.clxproductsync.xml.XmlRow;
import de.complex.clxproductsync.xml.XmlTable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author kunkel
 */
public class ReadXmlDataTest {

    public static int i = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        new ReadXmlDataTest();
    }

    public ReadXmlDataTest() {
        System.out.println("running...!");

        XmlData xmlData = null;

        try {
            JAXBContext context = null;
            context = JAXBContext.newInstance(XmlData.class);

            Unmarshaller um = context.createUnmarshaller();

            xmlData = (XmlData) um.unmarshal(new FileReader("bjTest.xml"));
        } catch (JAXBException ex) {
            System.out.println(ExceptionHelper.ExceptionToString(ex));
            System.out.println("geht net...");
        } catch (FileNotFoundException ex) {
            System.out.println(ExceptionHelper.ExceptionToString(ex));
        }

        showXmlData(xmlData);
    }

    private void showXmlData(XmlData xmlData) {
        showTable(xmlData.getTable());
    }

    private void showTable(XmlTable tbl) {
        i++;

        String p = "";

        for (int j = 0; j < i; j++) {
            p += "    ";
        }
        Iterator<XmlRow> rowItr = null;
        Iterator<XmlCol> colItr = null;
        Iterator<XmlTable> subItr = null;
        System.out.println(p + "Table name: " + tbl.getName() + " pkname: " + tbl.getPkName());

        if (!tbl.getRows().isEmpty()) {
            rowItr = tbl.getRows().iterator();

            while (rowItr.hasNext()) {
                XmlRow row = rowItr.next();

                System.out.println(p + "  row pkValue" + row.getPkValue());
                colItr = row.getCol().iterator();
                while (colItr.hasNext()) {
                    XmlCol col = colItr.next();
                    System.out.println(p + "    col name: " + col.getName() + " value :" + col.getValue());
                }

                subItr = row.getSubTables().iterator();
                while (subItr.hasNext()) {
                    XmlTable subTbl = subItr.next();
                    System.out.println("\n" + p + "    Subtable: " + subTbl.getName());
                    showTable(subTbl);
                }
            }
        }

        i--;
    }
}
