package cbedoy.cblibrary.services;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by Carlos Bedoy on 28/12/2014.
 *
 * Mobile App Developer
 * CBLibrary
 *
 * E-mail: carlos.bedoy@gmail.com
 * Facebook: https://www.facebook.com/carlos.bedoy
 * Github: https://github.com/cbedoy
 */
public class NotificationCenter {

    private static NotificationCenter mInstance;
    private final HashMap<Integer, ArrayList<NotificationListener>> mListeners;

    private NotificationCenter() {
        mListeners = new HashMap<Integer, ArrayList<NotificationListener>>();
    }

    public static NotificationCenter getInstance() {
        if (mInstance == null) {
            mInstance = new NotificationCenter();
        }
        return mInstance;
    }

    public static void clearInstance() {
        mInstance = null;
    }

    public void addListener(Integer type, NotificationListener listener) {
        synchronized (mListeners) {
            ArrayList<NotificationListener> listeners = mListeners.get(type);
            if (listeners == null) {
                listeners = new ArrayList<NotificationListener>();
            }

            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }

            mListeners.put(type, listeners);
        }
    }

    public void removeListener(Integer type, NotificationListener listener) {
        synchronized (mListeners) {
            ArrayList<NotificationListener> listeners = mListeners.get(type);
            if (listeners != null && listeners.contains(listener)) {
                listeners.remove(listener);
            }

            if (listeners != null && listeners.size() > 0) {
                mListeners.put(type, listeners);
            } else {
                mListeners.remove(type);
            }
        }
    }

    public void postNotification(Integer type, Object... notification) {
        synchronized (mListeners) {
            ArrayList<NotificationListener> listeners = mListeners.get(type);
            if (listeners != null && listeners.size() > 0) {
                for (NotificationListener listener : listeners) {
                    listener.didReceivedNotification(type, notification);
                }
            }
        }
    }

    public interface NotificationListener {

        public abstract void didReceivedNotification(Integer type, Object... args);

    }

}
