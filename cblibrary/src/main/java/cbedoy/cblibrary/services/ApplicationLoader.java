package cbedoy.cblibrary.services;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.HashMap;
import java.util.Map;

import cbedoy.cblibrary.R;
import cbedoy.cblibrary.utils.ImageLoaderService;

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
public class ApplicationLoader extends Application
{
    public static volatile Handler mainHandler;
    public static volatile Context mainContext;
    public static volatile LayoutInflater mainLayoutInflater;
    public static volatile DisplayImageOptions options;
    public static volatile String urlProject;
    public static volatile Typeface boldFont;
    public static volatile Typeface regularFont;
    public static volatile Typeface thinFont;
    public static volatile Typeface lightFont;
    public static volatile Typeface cardFont;
    public static Integer DISMISS_LOADER;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mainContext = getApplicationContext();
        mainHandler = new Handler(getMainLooper());
        mainLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        boldFont 	                    = Typeface.createFromAsset(mainContext.getAssets(), "fonts/Roboto-Bold.ttf");
        regularFont                     = Typeface.createFromAsset(mainContext.getAssets(), "fonts/Roboto-Regular.ttf");
        thinFont 	                    = Typeface.createFromAsset(mainContext.getAssets(), "fonts/Roboto-Thin.ttf");
        lightFont 	                    = Typeface.createFromAsset(mainContext.getAssets(), "fonts/Roboto-Light.ttf");
        cardFont 	                    = Typeface.createFromAsset(mainContext.getAssets(), "fonts/CardType.ttf");

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(5 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

        ImageLoaderService.getInstance();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static int getAppVersion() {
        try {
            PackageInfo packageInfo = mainContext.getPackageManager().getPackageInfo(mainContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static void startPushService() {
        SharedPreferences preferences = mainContext.getSharedPreferences("Notifications", MODE_PRIVATE);

        if (preferences.getBoolean("pushService", true)) {
            mainContext.startService(new Intent(mainContext, NotificationService.class));

            if (android.os.Build.VERSION.SDK_INT >= 19) {
                PendingIntent pintent = PendingIntent.getService(mainContext, 0, new Intent(mainContext, NotificationService.class), 0);
                AlarmManager alarm = (AlarmManager)mainContext.getSystemService(Context.ALARM_SERVICE);
                alarm.cancel(pintent);
            }
        } else {
            stopPushService();
        }
    }

    public static void stopPushService() {
        mainContext.stopService(new Intent(mainContext, NotificationService.class));

        PendingIntent pintent = PendingIntent.getService(mainContext, 0, new Intent(mainContext, NotificationService.class), 0);
        AlarmManager alarm = (AlarmManager)mainContext.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pintent);
    }


    public static void postInitApplication() {
        //TODO NOTIFICATION RECEIVER
    }

    public static void savePreferences(String keyShared, HashMap<String, Object> information){
        SharedPreferences sharedPreferences = mainContext.getSharedPreferences(keyShared, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        for(String key : information.keySet()){
            edit.putString(key, information.get(key).toString());
        }
        edit.commit();
    }

    public static HashMap<String, Object> getSharedFromKey(String key){
        HashMap<String, Object> information = new HashMap<String, Object>();
        SharedPreferences sharedPreferences = mainContext.getSharedPreferences(key, Context.MODE_PRIVATE);
        Map<String, ?> all = sharedPreferences.getAll();
        for(Map.Entry<String,?> entry : all.entrySet()){
            information.put(entry.getKey(), entry.getValue());
        }
        return information;
    }

    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getWindow() != null && activity.getWindow().getDecorView() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    private boolean verifyIfApplicationInstalled(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }
}
