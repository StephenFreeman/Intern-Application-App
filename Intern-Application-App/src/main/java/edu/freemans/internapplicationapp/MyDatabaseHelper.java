package edu.freemans.internapplicationapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Stephen on 3/5/14.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "discountManager";

    // Products table name
    private static final String TABLE_PRODUCTS = "products";

    // Products Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_PRODUCT_ID = "productId";

    public MyDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DISCOUNTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PRODUCT_ID + " TEXT" + ")";

        db.execSQL(CREATE_DISCOUNTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

        // Create tables again
        onCreate(db);
    }

    // Add products
    public void addProduct(String productId){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, productId);

        //Insert the row
        db.insert(TABLE_PRODUCTS, null, values);
        //db.close(); // Close connection to database
    }

    public String getProduct(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String productId;
        Cursor cursor = db.rawQuery("SELECT * FROM products", null);

        if(cursor != null && cursor.getCount()>0){
            if(id == 0){
                cursor.moveToFirst();
                productId = cursor.getString(1);
                return productId;
            }

            cursor.moveToFirst();
            for(int i = 0; i < id; i++){
                cursor.moveToNext();
            }

        }
        // Get out product id from our table
        productId = cursor.getString(1);
        cursor.close();
        return productId;
    }

    //Delete a single row
    public void deleteProduct(String productId){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, KEY_PRODUCT_ID + " = ?", new String[] {String.valueOf(productId)});
        db.close();
    }

    // This checks if our product is already stored in the database
    public Boolean isStored(String productId){
        String query = "SELECT * FROM " +  TABLE_PRODUCTS +" WHERE "+ KEY_PRODUCT_ID + " = " + productId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                cursor.moveToNext();
                return true;
            }

        }
        return false;
    }

    public int getProductCount() {
        String countQuery = "SELECT productId FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.moveToFirst();
        int cnt = cursor.getCount();
        cursor.close();

        return cnt;
    }

}
