package com.potalab.exam.jmx.notification;

import  javax.management.*;
import  java.util.*;


/**
 * Naive implementation of NotificationBroadcaster.
 */
public class GenericBroadcaster implements NotificationBroadcaster {

  private static final boolean traceOn = true;
  /**
   * This is the list of listeners, which contains one triplet
   * object for each interested listener.
   */
  private ArrayList listeners = new ArrayList();
  /**
   * Hashtable containing the notification types and the number of
   * times each one was sent.
   */
  private Hashtable notifications = new Hashtable();

  /**
   * Default constructor. Delegates to alternate constructor.
   */
  public GenericBroadcaster () {
    this(null);
  }

  /**
   * Alternate constructor. Allows the agent to specify a String array
   * that contains the possible notification types that can be sent by
   * this broadcaster. This information is used for getNotificationInfo()
   */
  public GenericBroadcaster (String[] notificationTypes) {
    if (notificationTypes != null) {
      for (int aa = 0; aa < notificationTypes.length; aa++) {
        String notifType = notificationTypes[aa];
        trace("GenericBroadcaster(): INFO: Attempting to add notification type \'"
            + notifType + "\'.");
        if (notifType != null && !notifType.equals("")) {
          // We don't care if something else was there, the counters
          /// all get set to zero anyway...
          notifications.put(notifType, new Integer(0));
          trace("GenericBroadcaster(): INFO: Notification type \'"
              + notifType + "\' successfully added.");
        }
      }
    }
  }

  /**
   * Adds the specified listener (if not null) to the list of listeners.
   * A triplet object is created and added to the backing store.
   */
  public void addNotificationListener (NotificationListener listener, NotificationFilter filter,
      Object handback) {
    if (listener != null) {
      trace("GenericBroadcaster.addNotificationListener(): INFO: " +
          "Adding listener/filter/handback triplet: " + listener +
          "/" + filter + "/" + handback + ".");
      listeners.add(new ListenerFilterHandbackTriplet(listener, filter,
          handback));
    }
  }

  /**
   * Removes all triplets associated with the specified listener.
   */
  public void removeNotificationListener (NotificationListener listener) {
    trace("GenericBroadcaster.removeNotificationListener(): INFO: " + "Removing all triplets for listener: "
        + listener);
    removeNotificationListener(listener, null);
  }

  /**
   * Removes the specified listener/filter/handback triplet from the
   * list. If the handback object is null, all triplets for the specified
   * listener are removed. The handback reference must be to the same
   * object instance (not a clone).
   */
  public void removeNotificationListener (NotificationListener listener,
      Object handback) {
    trace("GenericBroadcaster.removeNotificationListener(): INFO: Removing listener/handback: "
        + listener + "/" + handback);
    if (listener != null) {
      Iterator iter = listeners.iterator();
      while (iter.hasNext()) {
        ListenerFilterHandbackTriplet triplet = (ListenerFilterHandbackTriplet)iter.next();
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

  /**
   * Constructs an array of MBeanNotificationInfo objects and returns
   * it to the caller.
   */
  public MBeanNotificationInfo[] getNotificationInfo () {
    MBeanNotificationInfo[] notifications = new MBeanNotificationInfo[1];
    String[] notificationTypes = new String[this.notifications.size()];
    Iterator iter = this.notifications.keySet().iterator();
    int aa = 0;
    while (iter.hasNext()) {
      notificationTypes[aa] = (String)iter.next();
      aa++;
    }
    notifications[0] = new MBeanNotificationInfo(notificationTypes, "NotificationTypes",
        "Types of notifications emitted by this broadcaster.");
    return  notifications;
  }

  /**
   * Sends the specified notification. The hashtable of notifications
   * is checked to see if this notification has been sent. If not, a
   * new entry is created, otherwise the counter for this notification
   * type is incremented.
   */
  public void sendNotification (Notification notification) {
    if (notification != null) {
      String notifType = notification.getType();
      if (notifications.containsKey(notifType)) {
        Integer count = (Integer) notifications.get(notifType);
        notifications.put(notifType, new Integer(count.intValue() +
            1));
      }
      else {
        notifications.put(notifType, new Integer(1));
      }
      // Now send the notification to all interested listeners
      for (int aa = 0; aa < listeners.size(); aa++) {
        ListenerFilterHandbackTriplet triplet = (ListenerFilterHandbackTriplet) listeners.get(aa);
        trace("GenericBroadcaster.sendNotification(): INFO: Looking at triplet: "
            + triplet);
        NotificationListener listener = triplet.getListener();
        NotificationFilter filter = triplet.getFilter();
        Object handback = triplet.getHandback();
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
  private class ListenerFilterHandbackTriplet {
    private NotificationListener _listener;

    NotificationListener getListener () {
      return  _listener;
    }
    private NotificationFilter _filter;

    NotificationFilter getFilter () {
      return  _filter;
    }
    private Object _handback;

    Object getHandback () {
      return  _handback;
    }

    ListenerFilterHandbackTriplet (NotificationListener listener, NotificationFilter filter,
        Object handback) {
      _listener = listener;
      _filter = filter;
      _handback = handback;
    }

    public String toString () {
      return  _listener + "/" + _filter + "/" + _handback;
    }
  }

  private void trace (String message) {
    if (traceOn)
      System.out.println(message);
  }
}