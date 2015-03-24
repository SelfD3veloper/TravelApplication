package cbedoy.cblibrary.viewcontrollers;


import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;



import cbedoy.cblibrary.interfaces.IAppViewManager;
import cbedoy.cblibrary.interfaces.IMementoHandler;
import cbedoy.cblibrary.interfaces.IMessageRepresentationHandler;
import cbedoy.cblibrary.interfaces.IToastRepresentationHandler;
import cbedoy.cblibrary.interfaces.IViewController;
import cbedoy.cblibrary.services.ApplicationLoader;
import cbedoy.cblibrary.services.FlurryService;
import cbedoy.cblibrary.services.LogService;

/**
 * Created by Carlos Bedoy on 10/7/14.
 *
 * Mobile App Developer @Pademobile
 */
public abstract class AbstractViewController implements IViewController
{
    protected View                          mViewController;
    protected boolean                       mActive;
    protected String                        mTag;
    protected boolean                       mIsAttached;
    protected IAppViewManager               mViewManager;
    protected IMementoHandler               mMementoHandler;
    protected IToastRepresentationHandler   mToastRepresentationHandler;
    protected IMessageRepresentationHandler mMessageRepresentationHandler;

    protected abstract View onCreateView();


    public void setToastRepresentationHandler(IToastRepresentationHandler toastRepresentationHandler) {
        this.mToastRepresentationHandler = toastRepresentationHandler;
    }

    public void setTag(String tag) {
        this.mTag = tag;
    }

    public void setAppViewManager(IAppViewManager appViewManager) {
        this.mViewManager = appViewManager;
    }

    public void setMessageRepresentationHandler(IMessageRepresentationHandler messageRepresentationHandler) {
        this.mMessageRepresentationHandler = messageRepresentationHandler;
    }

    public View getViewController() {
        if(mViewController == null)
            mViewController = onCreateView();
        mViewController.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!(view instanceof EditText)) {
                    ApplicationLoader.hideKeyboard(mViewManager.getActivity());
                }
                return false;
            }
        });
        return mViewController;
    }

    public void setMementoHandler(IMementoHandler mementoHandler) {
        mMementoHandler = mementoHandler;
    }

    public void setActive(boolean active) {
        this.mActive = active;
    }

    public boolean onBackPressed() {
        return true;
    }

    public String getTag() {    return mTag; }

    @Override
    public void onAttachToWindow() {
        mIsAttached     = true;
        mActive         = true;

        FlurryService.getInstance().logEvent("Se añade la vista  " + mTag + " a la pantalla");
    }

    @Override
    public void onRemoveToWindow() {
        mIsAttached     = false;
        mViewController = null;
        mActive         = false;
        System.gc();

        FlurryService.getInstance().logEvent("Se elimina la vista "+ mTag +" de la pantalla");
    }

    @Override
    public void onLowMemory()
    {
        FlurryService.getInstance().logEvent("Hay poca memoria para la vista de "+mTag);
        System.gc();
    }

    @Override   public void onFinish() {
        LogService.i("ON FINISH FROM " + mTag);
    }

    @Override   public void onPause() {
        LogService.i("ON PAUSE FROM "+mTag);
    }

    @Override   public void onResume() {
        LogService.i("ON RESUME FROM "+mTag);
    }

    @Override   public void onStart() {
        LogService.i("ON START FROM "+mTag);
    }

    @Override   public void onStop() {
        LogService.i("ON STOP FROM "+mTag);
    }
}
