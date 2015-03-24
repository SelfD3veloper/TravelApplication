package cbedoy.cblibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cbedoy.cblibrary.interfaces.IActivityResultListener;
import cbedoy.cblibrary.interfaces.IAppViewManager;
import cbedoy.cblibrary.interfaces.IViewController;
import cbedoy.cblibrary.services.ApplicationLoader;
import cbedoy.cblibrary.services.ImageService;
import cbedoy.cblibrary.services.InjectionManager;
import cbedoy.cblibrary.services.NotificationCenter;
import cbedoy.cblibrary.utils.Utils;
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

public abstract class MainActivity extends ActionBarActivity implements IAppViewManager{

    protected ViewFlipper                               mViewFlipper;
    protected int                                       mViewControllerWidth;
    protected int                                       mViewControllerHeight;
    protected HashMap<String, AbstractViewController>   mViewModel;
    protected List<IActivityResultListener>             mListeners;
    protected IViewController                           mCurrentViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Required instance mViewFlipper from extends class
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mViewModel = new HashMap<>();
        mViewControllerHeight = ImageService.getScreenHeight();
        mViewControllerWidth = ImageService.getScreenWidth();
        mListeners = new ArrayList<>();
        Utils.init(this);
        ImageService.init(this);
    }

    @Override
    public void addActivityResultListener(IActivityResultListener resultListener) {
        mListeners.add(resultListener);
    }

    @Override
    public void finish() {
        long delay = 200;
        final MainActivity self = this;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                self.terminateInApp();
            }
        }, delay);
    }

    private void terminateInApp(){
        overridePendingTransition(R.anim.fadeout, R.anim.fadein);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(mListeners != null)
        {
            for(IActivityResultListener resultListener : mListeners)
            {
                if(requestCode == resultListener.getRequestCode())
                {
                    resultListener.onActivityResult(resultCode, data);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        boolean allowBack = true;
        int displayed_child = this.mViewFlipper.getDisplayedChild();
        View view = this.mViewFlipper.getChildAt(displayed_child);

        for(Map.Entry<String, AbstractViewController> entry : this.mViewModel.entrySet()) {
            AbstractViewController child = entry.getValue();
            if(child.getViewController() == view) {
                allowBack = child.onBackPressed();
                break;
            }
        }

        if(allowBack)
            super.onBackPressed();
    }

    @Override
    public int getViewControllerWidth() {
        return this.mViewControllerWidth;
    }

    @Override
    public int getViewControllerHeight() {
        return this.mViewControllerHeight;
    }

    @Override
    public Activity getActivity() {
        return null;
    }

    @Override
    public void reActivateCurrentView() {
        final MainActivity self = this;

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int displayed_child = self.mViewFlipper.getDisplayedChild();
                View view = self.mViewFlipper.getChildAt(displayed_child);

                for(Map.Entry<String, AbstractViewController> entry : self.mViewModel.entrySet()) {
                    AbstractViewController child = entry.getValue();

                    if(child.getViewController() == view) {
                        child.setActive(true);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void presentViewForTag(String tag) {
        final MainActivity self = this;
        final String final_tag = tag;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AbstractViewController child = self.mViewModel.get(final_tag);
                View view = child.getViewController();

                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);

                int child_index = self.mViewFlipper.indexOfChild(view);
                if (child_index < 0) {
                    self.mViewFlipper.addView(view);
                    child_index = self.mViewFlipper.indexOfChild(view);
                }

                int displayed_child = self.mViewFlipper.getDisplayedChild();
                if (child_index != displayed_child)
                {

                    AlphaAnimation in = new AlphaAnimation(0.0f, 1.0f);
                    in.setDuration(600);
                    in.setZAdjustment(Animation.ZORDER_TOP);
                    self.mViewFlipper.setInAnimation(in);

                    AlphaAnimation out = new AlphaAnimation(1.0f, 0.0f);
                    out.setDuration(600);
                    out.setZAdjustment(Animation.ZORDER_TOP);
                    self.mViewFlipper.setOutAnimation(out);


                    self.mViewFlipper.setDisplayedChild(child_index);


                    final IViewController controller = mCurrentViewController;
                    controller.setActive(false);
                    if (!self.isFinishing()) {
                        self.mViewFlipper.setInAnimation(null);
                        self.mViewFlipper.setOutAnimation(null);
                        self.mViewFlipper.removeView(controller.getViewController());
                        controller.onRemoveToWindow();
                    }
                }
                child.onAttachToWindow();
                child.setActive(true);
                mCurrentViewController = child;
            }
        });
    }

    @Override
    public void addViewWithTag(AbstractViewController controller, String tag) {
        this.mViewModel.put(tag, controller);
    }

    @Override
    public void setBackgroundViewController(Object... values) {

    }
}
