package cbedoy.cblibrary.services;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cbedoy.cblibrary.business.InjectionManager;
import cbedoy.cblibrary.interfaces.IRestService;
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
public class RestService implements IRestService {

    private int mPort;
    private String mUrl;

    @Override
    public void setURL(String url) {
        mUrl = url;
    }

    @Override
    public void setPort(int port) {
        mPort = port;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void request(String url, HashMap<String, Object> parameters, IRestCallback callback) {
        HashMap<String, Object> request = new HashMap<String, Object>();
        request.put("url", url);
        request.put("callback", callback);
        request.put("parameters", parameters);

        AsyncCall call = new AsyncCall();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            call.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
        } else {
            call.execute(request);
        }
    }

    private class AsyncCall extends AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>> {

        @Override
        @SuppressWarnings("unchecked")
        protected HashMap<String, Object> doInBackground(HashMap<String, Object>... params) {
            HashMap<String, Object> request = params[0];
            String url = request.get("url").toString();
            HashMap<String, Object> parameters = (HashMap<String, Object>) request.get("parameters");

            if (parameters == null) {
                parameters = new HashMap<String, Object>();
            }

            HttpResponse httpResponse;
            HttpUriRequest httpUriRequest;
            HttpClient defaultHttpClient = new DefaultHttpClient();
            HashMap<String, Object> response;

            try {
                if (InjectionManager.getInstance().isProduction())
                {
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    for (Map.Entry<String, Object> entry : parameters.entrySet())
                    {
                        NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
                        nameValuePairs.add(pair);
                    }
                    httpUriRequest = new HttpPost(mUrl + mPort + url);
                    UrlEncodedFormEntity body = new UrlEncodedFormEntity(nameValuePairs);
                    ((HttpPost) httpUriRequest).setEntity(body);
                }
                else
                {
                    String query = Utils.mapToUrlDjangoString(url, parameters);
                    httpUriRequest = new HttpGet(mUrl +":"+ mPort + (query.length() > 0 ? ("" + query) : ""));
                    Log.e("Request", mUrl +":"+ mPort  + (query.length() > 0 ? ("" + query) : ""));
                }

                httpResponse = defaultHttpClient.execute(httpUriRequest);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                for (String line; (line = bufferedReader.readLine()) != null; )
                {
                    builder.append(line);
                }
                JSONTokener jsonTokener = new JSONTokener(builder.toString());
                JSONArray jsonArray = new JSONArray(jsonTokener);
                if(jsonArray.length()>1)
                {
                    HashMap<String, Object> objects = new HashMap<String, Object>();
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        objects.put("row_"+(i+1), Utils.toMap(jsonArray.getJSONObject(i)));
                    }
                    response = objects;
                }
                else
                {
                    response = (HashMap<String, Object>) Utils.toMap(jsonArray.getJSONObject(0));
                }
            } catch (UnsupportedEncodingException uee) {
                response = new HashMap<String, Object>();
                response.put("status", false);
                response.put("error", "char_encoding");
                response.put("message", "Character Conversion Unavailable");
            } catch (ClientProtocolException cpe) {
                response = new HashMap<String, Object>();
                response.put("status", false);
                response.put("error", "http_protocol");
                response.put("message", "HTTP Error Protocol");
            } catch (IOException ioe) {
                response = new HashMap<String, Object>();
                response.put("status", false);
                response.put("error", "io_exception");
                response.put("message", "Connection Un Available");
            } catch (JSONException jsone) {
                response = new HashMap<String, Object>();
                response.put("status", false);
                response.put("error", "json_exception");
                response.put("message", "Incorrect JSON Format");
            }

            HashMap<String, Object> result = new HashMap<String, Object>();
            result.put("callback", request.get("callback"));
            result.put("response", response);
            return result;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void onPostExecute(HashMap<String, Object> result) {
            IRestCallback callback = (IRestCallback) result.get("callback");
            HashMap<String, Object> response = (HashMap<String, Object>) result.get("response");
            callback.run(response);
            super.onPostExecute(result);
        }

    }
}
