package edu.freemans.internapplicationapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Stephen on 2/28/14.
 */

// Async task to use our API
// Param1: String> Our search query term
// Param2: Integer> Mainly used for debugging to see my codes
// Param3: String> The JSON String we want

public class ZapposAPI extends AsyncTask<String, Integer, String> {
    private ProgressDialog progDialog;
    private Context context;
    private MainActivity activity;
    private static final String debugTag = "Zappos API Task";

    /**
     * Construct a task
     * @param activity
     */
    public ZapposAPI(MainActivity activity) {
        super();
        this.activity = activity;
        this.context = this.activity.getApplicationContext();
    }

    // When we are loading, let the user know whats going on!
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDialog = ProgressDialog.show(this.activity, "Search", this.context.getResources().getString(R.string.search) , true, false);
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Log.d(debugTag, "Background:" + Thread.currentThread().getName());
            String result = ZapposAPIHelper.downloadFromServer(params);
            return result;
        } catch (Exception e) {
            return new String();
        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        // We will populate this to send over to our activity to populate
        ArrayList<ProductData> productData = new ArrayList<ProductData>();

        progDialog.dismiss();
        if (result.length() == 0) {
            this.activity.alert ("No searches found, try a different keyword!");
            return;
        }

        try {
            JSONObject respObj = new JSONObject(result);
            JSONArray products = respObj.getJSONArray("results");
            for(int i=0; i<products.length(); i++) {
                JSONObject product = products.getJSONObject(i);
                String productName = product.getString("productName");
                String productBrand = product.getString("brandName");
                String price = product.getString("price");
                String discount = product.getString("percentOff");
                String originalPrice = product.getString("originalPrice");
                String imageUrl = product.getString("thumbnailImageUrl");
                String productId = product.getString("productId");

                productData.add(new ProductData(productName, productBrand, imageUrl, price, discount, originalPrice, productId));
            }

        } catch (JSONException e) {

            e.printStackTrace();
        }

        this.activity.setProducts(productData);

    }
}
