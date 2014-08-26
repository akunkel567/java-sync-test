/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.complex.jclxsync.soap;

import de.complex.exception.ExceptionHelper;
import org.apache.axis.AxisFault;

/**
 *
 * @author kunkel
 */
public class AxisHelper {

	public static String getAxisFaultString(AxisFault af) {
      return "AxisFault" + " - getFaultCode (LocalPart): " + af.getFaultCode().getLocalPart() +
				  " - getFaultCode (Prefix): " + af.getFaultCode().getPrefix() +
				  " - getFaultString: " + af.getFaultString() +
		        " - getMessage: " + af.getMessage() +
				  " - StackTrace: " + ExceptionHelper.ExceptionToString(af);
   }

}
