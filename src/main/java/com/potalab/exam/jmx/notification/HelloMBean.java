package com.potalab.exam.jmx.notification;

public interface HelloMBean {

    public void stopProcess();
    public void sayHello(); 
    public int add(int x, int y); 
    
    public String getName(); 
     
    public int getCacheSize(); 
    public void setCacheSize(int size); 
} 