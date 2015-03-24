package cbedoy.cblibrary.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cbedoy.cblibrary.services.ApplicationLoader;

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
public class ImageLoaderService
{
    private static ImageLoaderService imageLoaderService;
    private final String folderStorage;
    private final String cacheStorage;
    private final String TAG = ImageLoaderService.class.getSimpleName();

    public static ImageLoaderService getInstance(){
        if(imageLoaderService == null)
            imageLoaderService = new ImageLoaderService();
        return imageLoaderService;
    }

    public ImageLoaderService()
    {
        File external_files_dir = ApplicationLoader.mainContext.getExternalFilesDir(null);
        if(external_files_dir != null && this.isExternalStorageWritable())
        {
            this.folderStorage  = external_files_dir.getAbsolutePath() + File.separator + "cblibrary" + File.separator + "images" + File.separator;
            this.cacheStorage   = external_files_dir.getAbsolutePath() + File.separator + "cblibrary" + File.separator + "cache" + File.separator;
        }
        else
        {
            this.folderStorage  = ApplicationLoader.mainContext.getFilesDir().getAbsolutePath() + File.separator + "cblibrary" + File.separator + "images" + File.separator;
            this.cacheStorage   = ApplicationLoader.mainContext.getFilesDir().getAbsolutePath() + File.separator + "cblibrary" + File.separator + "cache" + File.separator;
        }
        new File(this.folderStorage).mkdirs();
        new File(this.cacheStorage).mkdirs();
        saveAssetsToSDCard();
        saveCacheBinaryToSDCard();
    }

    private void saveCacheBinaryToSDCard() {
        AssetManager assetManager = ApplicationLoader.mainContext.getAssets();
        try {
            String[] files = assetManager.list("cache");
            for(String filename : files)
            {
                File file = new File(this.cacheStorage + filename);
                if(!file.exists())
                {
                    String pathAsset = "cache/"+filename;
                    InputStream in = assetManager.open(pathAsset);
                    OutputStream out = new FileOutputStream(file);
                    copyFile(in, out);
                    in.close();
                    out.flush();
                    out.close();
                    Log.e(TAG, "Cache: "+ filename + " saved.");
                }
                else
                {
                    Log.e(TAG, "Cache: " + filename + " has been saved.");
                }
            }
        }
        catch (FileNotFoundException e)
        {
            Log.e(TAG, e.getMessage());
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
        }
    }


    public void downloadImagesFrom(Object images_info)
    {
        ArrayList<String> images_list = new ArrayList<String>();
        this.processObject(images_info, images_list);

        DownloadImagesAsyncTask asyncTask = new DownloadImagesAsyncTask();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, images_list);
        else
            asyncTask.execute(images_list);
    }

    private void saveAssetsToSDCard() {
        AssetManager assetManager = ApplicationLoader.mainContext.getAssets();
        try {
            String[] files = assetManager.list("images");
            for(String filename : files)
            {
                File file = new File(this.folderStorage + filename);
                if(!file.exists())
                {
                    String pathAsset = "images/"+filename;
                    InputStream in = assetManager.open(pathAsset);
                    OutputStream out = new FileOutputStream(file);
                    copyFile(in, out);
                    in.close();
                    out.flush();
                    out.close();
                    Log.e(TAG, "Image: "+ filename + " saved.");
                }
                else
                {
                    Log.e(TAG, "Cache: " + filename + " has been saved.");
                }
            }
        }
        catch (FileNotFoundException e)
        {
            Log.e(TAG, e.getMessage());
        }
        catch (IOException e)
        {
            Log.e(TAG, e.getMessage());
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public Bitmap getBitmapFromFile(String file)
    {
        Bitmap bmp = BitmapFactory.decodeFile(this.folderStorage + file);
        return bmp;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }


    private void processObject(Object images_info, ArrayList<String> images_list) {
        if (images_info == null)
            return;

        if (images_info instanceof List) {
            for (Object object : (List) images_info)
                this.processObject(object, images_list);
            return;
        }

        if (images_info instanceof Map) {
            for (Object o : ((Map) images_info).entrySet()) {
                Map.Entry pair = (Map.Entry) o;
                Object value = pair.getValue();
                this.processObject(value, images_list);
            }
            return;
        }

        if (images_info instanceof String && images_info.toString().toLowerCase(Locale.getDefault()).contains(".png")) {
            File file = new File(this.folderStorage + images_info.toString());
            if (!file.exists())
                images_list.add(images_info.toString());
            return;
        }
    }

    public void parseImagesToDownload(HashMap<String, Object> response)
    {
        downloadImagesFrom(null);
    }


    private class DownloadImagesAsyncTask extends AsyncTask<ArrayList<String>, Void, Void> {
        private int index = 0;
        protected Void doInBackground(ArrayList<String>... urls) {
            for (ArrayList<String> list : urls)
                for (String link : list)
                    this.getBitmapFromURL(link);
            return null;
        }

        private void getBitmapFromURL(String link) {
            try {
                String url_path = ApplicationLoader.urlProject + link;
                URL url = new URL(url_path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bmp   = BitmapFactory.decodeStream(input);
                this.saveImage(bmp, link);
            } catch (Exception ignored) {
            }
        }

        private void saveImage(Bitmap bmp, String link) {
            try {
                FileOutputStream fos;
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                File file = new File(folderStorage + link);
                if (!file.exists())
                    file.createNewFile();
                fos = new FileOutputStream(file);
                fos.write(bytes.toByteArray());
                fos.close();
            } catch (Exception ignored) {
            }
            Log.e("Image", "Image save "+(index++));
        }
    }

    public String getFolderStorage() {
        return folderStorage;
    }
}
