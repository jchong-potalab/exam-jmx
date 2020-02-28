package com.potalab.exam.jmx.dynamicmbean;
 
import java.lang.management.*; 
import javax.management.*; 

/**
 * -----	Standard MBean Example	-----
 * -- Creating a JMX Agent to Manage a Resource
 *	리소스는 MBeans에 의해 측정되며 JMX agent에 의해 리소스가 관리되어 진다.
 *	JMX agent의 코어는 MBean server이다. MBean server는 등록된 MBean 들이 관리되는 object server이다.
 *	JMX agent 코어 말고 MBean들을 관리하기 위한 서비스들을 포함하고 있다.
 *	Main은 초기화된 MBean server를 얻음으로서 시작된다.
 *	모든 JMX MBean은 object name을 가져야한다. 이 ObjectName은 JMX 사양을 따라야 한다.
 *	ObjectName은 domain and a list of key-properties 을 가져야한다.
 */
public class Main { 
 
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