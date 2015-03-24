package cbedoy.cblibrary.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import cbedoy.cblibrary.interfaces.IBackCore;
import cbedoy.cblibrary.interfaces.IMementoHandler;
import cbedoy.cblibrary.utils.Utils;

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

public class MementoHandler implements IMementoHandler {

    private final ArrayList<Memento> mMementoStack;

    public MementoHandler() {
        mMementoStack = new ArrayList<Memento>();
    }

    @Override
    public Memento getTopMemento() {
        Memento memento;
        synchronized (mMementoStack) {
            if (mMementoStack.size() > 0) {
                memento = mMementoStack.get(mMementoStack.size() - 1);
            } else {
                memento = new Memento();
            }
        }
        return memento;
    }

    @Override
    public void setStateForOwner(HashMap<String, Object> data, Object owner) {
        if (data == null || owner == null) {
            return;
        }
        Memento memento = new Memento();
        Memento topMemento = getTopMemento();
        HashMap<String, Object> topMementoData = topMemento.getMementoData();
        HashMap<String, Object> mementoDataMerge = Utils.mergeHashMaps(data, topMementoData);
        memento.setMementoOwner(owner);
        memento.setMementoData(mementoDataMerge);
        addMemento(memento);
    }

    @Override
    public void clearStack() {
        synchronized (mMementoStack) {
            mMementoStack.clear();
        }
    }

    @Override
    public boolean popDataFor(Object owner) {
        if (owner == null) {
            return false;
        }
        if (isOwnerInTheStack(owner)) {
            synchronized (mMementoStack) {
                while (mMementoStack.size() > 0) {
                    Memento memento = mMementoStack.get(mMementoStack.size() - 1);
                    mMementoStack.remove(mMementoStack.size() - 1);
                    if (memento.getMementoOwner().equals(owner)) {
                        while (mMementoStack.size() > 0) {
                            Memento tempMemento = mMementoStack.get(mMementoStack.size() - 1);
                            if (!tempMemento.getMementoOwner().equals(owner)) {
                                return true;
                            }
                            mMementoStack.remove(mMementoStack.size() - 1);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object getLastOwnerWithBackSupport() {
        synchronized (mMementoStack) {
            ArrayList<Memento> reverseStack = (ArrayList<Memento>) mMementoStack.clone();
            Collections.reverse(reverseStack);
            for (Memento memento : reverseStack) {
                Object mementoOwner = memento.getMementoOwner();
                if (mementoOwner instanceof IBackCore) {
                    return mementoOwner;
                }

            }
        }
        return null;
    }

    private void addMemento(Memento memento) {
        if (memento != null) {
            synchronized (mMementoStack) {
                mMementoStack.add(memento);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private boolean isOwnerInTheStack(Object owner) {
        if (owner == null) {
            return false;
        }
        synchronized (mMementoStack) {
            ArrayList<Memento> reverseStack = (ArrayList<Memento>) mMementoStack.clone();
            Collections.reverse(reverseStack);
            for (Memento memento : reverseStack) {
                if (memento.getMementoOwner().equals(owner)) {
                    return true;
                }
            }
        }
        return false;
    }

}
