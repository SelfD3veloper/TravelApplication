package cbedoy.cblibrary.interfaces;

import android.app.Activity;

import cbedoy.cblibrary.viewcontrollers.AbstractViewController;

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
public interface IAppViewManager
{
    public Activity getActivity();
    public void setBackgroundViewController(Object... values);
    public void reActivateCurrentView();
    public int getViewControllerWidth();
    public int getViewControllerHeight();
    public void presentViewForTag(String tag);
    public void addViewWithTag(AbstractViewController controller, String tag);
    public void addActivityResultListener(IActivityResultListener resultListener);
}
