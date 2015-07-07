/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.test;

import de.complex.util.datetime.DateTool;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

/**
 *
 * @author kunkel
 */
public class DivTest {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws IOException {
		// TODO code application logic here
		
//		double i = 1234567890.45;
//		
//		
//		DecimalFormat df = new DecimalFormat("########0");
//		
//		System.out.println("" + df.format(i));

		
		Date d = DateTool.simpleParse("2015-07-07 14:34:19.999", "yyyy-MM-dd HH:mm:ss.SSS");
		
		System.out.println("date: " + d);
		
		//2015-07-07 14:34:19.31
		
	
			Calendar cal = new GregorianCalendar();
			cal.setTime(d);
			cal.add(Calendar.MINUTE, +1);
		
		System.out.println("date: " + cal.getTime());
		
		System.exit(0);
		
		
		
    ICsvMapWriter writer = new CsvMapWriter(new FileWriter("testfile2.csv"), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
    try {
      final String[] header = new String[] { "name", "city", "zip" };
      // set up some data to write
      
		final HashMap<String, ? super Object> data1 = new HashMap<String, Object>();
      data1.put(header[0], "Ka;rl");
      data1.put(header[1], "Tent city");
      data1.put(header[2], 5565);
      final HashMap<String, ? super Object> data2 = new HashMap<String, Object>();
      data2.put(header[0], "Banjo");
      data2.put(header[1], "River\n side");
      data2.put(header[2], 5551);
      // the actual writing
      writer.writeHeader(header);
      writer.write(data1, header);
      writer.write(data2, header);
    } finally {
      writer.close();
    }
  }
}
	

