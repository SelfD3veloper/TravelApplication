package cbedoy.cblibrary.interfaces;

import android.view.View;

/**
 * Created by Carlos Bedoy on 2/16/15.
 * <p/>
 * Mobile App Developer - Bills Android
 * <p/>
 * Pademobile
 */
public interface IViewController
{
    public View getViewController();

    public String getTag();

    public void onStop();

    public void onStart();

    public void onFinish();

    public void onResume();

    public void onPause();

    public void onAttachToWindow();

    public void onRemoveToWindow();

    public void onLowMemory();

    public void setActive(boolean buttonsStatus);

    public boolean onBackPressed();

}
