package cbedoy.cblibrary.services;

import android.util.Log;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cbedoy.cblibrary.business.InjectionManager;


/**
 * Created by Carlos Bedoy on 1/28/15.
 * <p/>
 * Mobile App Developer - Bills Android
 * <p/>
 * Pademobile
 */
public class LogService
{

    private static boolean mLogsEnabled = !InjectionManager.getInstance().isProduction();
    private static final String mTag    = "cblibrary";

    public static void i(String message)
    {
        if (mLogsEnabled) {
            Log.i(mTag, message);
        }
    }

    public static void i(String message, Throwable throwable)
    {
        if (mLogsEnabled) {
            Log.i(mTag, message, throwable);
        }
    }

    public static void d(String message)
    {
        if (mLogsEnabled) {
            Log.d(mTag, message);
        }
    }

    public static void d(String message, Throwable throwable) {
        if (mLogsEnabled) {
            Log.d(mTag, message, throwable);
        }
    }

    public static void e(String message) {
        if (mLogsEnabled) {
            Log.e(mTag, message);
        }
    }

    public static void e(String message, Throwable throwable)
    {
        if (mLogsEnabled) {
            Log.e(mTag, message, throwable);
        }
    }

    public static void v(String message)
    {
        if (mLogsEnabled) {
            Log.v(mTag, message);
        }
    }

    public static void v(String message, Throwable throwable)
    {
        if (mLogsEnabled) {
            Log.v(mTag, message, throwable);
        }
    }

    public static void w(String message) {
        if (mLogsEnabled) {
            Log.w(mTag, message);
        }
    }

    public static void w(Throwable throwable)
    {
        if (mLogsEnabled) {
            Log.w(mTag, throwable);
        }
    }

    public static void w(String message, Throwable throwable)
    {
        if (mLogsEnabled) {
            Log.w(mTag, message, throwable);
        }
    }

    public static void wtf(String message)
    {
        if (mLogsEnabled) {
            Log.wtf(mTag, message);
        }
    }

    public static void wtf(Throwable throwable)
    {
        if (mLogsEnabled) {
            Log.wtf(mTag, throwable);
        }
    }

    public static void wtf(String message, Throwable throwable)
    {
        if (mLogsEnabled) {
            Log.wtf(mTag, message, throwable);
        }
    }

    public static void logRequest(String url, int port, String uri, ArrayList<NameValuePair> nameValuePairs)
    {
        if(mLogsEnabled){
            Log.e(mTag, "------------------------------------------------------");
            Log.e(mTag, url+port+uri);
            for(NameValuePair pair : nameValuePairs)
            {
                String key = pair.getName();
                String value = pair.getValue();
                Log.e(mTag, key+" : "+value);
            }
        }

    }

    public static void logRequestFromPayPal(String url, HashMap<String, Object> params, HashMap<String, Object> headers)
    {
        if(mLogsEnabled)
        {
            Log.e(mTag, "------------------------------------------------------");
            Log.e(mTag, url);
            Log.e(mTag, "Params:");
            for(String key : params.keySet())
            {
                Log.e(mTag, key+" : "+params.get(key));
            }
            Log.e(mTag, "Headers:");
            for(String key : headers.keySet())
            {
                Log.e(mTag, key+" : "+headers.get(key));
            }
        }
    }

    public static void logResponse(HashMap<String, Object> response)
    {
        if(mLogsEnabled)
        {
            Log.e(mTag, "-------------------------------------------------------");
            for (String key : response.keySet())
            {
                Log.e(mTag, key + " : " + response.get(key));
            }
        }
    }

    public static void logResponseString(String responseString)
    {
        if(mLogsEnabled) {
            Log.e(mTag, "-------------------------------------------------------");
            Log.e(mTag, responseString);
        }
    }

    public static void logPaymentConfirmation(Map<String, Object> stringObjectMap)
    {
        if(mLogsEnabled)
        {
            Log.e(mTag, "-------------------------------------------------------");
            for (String key : stringObjectMap.keySet())
            {
                Log.e(mTag, key + " : " + stringObjectMap.get(key));
            }
        }
    }
}
