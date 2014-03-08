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
 * Created by Stephen on 3/8/14.
 */
public class ZapposDiscountAPI extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {
    private ProgressDialog progDialog;
    private Context context;
    private MainActivity activity;
    private static final String debugTag = "Zappos Discount Check";
    public MyDatabaseHelper db;

    /***
     * This class will perform the logic to find if items are on discount
     * It uses ZapposAPIHelper
     */
    public ZapposDiscountAPI(MainActivity activity, MyDatabaseHelper database){
        super();
        this.activity = activity;
        this.context = this.activity.getApplicationContext();
        this.db = database;
    }

    public String removePerc(String str) {

        if (str.length() > 0) {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }

    //When loading, let the user know its finding discounts from before
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progDialog = ProgressDialog.show(this.activity, "Search", "Checking discounted prices!", true, false);
    }


    protected ArrayList<String> doInBackground(ArrayList<String>... params) {
        try{
            Log.d(debugTag, "Background:" + Thread.currentThread().getName());
            ArrayList<String> passed = params[0];
            ArrayList<String> discountObjs = new ArrayList<String>();
            int arraySize = passed.size();
            String url;
            String result;

            for(int i = 0; i < arraySize; i++){
                url = passed.get(i);
                result = ZapposAPIHelper.downloadFromServer(url);
                discountObjs.add(new String(result));
            }

            return discountObjs;
        }catch(Exception e){
            return new ArrayList<String>();
        }
    }

    protected void onPostExecute(ArrayList<String> result) {
        ArrayList<ProductData> productData = new ArrayList<ProductData>();

        progDialog.dismiss();
        if(result.size()==0){
            this.activity.alert("No products have a discount of at least 20% today :(");
            return;
        }
        int counter = result.size();

        try{
            for(int i = 0; i < counter; i++){
                String jsonObj = result.get(i);
                JSONObject respObj = new JSONObject(jsonObj);
                JSONArray products = respObj.getJSONArray("results");
                JSONObject product = products.getJSONObject(0);
                String discount = product.getString("percentOff");
                String check = removePerc(discount);
                int priceCheck = Integer.parseInt(check);

                // Make sure the discount is 20 or above
                if(priceCheck >= 20){
                String productName = product.getString("productName");
                String productBrand = product.getString("brandName");
                String price = product.getString("price");
                String originalPrice = product.getString("originalPrice");
                String imageUrl = product.getString("thumbnailImageUrl");
                String productId = product.getString("productId");

                productData.add(new ProductData(productName, productBrand, imageUrl, price, discount, originalPrice, productId));
                }

            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        this.activity.setProducts(productData);
    }
}
