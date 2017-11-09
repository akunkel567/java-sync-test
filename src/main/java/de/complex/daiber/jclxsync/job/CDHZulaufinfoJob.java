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
public class CDHZulaufinfoJob implements org.quartz.StatefulJob {

    private static Logger logger = Logger.getLogger(CDHZulaufinfoJob.class);

    public CDHZulaufinfoJob() {
        CDHZulaufinfoJob.logger.debug(this + " create " + CDHZulaufinfoJob.class.getName());
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            CDHZulaufinfoJob.logger.info("CDHZulaufinfo execute");
            JobDataMap dataMap = context.getJobDetail().getJobDataMap();
            Properties prop = (Properties) dataMap.get("prop");

            new CDHZulaufinfo(prop).start();
            CDHZulaufinfoJob.logger.info("CDHZulaufinfo done");
        } catch (Exception e) {
            CDHZulaufinfoJob.logger.error("Error in CDHZulaufinfo!");
            JobExecutionException e2 = new JobExecutionException(e);
            throw e2;
        }

    }
}
