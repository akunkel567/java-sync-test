/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.eventhandler.socketevent.xml;

import de.complex.activerecord.ActiveRecord;
import de.complex.activerecord.config.Base64Encoding;
import de.complex.activerecord.config.FileConfig;
import de.complex.activerecord.config.FileNameType;
import de.complex.activerecord.config.Mapping;
import de.complex.clxproductsync.eventhandler.fileevent.FileConvertException;
import de.complex.clxproductsync.eventhandler.fileevent.FileConverter;
import de.complex.tools.config.ApplicationConfig;
import de.complex.tools.xml.XmlHelper;
import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author kunkel
 */
public class XmlConverter {

    private static Logger logger = LogManager.getLogger(XmlConverter.class);

    public XmlConverter() {
    }

    public String fromActiveRecord(ActiveRecord ar) throws SQLException, FileConvertException {
        String colName = null;

        XmlData xData = new XmlData();

        xData.setTable(this.getXmlTable(new ActiveRecord[]{ar}));

        XmlHelper xmlH = new XmlHelper();

        if (logger.isDebugEnabled()) {
            logger.debug("Xml fromActiveRecord: " + xmlH.toXml(xData));
        }

        return xmlH.toXml(xData);
    }

    public String fromActiveRecords(ActiveRecord[] ars) throws SQLException, FileConvertException {
        XmlHelper xmlH = null;
        XmlData xData = null;

        xData = new XmlData();
        xData.setTable(this.getXmlTable(ars));
        xmlH = new XmlHelper();

        if (logger.isDebugEnabled()) {
            logger.debug("Xml fromActiveRecord: " + xmlH.toXml(xData));
        }

        return xmlH.toXml(xData);
    }

