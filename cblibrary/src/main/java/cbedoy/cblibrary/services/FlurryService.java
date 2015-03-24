
package cbedoy.cblibrary.services;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;


/**
 * Created by Carlos Bedoy on 26/12/14.
 *
 * Mobile App Developer @ Bills Android
 *
 * Pademobile
 */


import com.flurry.android.FlurryAgent;

import java.util.HashMap;
import java.util.Map;

import cbedoy.cblibrary.business.InjectionManager;
import cbedoy.cblibrary.interfaces.ICheckPointHandler;

public class FlurryService implements ICheckPointHandler
{
    private static FlurryService instance;
    private boolean activateFlurry = InjectionManager.getInstance().enableFlurry();
    private String mKey;

    public static FlurryService getInstance()
    {
        if(instance == null)
            instance = new FlurryService();
        return  instance;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    private FlurryService()
    {
        if(activateFlurry)
        {
            FlurryAgent.setLogEnabled(true);
            FlurryAgent.setLogEvents(true);
            FlurryAgent.setReportLocation(true);
            LocationManager lm = (LocationManager)ApplicationLoader.mainContext.getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null){
                float longitude = (float) location.getLongitude();
                float latitude = (float) location.getLatitude();
                FlurryAgent.setLocation(longitude, latitude);
            }
        }
    }

    public void init(){
        if(activateFlurry)
        {
            FlurryAgent.init(ApplicationLoader.mainContext, mKey);
        }
    }

    public void startSession(Context context)
    {
        if(activateFlurry)
        {
            FlurryAgent.onStartSession(context, mKey);
        }
    }

    public void endSession(Context context)
    {
        if(activateFlurry)
            FlurryAgent.onEndSession(context);
    }

    @Override
    public void logEvent(String event)
    {
        if(activateFlurry) {
            FlurryAgent.logEvent(event);
            LogService.e(event);
        }
    }

    @Override
    public void logEvent(String event, boolean timed)
    {
        if(activateFlurry) {
            FlurryAgent.logEvent(event, timed);
            LogService.e("Timed "+timed);
        }
    }

    @Override
    public void logEvent(String event, HashMap<String, String> parameters)
    {
        if(activateFlurry)
        {
            FlurryAgent.logEvent(event, parameters);
            LogService.e(event);
            for(Map.Entry<String, String> entry : parameters.entrySet()){
                LogService.i(entry.getKey() + " : "+ entry.getValue());
            }
        }
    }

    @Override
    public void logEvent(String event, HashMap<String, String> parameters, boolean timed)
    {
        if(activateFlurry)
        {
            FlurryAgent.logEvent(event, parameters, timed);
            LogService.e(event);
            LogService.e("<<<------------------------------------------------");
            for(Map.Entry<String, String> entry : parameters.entrySet()){
                LogService.i(entry.getKey() + " : "+ entry.getValue());
            }
            LogService.e("Timed "+timed);
        }
    }

}
