package cbedoy.cblibrary.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class Utils
{
    private static Handler mHandler;

    public static void init(Activity activity) {
        mHandler = new Handler(activity.getMainLooper());
    }

    public static void deInit() {
        mHandler = null;
    }

    public static void postRunnable(Runnable runnable) {
        mHandler.post(runnable);
    }

    public static void postRunnableAtFrontOfQueue(Runnable runnable) {
        mHandler.postAtFrontOfQueue(runnable);
    }

    public static void postDelayedRunnable(Runnable runnable, long delay) {
        mHandler.postDelayed(runnable, delay);
    }

    public static HashMap<String, Object> mergeHashMaps(HashMap<String, Object> map1, HashMap<String, Object> map2) {
        HashMap<String, Object> merge = new HashMap<String, Object>();

        for (Map.Entry<String, Object> entry : map2.entrySet()) {
            merge.put(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, Object> entry : map1.entrySet()) {
            merge.put(entry.getKey(), entry.getValue());
        }

        return merge;
    }

    public static Object toJSON(Object object) throws JSONException {
        if (object instanceof Map) {
            JSONObject json = new JSONObject();
            Map map = (Map) object;
            for (Object key : map.keySet()) {
                json.put(key.toString(), toJSON(map.get(key)));
            }
            return json;
        } else if (object instanceof Iterable) {
            JSONArray json = new JSONArray();
            for (Object value : ((Iterable) object)) {
                json.put(value);
            }
            return json;
        } else
            return object;
    }

    public static boolean isEmptyObject(JSONObject object) {
        return object.names() == null;
    }

    public static Map<String, Object> getMap(JSONObject object, String key) throws JSONException {
        return toMap(object.getJSONObject(key));
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(object.get(key)));
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static List toList(JSONArray array) throws JSONException {
        List list = new ArrayList();
        for (int i = 0; i < array.length(); i++) {
            list.add(fromJson(array.get(i)));
        }
        return list;
    }

    private static Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else {
            if (json instanceof JSONObject) {
                return toMap((JSONObject) json);
            } else {
                if (json instanceof JSONArray) {
                    return toList((JSONArray) json);
                } else {
                    return json;
                }
            }
        }
    }

    public static String mapToUrlString(HashMap<String, Object> map) {
        String result = "";
        try {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                String to_append = URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value.toString(), "UTF-8");
                result = result + (result.length() > 0 ? "&" : "") + to_append;
            }
        } catch (Exception ex) {
            result = "";
        }
        return result;
    }

    public static String mapToUrlDjangoString(String url, HashMap<String, Object> map){
        String result = "";
        result = url;
        for(String key : map.keySet())
        {
            result = result.replace(key, map.get(key).toString());
        }
        return result;
    }

    public static boolean isValidEmail(String eMail) {
        String email_pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(email_pattern);
        Matcher matcher = pattern.matcher(eMail);
        return matcher.matches();
    }

    public static Drawable getDrawableImage(String url){
        return null;
    }

    private static void die(String foa) {
        throw new IllegalArgumentException(foa);
    }

    public static Bitmap takeScreenShot(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap drawingCache = view.getDrawingCache();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap bitmap = Bitmap.createBitmap(drawingCache, 0, statusBarHeight, width, height  - statusBarHeight);
        view.destroyDrawingCache();
        return bitmap;
    }

}
