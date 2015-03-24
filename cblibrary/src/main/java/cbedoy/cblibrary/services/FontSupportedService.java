package cbedoy.cblibrary.services;

import android.content.res.Configuration;
import android.util.TypedValue;
import android.widget.TextView;


/**
 * Created by Carlos Bedoy on 1/14/15.
 * <p/>
 * Mobile App Developer - Bills Android
 * <p/>
 * Pademobile
 */
public class FontSupportedService
{
    private static Configuration mConfiguration;
    private static float mFontScale;

    public static void init()
    {
        mConfiguration = ApplicationLoader.mainContext.getResources().getConfiguration();
        mFontScale = mConfiguration.fontScale;
    }

    public static void supportTextViewWithFontSize(TextView textView, float fontSize)
    {
        if(textView != null)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize / mFontScale);
    }

    public static void doInit(){
        mConfiguration = null;
    }

}
