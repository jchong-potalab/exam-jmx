package com.potalab.exam.jmx.modelermbean;

import org.apache.commons.modeler.Registry;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;

public class ModelerMBeanMain {
    /**
     * Main functionality for using Modeler XML metadata to instantiate and
     * register a Model MBean with the Platform MBean Server.
     */
    public void applyCommonsModeler()
    {
        final String modelerMetadataFile = "simple-calculator-modeler.xml";
        final SimpleCalculator calculator = new SimpleCalculator();
        Registry registry = null;
        final InputStream modelerXmlInputStream =
                ModelerMBeanMain.class.getResourceAsStream(
                        modelerMetadataFile);

        // Use getRegistry(Object,Object) rather than deprecated getRegistry()
        registry = Registry.getRegistry(null, null);

        // Use instance setMBeanServer method rather than class/static setServer
        registry.setMBeanServer(ManagementFactory.getPlatformMBeanServer());
        try
        {
            // The following two methods on Registry (loadMetadata and
            // registerComponent) throw the checked and very general Exception,
            // which must be captured here..
            registry.loadMetadata(modelerXmlInputStream);
            registry.registerComponent(
                    calculator,
                    "modelmbean:type=commons-modeler",  // mbean registered object name
                    "dustin.jmx.modelmbeans.SimpleCalculator");
        }
        catch (IOException ioEx)
        {
            System.err.println(
                    "ERROR trying to load metadata into Commons Modeler Registry "
                            + "from configuration file " + modelerMetadataFile + ":\n"
                            + ioEx.getMessage() );
        }
        catch (Exception ex)
        {
            System.err.print(  "ERROR trying to access metadata file "
                    + modelerMetadataFile + ":\n" + ex.getMessage() );
        }
    }

    /**
     * Pause for the specified number of milliseconds.
     *
     * @param millisecondsToPause Milliseconds to pause execution.
     */
    public static void pause(final int millisecondsToPause)
    {
        try
        {
            Thread.sleep(millisecondsToPause);
        }
        catch (InterruptedException threadAwakened)
        {
            System.err.println("Don't wake me up!\n" + threadAwakened.getMessage());
        }
    }

    /**
     * Main driver to demonstrate Apache Commons Modeler used with JMX
     * Model MBeans.
     *
     * @param arguments Command-line arguments.
     */
    public static void main(final String[] arguments)
    {
        ModelerMBeanMain me = new ModelerMBeanMain();
        me.applyCommonsModeler();
        pause(100000);
    }
}
