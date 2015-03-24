package cbedoy.cblibrary.services;

/**
 * Created by Carlos Bedoy on 10/9/14.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionService
{

    private Context context;

    private static ConnectionService _instance;
    public static ConnectionService getInstance(Context context) {
        if(_instance == null)
            _instance = new ConnectionService(context);
        return _instance;
    }

    public ConnectionService(Context context) {
        this.context = context;
    }

    public boolean haveInternetAccess()
    {
        ConnectivityManager connectivity_manager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivity_manager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
