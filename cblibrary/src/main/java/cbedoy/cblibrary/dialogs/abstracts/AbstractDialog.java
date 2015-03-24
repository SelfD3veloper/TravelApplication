package cbedoy.cblibrary.dialogs.abstracts;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import cbedoy.cblibrary.R;
import cbedoy.cblibrary.services.BlurService;


/**
 * Created by Carlos Bedoy on 10/28/14.
 *
 * Mobile App Developer - Bills Android
 *
 * Pademobile
 */
public abstract class AbstractDialog implements DialogSupported.IDialogSupportedCallback
{
    protected Activity mActivity;
    protected ViewGroup mView;
    protected DialogSupported mDialog;
    private Drawable mDrawableBackground;

    public AbstractDialog(Activity activity){
        mActivity = activity;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void show()
    {
        final AbstractDialog weakSelf = this;
        this.mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!weakSelf.mActivity.isFinishing())
                {
                    weakSelf.mActivity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run() {
                            if (weakSelf.mDialog == null)
                                weakSelf.createDialogView();
                            weakSelf.takeCurrentScreamShot();
                            weakSelf.reload();
                            weakSelf.mDialog.show();
                        }
                    });
                }
            }
        });
    }

    public  void createDialogView()
    {
        mDialog = new DialogSupported(mActivity);
        mDialog.setCallback(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(onCreateView());
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mDialog.getWindow().setWindowAnimations(R.style.other_apps_animation);
    }

    public void dissmiss()
    {
        final AbstractDialog weakSelf = this;
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (weakSelf.mDialog != null)
                    weakSelf.mDialog.dismiss();
                System.gc();
            }
        });
    }

    protected void takeCurrentScreamShot()
    {
        mDrawableBackground = BlurService.getInstance().doBlurWithActivity(mActivity);

        mDialog.getWindow().setBackgroundDrawable(mDrawableBackground);
    }


    public void dealloc()
    {
        mActivity = null;
        mDialog = null;
        if(mView != null)
        {
            mView.removeAllViews();
        }
        mView = null;
    }

    public abstract View onCreateView();
    public abstract void reload();

    @Override
    public void onShow() {}

    @Override
    public void onDismiss()
    {
        if(mDrawableBackground != null && mDrawableBackground instanceof BitmapDrawable)
        {
            Bitmap bitmap = ((BitmapDrawable) mDrawableBackground).getBitmap();
            bitmap.recycle();
            bitmap = null;
        }
        if(mView != null)
        {
            mView.removeAllViews();
        }
        mDialog = null;
        mDrawableBackground = null;
        System.gc();
    }
}
