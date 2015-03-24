package self.d3veloper.travelapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cbedoy.cblibrary.services.ApplicationLoader;

/**
 * Created by Carlos Bedoy on 23/03/2015.
 * <p/>
 * Mobile App Developer
 * TravelApplication
 * <p/>
 * E-mail: carlos.bedoy@gmail.com
 * Facebook: https://www.facebook.com/carlos.bedoy
 * Github: https://github.com/cbedoy
 */
public class SplashViewController extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ApplicationLoader.mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ApplicationLoader.mainContext, MasterViewController.class));
            }
        }, 3000);
    }
}
