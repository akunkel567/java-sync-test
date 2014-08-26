/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.complex.jclxsync.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 *
 * @author kunkel
 */
public class CliTest {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		
		
		String[] params = new String[]{"-tables","eins","zwei"};
		CommandLine cmdLine = null;
		Options options = new Options();

		Option opt = null;

		opt = OptionBuilder.withDescription("Config-File").hasArg().create("config");
		options.addOption(opt);

		opt = OptionBuilder.withDescription("Check Tables").hasArgs().create("tables");
		options.addOption(opt);

		CommandLineParser parser = new GnuParser();

		StringBuilder usage = new StringBuilder();
		usage.append("java -jar WebTableCheck.jar");

		StringBuilder footer = new StringBuilder();
		footer.append("\n\n");

		try {
			cmdLine = parser.parse(options, params);

			if (params.length == 0) {
				throw new org.apache.commons.cli.ParseException("");
			}
		} catch (org.apache.commons.cli.ParseException ex) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(180, usage.toString(), "", options, footer.toString(), true);

			//ex.printStackTrace(System.err);
			return;
		}
		
		for(String table: cmdLine.getOptionValues("tables")){
		  System.out.println("tables: " + table);
		}

	}
	
}
