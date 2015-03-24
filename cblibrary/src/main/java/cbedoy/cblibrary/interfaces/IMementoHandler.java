package cbedoy.cblibrary.interfaces;

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
import java.util.HashMap;

import cbedoy.cblibrary.services.Memento;

public interface IMementoHandler {

    public void clearStack();

    public Memento getTopMemento();

    public boolean popDataFor(Object owner);

    public Object getLastOwnerWithBackSupport();

    public void setStateForOwner(HashMap<String, Object> data, Object owner);

}
