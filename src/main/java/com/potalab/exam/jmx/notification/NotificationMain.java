package com.potalab.exam.jmx.notification;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class NotificationMain {

  public static void main(String[] args)
      throws Exception {

    MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    ObjectName name = new ObjectName("com.potalab.exam.jmx.notification:type=Hello");
    Hello mbean = new Hello();
    mbs.registerMBean(mbean, name);
    mbean.startProcess();
  }
}
