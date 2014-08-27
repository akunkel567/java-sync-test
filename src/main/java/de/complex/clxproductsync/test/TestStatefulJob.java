/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.test;

import java.util.Properties;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author kunkel
 */
public class TestStatefulJob implements org.quartz.StatefulJob {

	public TestStatefulJob() {
		System.out.println("Create :" + this + " " + TestStatefulJob.class.getCanonicalName());
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		System.out.println(this + " TestStatefulJob execute");

		try {
			System.out.println(this + " vor sleep");
			Thread.sleep(10 * 1000);
			System.out.println(this + " after sleep");
		} catch (InterruptedException ex) {
			ex.printStackTrace(System.out);
		}
	}
}
