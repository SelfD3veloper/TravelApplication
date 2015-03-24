package cbedoy.cblibrary.services;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;

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
public class ImageService {

    private static int mActionBarSize;
    private static int mStatusBarSize;
    private static int mNavigationBarSize;
    private static DisplayMetrics mMetrics;
    private static HashMap<String, Typeface> mFontsMap;
    private static HashMap<String, String> mImagesMap;
    public static Typeface boldFont;
    public static Typeface regularFont;
    public static Typeface thinFont;
    public static Typeface lightFont;


    public static void init(Activity activity) {
        mMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);

        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            mActionBarSize = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        } else {
            mActionBarSize = 0;
        }

        int status_bar_id = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (status_bar_id > 0) {
            mStatusBarSize = activity.getResources().getDimensionPixelSize(status_bar_id);
        } else {
            mStatusBarSize = 0;
        }

        int navigation_bar_id = activity.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (navigation_bar_id > 0) {
            mNavigationBarSize = activity.getResources().getDimensionPixelSize(navigation_bar_id);
        } else {
            mNavigationBarSize = 0;
        }

        mFontsMap = new HashMap<String, Typeface>();
        try {
            String[] assetsList = activity.getAssets().list("");
            for (String asset : assetsList) {
                if (asset.toLowerCase(Locale.getDefault()).contains(".ttf")) {
                    Typeface typeface = Typeface.createFromAsset(activity.getAssets(), asset);
                    mFontsMap.put(asset, typeface);
                }
            }
        } catch (Exception e) {
        }

        mImagesMap = new HashMap<String, String>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(activity.getResources().getAssets().open("images.txt")));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() < 4 || line.startsWith("//") || line.trim().compareToIgnoreCase("") == 0) {
                    continue;
                }
                String[] args = line.split(";");
                String id = args[0];
                String value = args[1];
                for (int i = 2; i < args.length; i++) {
                    value += ";" + args[i];
                }
                if (line.endsWith(";")) {
                    value += ";";
                }
                mImagesMap.put(id, value);
            }
        } catch (Exception e) {
        }

        boldFont 		= Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Bold.ttf");
        regularFont 	= Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Regular.ttf");
        thinFont 		= Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Thin.ttf");
        lightFont 		= Typeface.createFromAsset(activity.getAssets(), "fonts/Roboto-Light.ttf");
    }

    public static void deInit() {
        mMetrics = null;
        mFontsMap = null;
    }

    public static int getScreenDpi() {
        return mMetrics.densityDpi;
    }

    public static int getScreenWidth() {
        return mMetrics.widthPixels;
    }

    public static int getScreenHeight() {
        return mMetrics.heightPixels;
    }

    public static float getXDpi() {
        return mMetrics.xdpi;
    }

    public static float getYDpi() {
        return mMetrics.ydpi;
    }

    public static float getScreenDensity() {
        return mMetrics.density;
    }

    public static float getScaledScreenDensity() {
        return mMetrics.scaledDensity;
    }

    public static float getPXsFromDPs(float dp) {
        float px = dp * getXDpi() / DisplayMetrics.DENSITY_DEFAULT;
        return px;
    }

    public static float getDPsFromPXs(float px) {
        float dp = px / getXDpi() * DisplayMetrics.DENSITY_DEFAULT;
        return dp;
    }

    public static float getSPsFromPXs(float px) {
        float sp = px * getScaledScreenDensity();
        return sp;
    }

    public static float getPXsFromSPs(float sp) {
        float px = sp / getScaledScreenDensity();
        return px;
    }

    public static int getActionBarSize() {
        return mActionBarSize;
    }

    public static int getStatusBarSize() {
        return mStatusBarSize;
    }

    public static int getNavigationBarSize() {
        return mNavigationBarSize;
    }

    public static Bitmap convertStringToBitmap(String image) {
        byte[] imageBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    public static Bitmap getBitmap(String image) {
        String value = mImagesMap.containsKey(image) ? mImagesMap.get(image) : "";
        return convertStringToBitmap(value);
    }

    public static Bitmap getBitmapFromFilePath(String filePath) {
        return BitmapFactory.decodeFile(filePath);
    }

    public static boolean saveImageToFilePath(Bitmap bitmap, String filePath, String imageName) {
        return saveImageToFilePath(bitmap, filePath, imageName, false);
    }

    public static boolean saveImageToFilePath(Bitmap bitmap, String filePath, String imageName, boolean replace) {
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                boolean dirsCreated = dir.mkdirs();
                if (!dirsCreated) {
                    return false;
                }
            }
            FileOutputStream fos;
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            File file = new File(filePath + imageName);
            if (!file.exists()) {
                boolean createdFile = file.createNewFile();
                if (!createdFile) {
                    return false;
                }
            } else {
                if (replace) {
                    boolean deletedFile = file.delete();
                    if (deletedFile) {
                        boolean createdFile = file.createNewFile();
                        if (!createdFile) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            }
            fos = new FileOutputStream(file);
            fos.write(bytes.toByteArray());
            fos.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Bitmap createBitmapAndDrawString(int width, int height, int color, String text) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setColor(color);
        paintText.setStyle(Paint.Style.FILL);

        int size = Math.min(width, height);
        float textSize = getPXsFromSPs(size);
        paintText.setTextSize(textSize);

        Rect rectText = new Rect();
        paintText.getTextBounds(text, 0, text.length(), rectText);

        canvas.drawText(text, Math.abs(width - rectText.width()) / 2, Math.abs(height - rectText.height()) / 2, paintText);

        return bitmap;
    }

    public Bitmap resizeBitmapWithRatio(Bitmap originalBitmap, float width, float height) {
        float originalWidth = originalBitmap.getWidth(), originalHeight = originalBitmap.getHeight();
        float scale = width / originalWidth;
        float xTranslation = 0.0f, yTranslation = (height - originalHeight * scale) / 2.0f;

        Bitmap background = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);
        Matrix transformation = new Matrix();
        Paint paint = new Paint();

        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);
        paint.setFilterBitmap(true);

        canvas.drawBitmap(originalBitmap, transformation, paint);
        return background;
    }

    public Bitmap resizeBitmapHeightRatio(Bitmap originalBitmap, float width, float height) {
        float originalWidth = originalBitmap.getWidth(), originalHeight = originalBitmap.getHeight();
        float scale = height / originalHeight;
        float xTranslation = 0.0f, yTranslation = (height - originalHeight * scale) / 2.0f;

        Bitmap background = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);
        Matrix transformation = new Matrix();
        Paint paint = new Paint();

        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);
        paint.setFilterBitmap(true);

        canvas.drawBitmap(originalBitmap, transformation, paint);
        return background;
    }

    public Bitmap resizeBitmapBothRatios(Bitmap originalImage, float width, float height) {
        float originalWidth = originalImage.getWidth(), originalHeight = originalImage.getHeight();
        float scaleX = width / originalWidth, scaleY = height / originalHeight;
        float xTranslation = 0.0f, yTranslation = 0.0f;

        Bitmap background = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(background);
        Matrix transformation = new Matrix();
        Paint paint = new Paint();

        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scaleX, scaleY);
        paint.setFilterBitmap(true);

        canvas.drawBitmap(originalImage, transformation, paint);
        return background;
    }

    private void reloadFonts(Context context) {

    }


}
