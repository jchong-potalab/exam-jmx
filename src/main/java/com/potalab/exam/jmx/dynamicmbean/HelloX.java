package com.potalab.exam.jmx.dynamicmbean;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;

/**
 * **MBean Implementation**
 *
 */
public class HelloX implements DynamicMBean {
	  private final String name = "Reginald";
    private int cacheSize = DEFAULT_CACHE_SIZE; 
    private static final int DEFAULT_CACHE_SIZE = 200; 
    
    public void sayHello() { 
        System.out.println("hello, world"); 
    } 
     
    public int add(int x, int y) { 
        return x + y; 
    } 
     
    public String getName() { 
        return this.name; 
    }  
     
    public int getCacheSize() { 
        return this.cacheSize; 
    } 
     
    public synchronized void setCacheSize(int size) {
    
        this.cacheSize = size; 
        System.out.println("Cache size now " + this.cacheSize); 
    }

    /**
     * Obtain the value of a specific attribute of the Dynamic MBean.
     *
     * @param attribute The name of the attribute to be retrieved
     * @return The value of the attribute retrieved.
     * @throws AttributeNotFoundException
     * @throws MBeanException             Wraps a <CODE>java.lang.Exception</CODE> thrown by the
     *                                    MBean's getter.
     * @throws ReflectionException        Wraps a <CODE>java.lang.Exception</CODE> thrown while trying
     *                                    to invoke the getter.
     * @see #setAttribute
     */
    @Override
    public Object getAttribute(String attribute)
        throws AttributeNotFoundException, MBeanException, ReflectionException {

        if(attribute.equals("CacheSize")) {
            return this.getCacheSize();
        }

        return null;
    }

    /**
     * Set the value of a specific attribute of the Dynamic MBean.
     *
     * @param attribute The identification of the attribute to be set and  the value it is to be set
     *                  to.
     * @throws AttributeNotFoundException
     * @throws InvalidAttributeValueException
     * @throws MBeanException                 Wraps a <CODE>java.lang.Exception</CODE> thrown by the
     *                                        MBean's setter.
     * @throws ReflectionException            Wraps a <CODE>java.lang.Exception</CODE> thrown while
     *                                        trying to invoke the MBean's setter.
     * @see #getAttribute
     */
    @Override
    public void setAttribute(Attribute attribute)
        throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {

        if(attribute.getName().equals("CacheSize")) {
            this.setCacheSize((int)attribute.getValue());
        }

    }

    /**
     * Get the values of several attributes of the Dynamic MBean.
     *
     * @param attributes A list of the attributes to be retrieved.
     * @return The list of attributes retrieved.
     * @see #setAttributes
     */
    @Override
    public AttributeList getAttributes(String[] attributes) {
        return null;
    }

    /**
     * Sets the values of several attributes of the Dynamic MBean.
     *
     * @param attributes A list of attributes: The identification of the attributes to be set and  the
     *                   values they are to be set to.
     * @return The list of attributes that were set, with their new values.
     * @see #getAttributes
     */
    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        return null;
    }

    /**
     * Allows an action to be invoked on the Dynamic MBean.
     *
     * @param actionName The name of the action to be invoked.
     * @param params     An array containing the parameters to be set when the action is invoked.
     * @param signature  An array containing the signature of the action. The class objects will be
     *                   loaded through the same class loader as the one used for loading the MBean on
     *                   which the action is invoked.
     * @return The object returned by the action, which represents the result of invoking the action
     * on the MBean specified.
     * @throws MBeanException      Wraps a <CODE>java.lang.Exception</CODE> thrown by the MBean's
     *                             invoked method.
     * @throws ReflectionException Wraps a <CODE>java.lang.Exception</CODE> thrown while trying to
     *                             invoke the method
     */
    @Override
    public Object invoke(String actionName, Object[] params, String[] signature)
        throws MBeanException, ReflectionException {
        if (actionName.equals("sayHello")) {
            this.sayHello();
        }
        return null;
    }

    /**
     * Provides the exposed attributes and actions of the Dynamic MBean using an MBeanInfo object.
     *
     * @return An instance of <CODE>MBeanInfo</CODE> allowing all attributes and actions exposed by
     * this Dynamic MBean to be retrieved.
     */
    @Override
    public MBeanInfo getMBeanInfo() {
        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[] {
            new MBeanAttributeInfo("CacheSize", int.class.getName(),
                "CacheSize for response",
                true, true, false), };

        MBeanParameterInfo[] parameterInfos = new MBeanParameterInfo[] {};

        MBeanOperationInfo[] operations = new MBeanOperationInfo[] {
            new MBeanOperationInfo("sayHello",
                "Console write to \"hello, world\"",
                parameterInfos, null, MBeanOperationInfo.ACTION),
        };

        return new MBeanInfo(HelloX.class.getName(),
            "Jmx support POC....", attributes, null,
            operations, null);
    }
}