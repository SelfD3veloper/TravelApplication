/**
 * Created by Carlos Bedoy on 15/05/14.
 * exchange-android - Pademobile
 */
package cbedoy.cblibrary.services;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import java.util.concurrent.ExecutionException;

import cbedoy.cblibrary.interfaces.IAppViewManager;


public class BlurService {

    public static BlurService instance;
    private static Bitmap mBluredScreenShot;
    private static Bitmap mBluredService;
    private static Drawable mDrawableBackground;


    public static BlurService getInstance(){
        if(instance == null){
            instance = new BlurService();
        }
        return instance;
    }


    private Bitmap blurRenderScript(Bitmap smallBitmap)
    {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= 16)
        {
            bitmap = supportedBlur(smallBitmap, 25);
        }
        return bitmap;
    }

    private Bitmap blurRenderScript(Bitmap smallBitmap, int radius)
    {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= 16)
        {
            bitmap = supportedBlur(smallBitmap, radius);
        }
        return bitmap;
    }

    public Bitmap performRequestBlurByImage(Bitmap bitmap)
    {
        DoAsyncBlur doAsyncBlur = new DoAsyncBlur();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            doAsyncBlur.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bitmap);
        } else {
            doAsyncBlur.execute(bitmap);
        }
        try
        {
            return  doAsyncBlur.get();
        } catch (InterruptedException e) {
            return bitmap;
        } catch (ExecutionException e) {
            return bitmap;
        }
    }

    public Bitmap performRequestBlurByImageWithRadius(Bitmap bitmap, int radius)
    {
        DoAsyncBlurWithRadius doAsyncBlur = new DoAsyncBlurWithRadius();
        doAsyncBlur.setRadius(radius);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            doAsyncBlur.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bitmap);
        } else {
            doAsyncBlur.execute(bitmap);
        }
        try {
            return  doAsyncBlur.get();
        } catch (InterruptedException e) {
            return bitmap;
        } catch (ExecutionException e) {
            return bitmap;
        }
    }

    private class DoAsyncBlur extends AsyncTask<Bitmap, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {
            return blurRenderScript(bitmaps[0]);
        }
    }

    private class DoAsyncBlurWithRadius extends AsyncTask<Bitmap, Void, Bitmap> {
        private int radius;

        public void setRadius(int radius) {
            this.radius = radius;
        }

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {
            return blurRenderScript(bitmaps[0], radius);
        }
    }

    private Bitmap supportedBlur(Bitmap sentBitmap, int radius)
    {
        Bitmap output = Bitmap.createBitmap(sentBitmap.getWidth(), sentBitmap.getHeight(), sentBitmap.getConfig());
        RenderScript rs = RenderScript.create(ApplicationLoader.mainContext);
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation inAlloc = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_GRAPHICS_TEXTURE);
        Allocation outAlloc = Allocation.createFromBitmap(rs, output);
        script.setRadius(radius > 25 ? 25 : radius);
        script.setInput(inAlloc);
        script.forEach(outAlloc);
        outAlloc.copyTo(output);
        rs.destroy();
        sentBitmap.recycle();
        System.gc();
        return output;
    }


    public Drawable doBlurWithActivity(Activity activity)
    {
        if(mBluredScreenShot != null){
            mBluredScreenShot.recycle();
            mBluredScreenShot = null;
        }
        if (mDrawableBackground instanceof BitmapDrawable)
        {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) mDrawableBackground;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap.recycle();
        }
        takeCurrentScreamShot(activity);
        performBluring();
        return mDrawableBackground;
    }

    private void takeCurrentScreamShot(Activity activity)
    {
        try
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
            mBluredScreenShot = Bitmap.createBitmap(drawingCache, 0, statusBarHeight, width, height  - statusBarHeight);
            view.destroyDrawingCache();
            drawingCache.recycle();
            System.gc();
        }
        catch (Exception e)
        {
            System.gc();
        }
    }

    private void performBluring()
    {
        if(mBluredScreenShot != null)
        {
            Bitmap blurByImageWithRadius = BlurService.getInstance().performRequestBlurByImageWithRadius(mBluredScreenShot, 10);
            if(blurByImageWithRadius != null)
            {
                mDrawableBackground = new BitmapDrawable(blurByImageWithRadius);
            }
            else
            {
                mDrawableBackground = new ColorDrawable(Color.TRANSPARENT);
            }
        }
        else
        {
            mDrawableBackground = new ColorDrawable(Color.TRANSPARENT);
        }
    }

    public void doBlurWithBitmap(Bitmap bitmapFromFile, IAppViewManager viewManager)
    {
        if(mBluredService != null)
        {
            mBluredService.recycle();
            mBluredService = null;
        }
        Bitmap firstBlurImage = performRequestBlurByImage(bitmapFromFile);
        if(firstBlurImage != null)
        {
            mBluredService = performRequestBlurByImage(firstBlurImage);
            viewManager.setBackgroundViewController(mBluredService);
        }
        else
        {
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#CC2F2B29"));
            viewManager.setBackgroundViewController(bitmapFromFile, colorDrawable);
        }
        if(bitmapFromFile != null)
        {
            bitmapFromFile.recycle();
        }
        if(firstBlurImage != null)
        {
            firstBlurImage.recycle();
        }
    }

}
