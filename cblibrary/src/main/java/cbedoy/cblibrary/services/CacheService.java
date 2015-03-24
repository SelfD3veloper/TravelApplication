package cbedoy.cblibrary.services;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Carlos Bedoy  on 12/18/14.
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
            this.folderStorage = external_files_dir.getAbsolutePath() + File.separator + "bills" + File.separator + "cache" + File.separator;
        }
        else
        {
            this.folderStorage = ApplicationLoader.mainContext.getFilesDir().getAbsolutePath() + File.separator + "bills" + File.separator + "cache" + File.separator;
        }
        new File(this.folderStorage).mkdirs();
    }

    public void reloadFavorite(String sha, HashMap<String, Object> service)
    {
        ArrayList<HashMap<String, Object>> serviceList = readCacheFromSHA(sha);
        serviceList.add(service);
        writeCacheForSHA(sha, serviceList);
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

    public void removeFavorite(String SHA, HashMap<String, Object> currentService) {
        ArrayList<HashMap<String, Object>> serviceList = readCacheFromSHA(SHA);
        for(HashMap<String, Object> service : serviceList){
            int id = (Integer) service.get("id_servicio");
            int __id = (Integer) currentService.get("id_servicio");
            if(id == __id){
                serviceList.remove(service);
                break;
            }
        }
        writeCacheForSHA(SHA, serviceList);
    }

    public ArrayList<HashMap<String, Object>> getFavoritesFromSHA(String SHA)
    {
        ArrayList<HashMap<String, Object>> hashMaps = readCacheFromSHA(SHA);
        return hashMaps;
    }
}
