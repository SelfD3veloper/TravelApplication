package cbedoy.cblibrary.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.util.HashMap;
import java.util.List;

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
public abstract class AbstractView
{
    protected HashMap<String, Object> mDataModel;
    protected List<HashMap<String, Object>> mViewModel;
    protected Context context;
    protected View view;
    protected LayoutInflater layoutInflater;

    public void setDataModel(HashMap<String, Object> dataModel) {
        this.mDataModel = dataModel;
    }

    public void setLayoutInflater(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public View getView(){
        if(view == null)
            view = init();
        return view;
    }

    public abstract View init();
    public abstract void reload();

}
