/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.daiber.jclxsync.job;

/**
 *
 * @author kunkel
 */
public class CDHBestand {
//colIndex: 1        colName: ARTICLE_SERIAL_NO        colType: 4        colTypeName: int        colClassName: java.lang.Integer
//colIndex: 2        colName: ARTICLE_CODE        colType: -9        colTypeName: nvarchar        colClassName: java.lang.String
//colIndex: 3        colName: DATE_OF_CHANGE        colType: 93        colTypeName: datetime        colClassName: java.sql.Timestamp
//colIndex: 4        colName: ACTUAL_STOCK        colType: 8        colTypeName: float        colClassName: java.lang.Double
//colIndex: 5        colName: PHYSICAL_STOCK        colType: 8        colTypeName: float        colClassName: java.lang.Double
//colIndex: 6        colName: SELLING_PRICE        colType: 8        colTypeName: float        colClassName: java.lang.Double
//colIndex: 7        colName: INDICATOR        colType: -9        colTypeName: nvarchar        colClassName: java.lang.String

    private float actualStock = 0;
    private float reservedStock = 0;

    public CDHBestand(float actualStock, float reservedStock) {
        this.actualStock = actualStock;
        this.reservedStock = reservedStock;
    }

    public float getActualStock() {
        return actualStock;
    }

    public void setActualStock(float actualStock) {
        this.actualStock = actualStock;
    }

    public float getReservedStock() {
        return reservedStock;
    }

    public void setReservedStock(float reservedStock) {
        this.reservedStock = reservedStock;
    }

    public float getAktuellerBestand() {
        return (this.actualStock - this.reservedStock < 0 ? 0 : this.actualStock - this.reservedStock);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(" CdhBestand [");
        builder.append(" actualStock=");
        builder.append(actualStock);
        builder.append(" reservedStock=");
        builder.append(reservedStock);
        builder.append(" ]");
        return builder.toString();
    }

}
