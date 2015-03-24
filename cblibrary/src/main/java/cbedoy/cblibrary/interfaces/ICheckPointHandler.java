
package cbedoy.cblibrary.interfaces;

import java.util.HashMap;
/**
 * Created by Carlos Bedoy on 26/12/14.
 *
 * Mobile App Developer @ Bills Android
 *
 * Pademobile
 */
public interface ICheckPointHandler
{
    public void logEvent(String event);
    public void logEvent(String event, boolean timed);
    public void logEvent(String event, HashMap<String, String> parameters);
    public void logEvent(String event, HashMap<String, String> parameters, boolean timed);
}
