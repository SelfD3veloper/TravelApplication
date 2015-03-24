package cbedoy.cblibrary.interfaces;

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
public interface IRestServiceCacheable
{
    public boolean writeCacheForSHA(String sha, HashMap<String, Object> response);
    public HashMap<String, Object> readCacheFromSHA(String sha);
}
