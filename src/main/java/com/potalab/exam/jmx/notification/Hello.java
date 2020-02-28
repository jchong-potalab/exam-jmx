package com.potalab.exam.jmx.notification;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.ObjectName;
import javax.management.RuntimeErrorException;
import javax.management.RuntimeMBeanException;

public class Hello extends GenericBroadcaster implements Runnable,  HelloMBean, MBeanRegistration {

  private final String name = "Reginald";

  private int cacheSize = DEFAULT_CACHE_SIZE;

  private static final int DEFAULT_CACHE_SIZE = 200;

  public boolean isStop = false;

  @Override
  public void stopProcess() {
    isStop = true;
  }

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
   * Allows the MBean to perform any operations it needs before being registered in the MBean
   * Server.  If the name of the MBean is not specified, the MBean can provide a name for its
   * registration.  If any exception is raised, the MBean will not be registered in the MBean
   * Server.
   *
   * @param server The MBean Server in which the MBean will be registered.
   * @param name   The object name of the MBean.  This name is null if the name parameter to one of
   *               the <code>createMBean</code> or
   *               <code>registerMBean</code> methods in the {@link MBeanServer}
   *               interface is null.  In that case, this method must return a non-null ObjectName
   *               for the new MBean.
   * @return The name under which the MBean is to be registered. This value must not be null.  If
   * the <code>name</code> parameter is not null, it will usually but not necessarily be the
   * returned value.
   * @throws Exception This exception will be caught by the MBean Server and re-thrown as an {@link
   *                   MBeanRegistrationException}.
   */
  @Override
  public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
    return null;
  }

  /**
   * Allows the MBean to perform any operations needed after having been registered in the MBean
   * server or after the registration has failed.
   * <p>If the implementation of this method throws a {@link RuntimeException}
   * or an {@link Error}, the MBean Server will rethrow those inside a {@link RuntimeMBeanException}
   * or {@link RuntimeErrorException}, respectively. However, throwing an exception in {@code
   * postRegister} will not change the state of the MBean: if the MBean was already registered
   * ({@code registrationDone} is {@code true}), the MBean will remain registered. </p>
   * <p>This might be confusing for the code calling {@code createMBean()}
   * or {@code registerMBean()}, as such code might assume that MBean registration has failed when
   * such an exception is raised. Therefore it is recommended that implementations of {@code
   * postRegister} do not throw Runtime Exceptions or Errors if it can be avoided.</p>
   *
   * @param registrationDone Indicates whether or not the MBean has been successfully registered in
   *                         the MBean server. The value false means that the registration phase has
   *                         failed.
   */
  @Override
  public void postRegister(Boolean registrationDone) {

  }

  /**
   * Allows the MBean to perform any operations it needs before being unregistered by the MBean
   * server.
   *
   * @throws Exception This exception will be caught by the MBean server and re-thrown as an {@link
   *                   MBeanRegistrationException}.
   */
  @Override
  public void preDeregister() throws Exception {

  }

  /**
   * Allows the MBean to perform any operations needed after having been unregistered in the MBean
   * server.
   * <p>If the implementation of this method throws a {@link RuntimeException}
   * or an {@link Error}, the MBean Server will rethrow those inside a {@link RuntimeMBeanException}
   * or {@link RuntimeErrorException}, respectively. However, throwing an exception in {@code
   * postDeregister} will not change the state of the MBean: the MBean was already successfully
   * deregistered and will remain so. </p>
   * <p>This might be confusing for the code calling
   * {@code unregisterMBean()}, as it might assume that MBean deregistration has failed. Therefore
   * it is recommended that implementations of {@code postDeregister} do not throw Runtime
   * Exceptions or Errors if it can be avoided.</p>
   */
  @Override
  public void postDeregister() {

  }

  private int heartBeatSeq = 0;

  /**
   * When an object implementing interface <code>Runnable</code> is used to create a thread,
   * starting the thread causes the object's
   * <code>run</code> method to be called in that separately executing
   * thread.
   * <p>
   * The general contract of the method <code>run</code> is that it may take any action whatsoever.
   *
   * @see Thread#run()
   */
  @Override
  public void run() {
    this.isStop = false;
    while (!this.isStop) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      Notification notification
          = new Notification("hello.heartbeat", this, heartBeatSeq
          , "This message is intended to inform you that the \"Hello\" object is alive.");

      sendNotification(notification);
      heartBeatSeq++;
    }
  }
}
