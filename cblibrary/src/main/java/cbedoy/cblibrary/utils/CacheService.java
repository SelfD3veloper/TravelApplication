package cbedoy.cblibrary.utils;

import android.os.Environment;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
public class CacheService
{

    private static CacheService instance;
    private final String folderStorage;


    public static CacheService getInstance(){
        if(instance == null)
            instance = new CacheService();
        return instance;
    }



    public CacheService()
    {
        File external_files_dir = ApplicationLoader.mainContext.getExternalFilesDir(null);
        if(external_files_dir != null && this.isExternalStorageWritable())
        {
            this.folderStorage = external_files_dir.getAbsolutePath() + File.separator + "cblibrary" + File.separator + "cache" + File.separator;
        }
        else
        {
            this.folderStorage = ApplicationLoader.mainContext.getFilesDir().getAbsolutePath() + File.separator + "cblibrary" + File.separator + "cache" + File.separator;
        }
        new File(this.folderStorage).mkdirs();
    }

    private boolean writeCacheForSHA(String sha, ArrayList<HashMap<String, Object>> serviceList)
    {
        try
        {
            String path = this.folderStorage + sha;
            File file = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(serviceList);
            objectOutputStream.close();
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    private ArrayList<HashMap<String, Object>>  readCacheFromSHA(String sha)
    {
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        try
        {
            String path = this.folderStorage + sha;
            File file = new File(path);
            FileInputStream fileInputStream  = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            data = (ArrayList<HashMap<String, Object>>) objectInputStream.readObject();
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


}
