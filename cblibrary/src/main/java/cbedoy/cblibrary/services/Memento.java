package cbedoy.cblibrary.services;

import java.util.HashMap;

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
public class Memento
{
    private Object mMementoOwner;
    private HashMap<String, Object> mMementoData;

    public Memento() {
        mMementoData = new HashMap<String, Object>();
        mMementoOwner = new Object();
    }

    public Object getMementoOwner() {
        return mMementoOwner;
    }

    public void setMementoOwner(Object mementoOwner) {
        mMementoOwner = mementoOwner;
    }

    public HashMap<String, Object> getMementoData() {
        return mMementoData;
    }

    public void setMementoData(HashMap<String, Object> mementoData) {
        mMementoData = mementoData;
    }
}
