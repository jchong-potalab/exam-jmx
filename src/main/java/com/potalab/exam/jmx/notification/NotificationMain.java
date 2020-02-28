package com.potalab.exam.jmx.notification;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class NotificationMain {

  public static void main(String[] args)
      throws Exception {

    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    ObjectName name = new ObjectName("com.potalab.exam.jmx.notification:type=Hello");
    Hello mbean = new Hello();
    mbs.registerMBean(mbean, name);
    ExecutorService es = Executors.newFixedThreadPool(1);
    es.submit(mbean);
    es.shutdown();

    System.out.println("Waiting forever...");

    while (es.isShutdown()
        && es.isTerminated()) {
      Thread.sleep(1000);
    }
  }
}
