package com.potalab.exam.jmx.standardagent;

import com.potalab.exam.jmx.notification.HelloMBean;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import com.sun.tools.attach.spi.AttachProvider;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;

/**
 * 이 클래스의 {@link #main(String[])} 함수는 {@link StandardAgentMain} Class 에서 생성하여 무한히
 * 돌고 있는 스레드를 원격에서 중지하는 역할을 한다.
 *
 * 먼저 해당 VM을 구한다. 이를 통해 MBean Server 의 Connection 을 구하고,
 * 해당 MBean 의 ObjectName 을 구하여 MBean Server 의 Connection 을 대상으로
 * {@link HelloMBean#stopProcess()}를 invoke 한다.
 *
 * 테스트를 해보려면 먼저 {@link StandardAgentMain#main(String[])}을 실행시킬 것 그리고 스레드가
 * 무한히 돌고 있는 것을 확인하고 이 클래스의 {@link #main(String[])} 함수를 실행하면 먼저 무한히
 * 돌고 있던 스레드가 중지하면서 프로그램이 종료됨. (둘다)
 *
 * @author jchong
 */
public class AnotherProcess {

    public static void main(String[] args) throws IOException, MBeanException, ReflectionException,
            InstanceNotFoundException, MalformedObjectNameException, AttachNotSupportedException {

        final AttachProvider attachProvider = AttachProvider.providers().get(0);

        VirtualMachineDescriptor descriptor = null;
        for (VirtualMachineDescriptor vmd : attachProvider.listVirtualMachines()) {
            if (pickTargetVirtualMachine(vmd)) {
                descriptor = vmd;
                break;
            }
        }

        if (descriptor == null) {
            throw new RuntimeException("You didn't pick one");
        }

        final VirtualMachine virtualMachine = attachProvider.attachVirtualMachine(descriptor);
        virtualMachine.startLocalManagementAgent();
        final Object portObject = virtualMachine.getAgentProperties().
                get("com.sun.management.jmxremote.localConnectorAddress");

        final JMXServiceURL target = new JMXServiceURL(portObject + "");
        final JMXConnector connector = JMXConnectorFactory.connect(target);
        final MBeanServerConnection remote = connector.getMBeanServerConnection();
        final ObjectName objectName = new ObjectName("com.potalab.exam.jmx.standardagent:type=Hello");
        remote.invoke(objectName, "stopProcess", null, null);
    }

    private static boolean pickTargetVirtualMachine(VirtualMachineDescriptor desc) {
        if (desc.displayName().equals("com.potalab.exam.jmx.standardagent.StandardAgentMain")) {
            return true;
        }
        return false;
    }
}
