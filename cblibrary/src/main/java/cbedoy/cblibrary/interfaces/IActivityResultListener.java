package cbedoy.cblibrary.interfaces;

import android.content.Intent;

public interface IActivityResultListener {

    public int getRequestCode();

    public void onActivityResult(int resultCode, Intent intent);

}
