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

public interface IRestService {

    public void setPort(int port);

    public void setURL(String url);

    public void request(String url, HashMap<String, Object> parameters, IRestCallback callback);

    public interface IRestCallback {
        public void run(HashMap<String, Object> response);
    }

}