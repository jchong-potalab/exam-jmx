package com.potalab.exam.jmx.dynamicmbean;
 
import java.lang.management.*; 
import javax.management.*; 

public class DynamicMBeanMain {
 
    public static void main(String[] args) 
        throws Exception { 
     
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
        ObjectName name = new ObjectName("com.potalab.exam.jmx.dynamicmbean:type=HelloX");
        HelloX mbean = new HelloX();
        mbs.registerMBean(mbean, name); 
          
        System.out.println("Waiting forever..."); 
        Thread.sleep(Long.MAX_VALUE); 
    } 
} 