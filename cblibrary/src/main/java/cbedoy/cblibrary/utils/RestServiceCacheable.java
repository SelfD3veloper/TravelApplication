package cbedoy.cblibrary.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import cbedoy.cblibrary.interfaces.IRestService;
import cbedoy.cblibrary.interfaces.IRestServiceCacheable;
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
public class RestServiceCacheable implements IRestServiceCacheable
{

    private final String folderStorage;
    private IRestService restService;


    public void setRestService(IRestService restService) {
        this.restService = restService;
    }

    public RestServiceCacheable()
    {
        File external_files_dir = ApplicationLoader.mainContext.getExternalFilesDir(null);
        if(external_files_dir != null && this.isExternalStorageWritable())
        {
            this.folderStorage = external_files_dir.getAbsolutePath() + File.separator + "bills" + File.separator + "cache" + File.separator;
        }
        else
        {
            this.folderStorage = ApplicationLoader.mainContext.getFilesDir().getAbsolutePath() + File.separator + "bills" + File.separator + "cache" + File.separator;
        }
        new File(this.folderStorage).mkdirs();
    }

    public boolean writeCacheForSHA(String sha, HashMap<String, Object> response)
    {
        try
        {
            String path = this.folderStorage + sha;
            File file = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(response);
            objectOutputStream.close();
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }


    public boolean exitsFile(String sha)
    {
        String path = this.folderStorage + sha;
        File file = new File(path);
        return file.exists();
    }

    public HashMap<String, Object> readCacheFromSHA(String sha)
    {
        HashMap<String, Object> data = new HashMap<String, Object>();
        try
        {
            String path = this.folderStorage + sha;
            File file = new File(path);
            FileInputStream fileInputStream  = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            data = (HashMap<String, Object>) objectInputStream.readObject();
            objectInputStream.close();
        }
        catch (Exception ignored)
        {

        }
        return data;
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public void requestFromSHA1(String url, HashMap<String, Object> parameters, IRestService.IRestCallback callback, String SHA1) {
        if(exitsFile(SHA1))
        {
            HashMap<String, Object> cacheFromSHA = readCacheFromSHA(SHA1);
            Boolean isValidCache = cacheFromSHA.containsKey("status") ? (Boolean) cacheFromSHA.get("status") : false;
            if(isValidCache)
            {
                callback.run(cacheFromSHA);
            }
        }
        restService.request(url, parameters, callback);

    }

}
