package com.potalab.exam.jmx.dynamicmbean;

import javax.management.DynamicMBean;

/**
 * **MBean Interface**
 *	인터페이스 메소드는 MBean의 속성과 동작을 작성한다.
 *	standard MBean은 MBean 인터페이스와 클래스로 구성된다.
 *	JMX agent MBean을 관리한다.
 *	규약에 따르면 MBean 인터페이스는 suffix로 MBean으로 명명한다.
 *	관리되는 application이 값을 읽거나 변경이 가능하도록 getter, setter를 지원한다.
 *	참조 문헌
 *	- https://docs.oracle.com/javase/tutorial/jmx/mbeans/standard.html
 */
public interface HelloMBeanX extends DynamicMBean {
 
    public void sayHello(); 
    public int add(int x, int y); 
    
    public String getName(); 
     
    public int getCacheSize(); 
    public void setCacheSize(int size); 
} 