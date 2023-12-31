/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.job;

import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 *
 * @author kunkel
 */
public class SpoolcheckUploadJob implements StatefulJob {

    private static Logger logger = LogManager.getLogger(SpoolcheckUploadJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            SpoolcheckUploadJob.logger.debug("SpoolcheckUploadJob execute");
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            Properties prop = (Properties) dataMap.get("prop");

            new SpoolcheckUpload(prop).start();
            SpoolcheckUploadJob.logger.debug("SpoolcheckUploadJob done");
        } catch (Exception e) {
            SpoolcheckUploadJob.logger.error("Error in SpoolcheckUploadJob!");
            JobExecutionException e2 = new JobExecutionException(e);
            throw e2;
        }

    }
}
