package edu.freemans.internapplicationapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.freemans.internapplicationapp.MainActivity;
import edu.freemans.internapplicationapp.MainActivity.MyViewHolder;

import java.util.ArrayList;

/**
 * Created by Stephen on 2/28/14.
 */
public class ProductDataAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private static final String debugTag = "ProductDataAdapter";
    private MainActivity activity;
    private Icon imgFetcher;
    private LayoutInflater layoutInflater;
    private ArrayList<ProductData> products;

    public ProductDataAdapter(MainActivity act, Icon i, LayoutInflater l, ArrayList<ProductData> data)
    {
        this.activity = act;
        this.layoutInflater = l;
        this.products = data;
        this.imgFetcher = i;
    }

    public class MyListener implements View.OnClickListener{
        private View mView;

        String productName_temp;
        String brandName_temp;
        String price_temp;
        String discount_temp;
        String origPrice_temp;
        String imgUrl_temp;
        String productId_temp;
        Context context;

        public MyListener(View v, String product, String brand, String price, String discount, String origPrice, String imgUrl, String productId){
            mView = v;
            this.productName_temp = product;
            this.brandName_temp = brand;
            this.price_temp = price;
            this.discount_temp = discount;
            this.origPrice_temp = origPrice;
            this.imgUrl_temp = imgUrl;
            this.productId_temp = productId;
        }

        public void onClick(View v){
           // MyViewHolder holder = (MyViewHolder) v.getTag();

            Intent intent = new Intent(v.getContext(),ProductPageActivity.class);
            intent.putExtra("productName", productName_temp);
            intent.putExtra("brandName", brandName_temp);
            intent.putExtra("price", price_temp);
            intent.putExtra("discount", discount_temp);
            intent.putExtra("origPrice", origPrice_temp);
            intent.putExtra("imgUrl", imgUrl_temp);
            intent.putExtra("productId", productId_temp);
            v.getContext().startActivity(intent);
        }
    }

    @Override
    public int getCount(){
        return this.products.size();
    }

    @Override
    public boolean areAllItemsEnabled ()
    {
        return true;
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    public View getView(int pos, View convertView, ViewGroup parent){
        MainActivity.MyViewHolder holder;
        if (convertView == null){
            convertView = layoutInflater.inflate (R.layout.product_row, parent, false);
            holder = new MainActivity.MyViewHolder();
            holder.productName = (TextView) convertView.findViewById(R.id.productName);
            holder.brandName = (TextView) convertView.findViewById(R.id.brandName);
            holder.productThumbnail = (ImageView) convertView.findViewById(R.id.productImage);
            holder.viewButton = (Button) convertView.findViewById(R.id.productButton);
            holder.viewButton.setTag(holder);
            convertView.setTag(holder);
        }
        else{
            holder = (MyViewHolder) convertView.getTag();
        }

        //convertView.setOnClickListener(this);

        ProductData product = products.get(pos);
        holder.product = product;
        holder.productName.setText(product.getProductName());
        holder.brandName.setText(product.getProductBrand());

        holder.productString = product.getProductName().toString();
        holder.brand = product.getProductBrand().toString();
        holder.price = product.getPrice().toString();
        holder.discount = product.getDiscount().toString();
        holder.origPrice = product.getOriginalPrice().toString();
        holder.productId = product.getProductId().toString();
        holder.imgUrl = product.getImageUrl().toString();
        if(holder.imgUrl != null) {
            holder.productThumbnail.setTag(product.getImageUrl());
            Drawable dr = imgFetcher.loadImage(this, holder.productThumbnail);
            if(dr != null) {
                holder.productThumbnail.setImageDrawable(dr);
            }
        } else {
            holder.productThumbnail.setImageResource(R.drawable.blank);
        }

        View.OnClickListener listener = new MyListener(holder.viewButton, holder.productString, holder.brand, holder.price, holder.discount, holder.origPrice, holder.imgUrl, holder.productId);
        holder.viewButton.setOnClickListener(listener);
        return convertView;
    }

    public void onClick(View v){

    }

}
