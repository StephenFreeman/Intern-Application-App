package edu.freemans.internapplicationapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private ArrayList<ProductData> products;
    private ListView productList;
    private LayoutInflater layoutInflater;
    private Button searchButton;
    private TextView searchBar;
    private Icon imgFetcher;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyDatabaseHelper db = new MyDatabaseHelper(this);

        this.productList = (ListView) findViewById(R.id.productList);
        this.layoutInflater = LayoutInflater.from(this);
        this.searchBar = (TextView)this.findViewById(R.id.searchView);
        this.searchButton = (Button)this.findViewById(R.id.searchButton);
        this.imgFetcher = new Icon(this);

        this.searchButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                ZapposAPI zapposTask = new ZapposAPI(MainActivity.this);
                try {
                    String result = searchBar.getText().toString();
                    zapposTask.execute(result);
                }
                catch (Exception e)
                {
                    zapposTask.cancel(true);
                    alert (getResources().getString(R.string.no_connection));
                }

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
            }

        });

        searchBar.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT);
                return false;
            }
        });

        // Perform discount check
        check(db);
    }

        public void check(MyDatabaseHelper db){
            int counter = db.getProductCount();

            if(counter == 0){
                return;
            }

            ArrayList<String> productDiscounts = new ArrayList<String>();
            String result;

            for(int i = 0; i < counter; i++){
                result = db.getProduct(i);
                productDiscounts.add(new String(result));
            }

            new ZapposDiscountAPI(this,db).execute(productDiscounts);
            db.close();
        }


    // This function allows us to create toast alerts
    // to notify the user when they watch a product
    public void alert (String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    // Our references to our row items
    public static class MyViewHolder {
        public TextView productName, brandName;
        public Button viewButton;
        public ImageView productThumbnail;
        public ProductData product;
        public String price, discount, origPrice, imgUrl, productString, brand, productId;
    }

    public void setProducts(ArrayList<ProductData> products) {
        this.products = products;
        this.productList.setAdapter(new ProductDataAdapter(this,this.imgFetcher,this.layoutInflater, this.products));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
