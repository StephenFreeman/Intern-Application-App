package edu.freemans.internapplicationapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Stephen on 3/4/14.
 */
public class ProductPageActivity extends ActionBarActivity {

    String productName;
    String brandName;
    String price;
    String discount;
    String origPrice;
    String imgUrl;
    String productId;

    Boolean isNotified = false;

    private TextView nameText;
    private TextView brandText;
    private TextView priceText;
    private TextView discountText;
    private TextView origText;
    private ImageView image;
    private Button notifyButton;
    private Icon imgFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_page);

        final MyDatabaseHelper db = new MyDatabaseHelper(this);

        Intent intent = getIntent();

        this.nameText = (TextView)findViewById(R.id.productPageName);
        this.brandText = (TextView)findViewById(R.id.productPageBrand);
        this.priceText = (TextView)findViewById(R.id.productPagePrice);
        this.discountText = (TextView)findViewById(R.id.productPageDiscount);
        this.origText = (TextView)findViewById(R.id.productPageOriginalPrice);
        origText.setTextColor(Color.parseColor("#000000"));
        this.image = (ImageView)findViewById(R.id.productPageImage);
        this.notifyButton = (Button)findViewById(R.id.productPageButton);

        this.productName = intent.getStringExtra("productName");
        this.brandName = intent.getStringExtra("brandName");
        this.price = intent.getStringExtra("price");
        this.discount = intent.getStringExtra("discount");
        this.origPrice = intent.getStringExtra("origPrice");
        this.imgUrl = intent.getStringExtra("imgUrl");
        this.productId = intent.getStringExtra("productId");
        this.imgFetcher = new Icon(this);

        nameText.setText(productName);
        brandText.setText(brandName);
        priceText.setText(price);
        discountText.setText(discount);
        origText.setText(origPrice);
        // Set the image
        // See below for explanation for commenting this out
        /*
        if(imgUrl != null) {
            image.setTag(imgUrl);
            Drawable dr = imgFetcher.loadImage(image);
            if(dr != null) {
                image.setImageDrawable(dr);
            }
        } else {
            image.setImageResource(R.drawable.blank);
        }
        */
        new DownloadImageTask((ImageView) findViewById(R.id.productPageImage)).execute(imgUrl);

        // If the price and orig price are not the same, make it look nice
        if(!price.equals(origPrice)){
            origText.setPaintFlags(origText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            origText.setTextColor(Color.parseColor("#FF5200"));
        }

        // We want to check if user wanted to notify about the product already
        // This will determine what our button will say
        if(db.isStored(productId)){
            notifyButton.setText("Stop notifications for this product");
            isNotified = true;
        }
        else{
            notifyButton.setText("Notify me on discount!");
            isNotified = false;
        }

        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNotified){
                    // User has selected to be notified, so that means clicking will reverse
                    db.deleteProduct(productId);
                    isNotified = false;
                    notifyButton.setText("Notify me on discount!");
                    alert("You are no longer being notified on discounts for this item");
                }
                else{
                    // User has not selected to be notified, so implement logic for it
                    db.addProduct(productId);
                    isNotified = true;
                    notifyButton.setText("Stop notifications for this product");
                    alert("You will be notified when " + productName + " has a 20% or greater discount on your next visit!");
                }
            }
        });
    }

    public void alert (String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    // To load the image into our new activity
    // I ran into some errors overloading my icon class to deal with this
    // However I did not want to go through the effort of implementing parceable and passing an
    // object through intents so I did this instead to save me time
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
/*
    public class MyListener implements View.OnClickListener{
        private View mView;
        MyDatabaseHelper database;

        public MyListener(View v, MyDatabaseHelper db){
            mView = v;
            this.database = db;
        }

        public void onClick(View v){
            if(isNotified){
                // User has selected to be notified, so that means clicking will reverse
                database.deleteProduct(productId);
                isNotified = false;
            }
            else{
                // User has not selected to be notified, so implement logic for it
                database.addProduct(productId);
                isNotified = true;
            }
        }
    }
    */
}


