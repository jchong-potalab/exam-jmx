package com.potalab.exam.jmx.notification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

public class GenericBroadcaster implements NotificationBroadcaster {

  private static final boolean TRACE_ON = true;
  private List<NotificationListenerInfo> listeners = new ArrayList<>();
  private List<String> notificationTypes = new CopyOnWriteArrayList<>();

  public GenericBroadcaster () {
    this(null);
  }
  public GenericBroadcaster (String[] notificationTypes) {
    if (notificationTypes != null) {
      for (String notifType : notificationTypes) {
        trace("GenericBroadcaster(): INFO: Attempting to add notification type \""
            + notifType + "\".");
        if (notifType != null && !notifType.equals("")) {
          this.notificationTypes.add(notifType);
          trace("GenericBroadcaster(): INFO: Notification type \""
              + notifType + "\" successfully added.");
        }
      }
    }
  }

  @Override
  public void addNotificationListener (NotificationListener listener, NotificationFilter filter,
      Object handback) {
    if (listener != null) {
      trace("GenericBroadcaster.addNotificationListener(): INFO: " +
          "Adding listener/filter/handback triplet: " + listener +
          "/" + filter + "/" + handback + ".");
      listeners.add(new NotificationListenerInfo(listener, filter,
          handback));
    }
  }

  @Override
  public void removeNotificationListener (NotificationListener listener) {
    trace("GenericBroadcaster.removeNotificationListener(): INFO: "
        + "Removing all triplets for listener: "
        + listener);
    removeNotificationListener(listener, null);
  }

  public void removeNotificationListener (NotificationListener listener,
      Object handback) {
    trace("GenericBroadcaster.removeNotificationListener(): INFO: Removing listener/handback: "
        + listener + "/" + handback);
    if (listener != null) {
      Iterator iter = listeners.iterator();
      while (iter.hasNext()) {
        NotificationListenerInfo triplet = (NotificationListenerInfo)iter.next();
        trace("GenericBroadcaster.removeNotificationListener(): INFO: Examining triplet: "
            + triplet);
        if (listener == triplet.getListener() && (handback == null ||
            handback == triplet.getHandback())) {
          trace("GenericBroadcaster.removeNotificationListener(): INFO: Removing triplet: "
              + triplet);
          iter.remove();
          if (handback != null)
            break;
        }
      }
    }
  }

  @Override
  public MBeanNotificationInfo[] getNotificationInfo () {
    MBeanNotificationInfo[] notificationInfos = new MBeanNotificationInfo[1];
    String[] notificationTypeNames = new String[this.notificationTypes.size()];
    Iterator iter = this.notificationTypes.iterator();
    int aa = 0;
    while (iter.hasNext()) {
      notificationTypeNames[aa] = (String)iter.next();
      aa++;
    }
    notificationInfos[0] = new MBeanNotificationInfo(notificationTypeNames, "NotificationTypes",
        "Types of notificationInfos emitted by this broadcaster.");
    return  notificationInfos;
  }

  public void sendNotification (Notification notification) {
    if (notification != null) {
      String notifType = notification.getType();
      if (!notificationTypes.contains(notifType)) {
        notificationTypes.add(notifType);
      }
      // Now send the notification to all interested listeners
      for (NotificationListenerInfo notificationListenerInfo : listeners) {
        trace("GenericBroadcaster.sendNotification(): INFO: Looking at triplet: "
            + notificationListenerInfo);
        NotificationListener listener = notificationListenerInfo.getListener();
        NotificationFilter filter = notificationListenerInfo.getFilter();
        Object handback = notificationListenerInfo.getHandback();
        if (filter == null || filter.isNotificationEnabled(notification)) {
          trace("GenericBroadcaster.sendNotification(): INFO: Sending notification to listener: "
              + listener + ", sending handback: " + handback);
          listener.handleNotification(notification, handback);
        }
      }
    }
  }

  /**
   * This class is used to encapsulate a listener/filter/handback triplet.
   */
  private static class NotificationListenerInfo {
    private NotificationListener listener;
    private NotificationListener getListener () {
      return listener;
    }
    private NotificationFilter filter;
    NotificationFilter getFilter () {
      return filter;
    }
    private Object handback;
    Object getHandback () {
      return handback;
    }

    NotificationListenerInfo(NotificationListener listener, NotificationFilter filter,
        Object handback) {
      this.listener = listener;
      this.filter = filter;
      this.handback = handback;
    }

    public String toString () {
      return  listener + "/" + filter + "/" + handback;
    }
  }

  private void trace (String message) {
    if (TRACE_ON)
      System.out.println(message);
  }
}