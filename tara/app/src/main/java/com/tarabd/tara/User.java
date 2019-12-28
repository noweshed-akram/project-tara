package com.tarabd.tara;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("response")
    private String Response;
    @SerializedName("name")
    private String User;

    private String email;

    private String address;
    private String mobile;

    // products
    private String shopname;
    private String code;
    private String title;
    private String price;
    private String category;
    private String negotiable;
    private String origin;
    private String url;
    private String status;

    public User(String email,String address, String mobile, String shopname, String code, String title, String price,
                String category, String negotiable, String origin,
                String url, String status) {

        this.email = email;
        this.address = address;
        this.mobile = mobile;
        this.shopname = shopname;
        this.code = code;
        this.title = title;
        this.price = price;
        this.category = category;
        this.negotiable = negotiable;
        this.origin = origin;
        this.url = url;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public String getResponse() {
        return Response;
    }

    public String getUser() {
        return User;
    }

    public String getShopname() {
        return shopname;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getNegotiable() {
        return negotiable;
    }

    public String getOrigin() {
        return origin;
    }

    public String getUrl() {
        return url;
    }

    public String getStatus() {
        return status;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile() {
        return mobile;
    }
}
