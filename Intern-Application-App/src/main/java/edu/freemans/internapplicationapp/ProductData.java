package edu.freemans.internapplicationapp;

import java.io.Serializable;

/**
 * Created by Stephen on 2/28/14.
 */

public class ProductData {

    private String productName;
    private String productBrand;
    private String imageUrl;
    private String price;
    private String discount;
    private String originalPrice;
    private String productId;

    public ProductData(String productName, String productBrand, String imageUrl, String price, String discount, String originalPrice, String productId){
        super();
        this.productName = productName;
        this.productBrand = productBrand;
        this.imageUrl = imageUrl;
        this.price = price;
        this.discount = discount;
        this.originalPrice = originalPrice;
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
