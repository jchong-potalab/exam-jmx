package com.potalab.exam.jmx.notification;

import javax.management.Notification;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Hello extends GenericBroadcaster implements Runnable,  HelloMBean {

  private final String name = "Reginald";

  private int cacheSize = DEFAULT_CACHE_SIZE;

  private static final int DEFAULT_CACHE_SIZE = 200;

  public boolean isStop = false;

  @Override
  public void startProcess() {
    ExecutorService es = Executors.newFixedThreadPool(1);
    es.submit(this);
    es.shutdown();

    while (!es.isShutdown() || !es.isTerminated()) {
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
  }

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
