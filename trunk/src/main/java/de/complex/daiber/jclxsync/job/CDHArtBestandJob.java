/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.daiber.jclxsync.job;

import java.util.Properties;
import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author kunkel
 */
public class CDHArtBestandJob implements org.quartz.StatefulJob {

    private static Logger logger = Logger.getLogger(CDHArtBestandJob.class);

    public CDHArtBestandJob() {
        CDHArtBestandJob.logger.debug(this + " create " + CDHArtBestandJob.class.getName());
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            CDHArtBestandJob.logger.info("CDHArtBestandJob execute");
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            Properties prop = (Properties) dataMap.get("prop");

            new CDHArtBestand(prop).start();
            CDHArtBestandJob.logger.info("CDHArtBestandJob done");
        } catch (Exception e) {
            CDHArtBestandJob.logger.error("Error in CDHArtBestandJob!");
            JobExecutionException e2 = new JobExecutionException(e);
            throw e2;
        }

    }
}
