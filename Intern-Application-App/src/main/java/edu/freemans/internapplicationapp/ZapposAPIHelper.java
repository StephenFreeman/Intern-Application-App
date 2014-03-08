package edu.freemans.internapplicationapp;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Stephen on 2/28/14.
 */
public class ZapposAPIHelper {
    // If they key does not work, replace it here
    private static final String API_KEY = "12c3302e49b9b40ab8a222d7cf79a69ad11ffd78";
    private static final String ZapposAPIProductUrl =
            "http://api.zappos.com/Search?term=";
    private static final int HTTP_STATUS_OK = 200;
    private static byte[] buff = new byte[1024];
    private static final String logTag = "Zappos API Helper";

    public static class ApiException extends Exception {
        private static final long serialVersionUID = 1L;

        public ApiException (String msg)
        {
            super (msg);
        }

        public ApiException (String msg, Throwable thr)
        {
            super (msg, thr);
        }
    }

    /**
     * This function receives our search results from Zappos' search API functionality
     * @param params search strings
     * @return Array of json strings returned by the API.
     */
    protected static synchronized String downloadFromServer (String... params)
            throws ApiException
    {
        String retval = null;
        String product = params[0];

        // Our API request to Zappos
        String url = ZapposAPIProductUrl + product +"&key=" + API_KEY;

        Log.d(logTag, "Fetching " + url);

        // create an http client and a request object to establish our connection
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        try {

            // execute the request
            HttpResponse response = client.execute(request);
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != HTTP_STATUS_OK) {
                // handle error here
                throw new ApiException("Invalid response from Zappos" +
                        status.toString());
            }

            // process the content.
            HttpEntity entity = response.getEntity();
            InputStream ist = entity.getContent();
            ByteArrayOutputStream content = new ByteArrayOutputStream();

            int readCount = 0;
            while ((readCount = ist.read(buff)) != -1) {
                content.write(buff, 0, readCount);
            }
            retval = new String (content.toByteArray());

        } catch (Exception e) {
            throw new ApiException("Error connecting to the server " +
                    e.getMessage(), e);
        }

        return retval;
    }
}
