package cbedoy.cblibrary.interfaces;
import java.lang.reflect.Method;
import java.util.HashMap;

import static cbedoy.cblibrary.interfaces.IRestService.IRestCallback;

/**
 * Created by admin on 12/18/14.
 */
public interface IRestServiceCatchable
{
    public void requestFromSHAWithHandler(String                    url,
                                          HashMap<String, Object>   parameters,
                                          IRestCallback             callback,
                                          String                    SHA1,
                                          Object                    className,
                                          Method                    method);

    public HashMap<String, Object> readCacheResponseFromSHA(String sha);

    public boolean writeCacheResponseForSHA(String sha, HashMap<String, Object> response);
}