    private XmlTable getXmlTable(ActiveRecord[] ar) throws SQLException, FileConvertException {
        XmlTable xTable = new XmlTable();
        XmlRow xRow = null;
        XmlCol xCol = null;
        String colName = null;
        String colNameMap = null;
        Mapping mapping = null;
        Base64Encoding base64encodeing = null;
        List<FileConfig> fileconfigs = null;

        try {
            if (ar.length > 0) {
                String name = ar[0].getConfig().getTableName();

                mapping = ar[0].getConfig().getMapping();

                if (mapping != null) {
                    if (!mapping.getMappingname().equals("")) {
                        xTable.setName(mapping.getMappingname());
                    } else {
                        xTable.setName(ar[0].getConfig().getTableName());
                    }

                    if (!mapping.getPrimaryKeyName().equals("")) {
                        xTable.setPkName(mapping.getPrimaryKeyName());
                    } else {
                        xTable.setPkName(ar[0].getConfig().getPrimaryKeyName());
                    }
                } else {
                    xTable.setName(ar[0].getConfig().getTableName());
                    xTable.setPkName(ar[0].getConfig().getPrimaryKeyName());
                }

                base64encodeing = ar[0].getConfig().getBase64encoding();
                fileconfigs = ar[0].getConfig().getFiles();

                for (int i = 0; i < ar.length; i++) {
                    if (!ar[i].rowData.isEmpty()) {
                        xRow = new XmlRow(ar[i].rowData.get(ar[i].getConfig().getPrimaryKeyName()));

                        Iterator<String> itr = ar[i].rowData.keySet().iterator();
                        while (itr.hasNext()) {
                            colName = itr.next().toLowerCase();
                            colNameMap = colName;

                            if ((mapping != null) && (mapping.hasFieldMapping(colName))) {
                                colNameMap = mapping.getFieldMapping(colName).getMappingname().toLowerCase();
                            }

                            if ((base64encodeing != null) && (base64encodeing.hasEntry(colName))) {
                                // Base64 codiert
                                xCol = new XmlCol(colNameMap, (ar[i].rowData.get(colName) == null ? "" : new String(Base64.encodeBase64(ar[i].rowData.get(colName).getBytes()))));
                                xCol.setIsBase64(true);
                                xRow.addCol(xCol);
                            } else {
                                xCol = new XmlCol(colNameMap, ar[i].rowData.get(colName) == null ? "" : ar[i].rowData.get(colName));
                                xCol.setIsBase64(false);
                                xRow.addCol(xCol);
                            }
                        }
                    }

                    if (ar[i].hasDetails()) {
                        Iterator<String> itr = ar[i].getDetailNames().iterator();
                        while (itr.hasNext()) {
                            xRow.addSubTable(this.getXmlTable(ar[i].getDetail(itr.next())));
                        }
                    }

                    // Files
                    for (FileConfig fc : fileconfigs) {
                        if (XmlConverter.logger.isDebugEnabled()) {
                            XmlConverter.logger.debug(fc.toString());
                        }

                        File filePath = null;

                        // Type FREE oder leer
                        if (fc.getFilenametype().equalsIgnoreCase(FileNameType.FREE)) {

                            if (ApplicationConfig.hasValue("filesPrePath")) {
                                filePath = new File(ApplicationConfig.getValue("filesPrePath", "") + "/" + fc.getPath() + "/" + xRow.getPkValue());
                            } else {
                                filePath = new File(fc.getPath() + "/" + xRow.getPkValue());
                            }

                            Iterator<File> fileItr = FileUtils.iterateFiles(filePath, new String[]{fc.getExtension()}, false);

                            if (XmlConverter.logger.isDebugEnabled()) {
                                XmlConverter.logger.debug("File Path: " + filePath.getAbsolutePath());
                            }

                            while (fileItr.hasNext()) {
                                File file = fileItr.next();

                                if (file.canRead()) {
                                    xRow.addFile(new XmlFile(file.getName(), FileConverter.toBase64String(file)));
                                } else {
                                    XmlConverter.logger.error("File: " + file.getAbsolutePath() + " kann nicht gelesen werden...!");
                                }
                            }
                        }

                        // Type PK
                        if (fc.getFilenametype().equalsIgnoreCase(FileNameType.PK)) {
                            File file = null;

                            if (ApplicationConfig.hasValue("filesPrePath")) {
                                file = new File(ApplicationConfig.getValue("filesPrePath", "") + "/" + fc.getPath() + "/" + xRow.getPkValue() + "/" + xRow.getPkValue() + "." + fc.getExtension());
                            } else {
                                file = new File(fc.getPath() + "/" + xRow.getPkValue() + "/" + xRow.getPkValue() + "." + fc.getExtension());
                            }

                            if (XmlConverter.logger.isDebugEnabled()) {
                                XmlConverter.logger.debug("File Path: " + file.getAbsolutePath());
                            }
                            if (file.canRead()) {
                                xRow.addFile(new XmlFile(file.getName(), FileConverter.toBase64String(file)));
                            } else {
                                XmlConverter.logger.error("File: " + file.getAbsolutePath() + " kann nicht gelesen werden...!");
                            }
                        }
                    }
                    xTable.addRow(xRow);
                }
            }

            return xTable;
        } finally {
            xTable = null;
            xRow = null;
            xCol = null;
            mapping = null;
            colName = null;
            colNameMap = null;
            mapping = null;
            base64encodeing = null;
        }
    }

    public String fromBlankActiveRecord(ActiveRecord ar, long id) {
        String colName = null;
        Mapping mapping = null;

        XmlData xData = new XmlData();
        XmlTable xTable = new XmlTable();
        XmlRow xRow = null;
        XmlCol xCol = null;

        mapping = ar.getConfig().getMapping();

        if (mapping != null) {
            if (!mapping.getMappingname().equals("")) {
                xTable.setName(mapping.getMappingname());
            } else {
                xTable.setName(ar.getConfig().getTableName());
            }

            if (!mapping.getPrimaryKeyName().equals("")) {
                xTable.setPkName(mapping.getPrimaryKeyName());
            } else {
                xTable.setPkName(ar.getConfig().getPrimaryKeyName());
            }
        } else {
            xTable.setName(ar.getConfig().getTableName());
            xTable.setPkName(ar.getConfig().getPrimaryKeyName());
        }

        xRow = new XmlRow(Long.toString(id));

        xTable.addRow(xRow);
        xData.setTable(xTable);

        XmlHelper xmlH = new XmlHelper();

        if (logger.isDebugEnabled()) {
            logger.debug("Xml fromBlankActiveRecord: " + xmlH.toXml(xData));
        }

        return xmlH.toXml(xData);
    }

    public XmlData xmlDataFromXml(String xml) {
        XmlConverter.logger.debug(xml);

        return (XmlData) new XmlHelper().factory(xml, XmlData.class);
    }
}
