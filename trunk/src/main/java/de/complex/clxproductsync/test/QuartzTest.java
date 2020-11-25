/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.complex.clxproductsync.test;

import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 *
 * @author kunkel
 */
public class QuartzTest extends Thread {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        new QuartzTest().init();
    }

    private void init() {

        this.start(); // und jetzt in den run -Mode

        try {
            Thread.sleep(250);
        } catch (InterruptedException ex) {
            // nix
        }

        return;
    }

    @Override
    public void run() {
        SchedulerFactory sf = null;
        Scheduler sched = null;
        try {
            Properties p = new Properties();
            p.setProperty("org.quartz.scheduler.instanceName", "QuartzScheduler");
            p.setProperty("org.quartz.scheduler.instanceId", "1");
            p.setProperty("org.quartz.scheduler.rmi.export", "false");
            p.setProperty("org.quartz.scheduler.rmi.proxy", "false");
            p.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
            p.setProperty("org.quartz.threadPool.threadCount", "10");
            p.setProperty("org.quartz.threadPool.threadPriority", "5");
            p.setProperty("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");

            sf = new StdSchedulerFactory(p);
            sched = sf.getScheduler();

            CronTrigger trigger = null;
            JobDetail job = null;
            Date dt = null;

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("prop", p);

            // CDHBestand job
            job = newJob(TestStatefulJob.class).withIdentity("cdhartbestand_job", "group").usingJobData(jobDataMap).build();
            trigger = newTrigger().forJob(job).withIdentity("cdhbestand_trigger", "group").withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?")).build();

            dt = sched.scheduleJob(job, trigger);
            System.out.println("TestStatefulJob :" + dt.toString());

            sched.start();

        } catch (SchedulerException ex) {
            System.err.println("FatalError:" + ex.getMessage());
            ex.printStackTrace(System.err);
        }

        // Hauptschleife.... Todo Überwachung der EventManager....
        while (!this.isInterrupted()) {
            try {

                Trigger t = null;
                try {
                    t = sched.getTrigger(new TriggerKey("cdhbestand_trigger", "group"));
                    if (t != null) {
                        System.out.println(t.getKey() + " naechster Start: " + t.getNextFireTime());
                    } else {
                        System.out.println(" kein cdhbestand_trigger aktiv");
                    }
                } catch (SchedulerException ex) {
                    ex.printStackTrace(System.err);
                }

                yield();

                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    interrupt();
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }
}
