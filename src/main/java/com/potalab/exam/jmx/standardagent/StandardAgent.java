package com.potalab.exam.jmx.standardagent;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class StandardAgent {
    private MBeanServer mBeanServer = null;

    public StandardAgent() {
        //
        // MBeanServerFactory.createMBeanServer()를 사용하면 jconsole 에서 보이지 않는다.
        //
        mBeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    public MBeanServer getMBeanServer() {
        return mBeanServer;
    }

    public ObjectName createObjectName(String name) {
        ObjectName objectName = null;
        try {
            objectName = new ObjectName(name);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        return objectName;
    }

    public void createStandardMBean(String mBeanClassFullName, ObjectName objectName) {
        try {
            // Instantiates and registers an MBean in the MBean server.
            mBeanServer.createMBean(mBeanClassFullName, objectName);
        } catch (ReflectionException | InstanceAlreadyExistsException | MBeanException |
                NotCompliantMBeanException e) {
            e.printStackTrace();
        }

    }
}
