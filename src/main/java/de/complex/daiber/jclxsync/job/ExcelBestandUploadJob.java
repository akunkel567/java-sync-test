/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.daiber.jclxsync.job;

import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author kunkel
 */
public class ExcelBestandUploadJob implements org.quartz.StatefulJob {

    private static Logger logger = LogManager.getLogger(ExcelBestandUploadJob.class);

    public ExcelBestandUploadJob() {
        ExcelBestandUploadJob.logger.debug(this + " create " + ExcelBestandUploadJob.class.getName());
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            ExcelBestandUploadJob.logger.info("ExcelBestandUplaodJob execute");
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            Properties prop = (Properties) dataMap.get("prop");

            new ExcelBestandUpload(prop).start();
            ExcelBestandUploadJob.logger.info("ExcelBestandUplaodJob done");
        } catch (Exception e) {
            ExcelBestandUploadJob.logger.error("Error in ExcelBestandUplaodJob!");
            JobExecutionException e2 = new JobExecutionException(e);
            throw e2;
        }

    }
}
