package com.potalab.exam.jmx.standardagent;

import javax.management.*;

public class StandardAgentMain {

    public static void main(String[] args) {
        StandardAgent agent = new StandardAgent();
        MBeanServer mBeanServer = agent.getMBeanServer();
        String mBeanFullClassName = "com.potalab.exam.jmx.notification.Hello";
        ObjectName objectName
                = agent.createObjectName( "com.potalab.exam.jmx.standardagent:type=Hello");
        agent.createStandardMBean(mBeanFullClassName, objectName);

        try {
            mBeanServer.invoke(objectName, "startProcess", null, null);
        } catch (InstanceNotFoundException | MBeanException | ReflectionException e) {
            e.printStackTrace();
        }
    }

}
