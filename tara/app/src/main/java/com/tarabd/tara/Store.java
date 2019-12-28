package com.tarabd.tara;

import com.google.gson.annotations.SerializedName;

public class Store {

    private String shopname;
    private String mobile;
    private String name;
    private String email;
    private String shopaddress;
    private String fburl;
    private String upload_url;
    @SerializedName("package")
    private String pPackage;
    private String status;

    public String getUpload_url() {
        return upload_url;
    }

    public Store(String shopname, String mobile, String name, String email, String shopaddress, String fburl, String upload_url, String pPackage, String status) {
        this.shopname = shopname;
        this.mobile = mobile;
        this.name = name;
        this.email = email;
        this.shopaddress = shopaddress;
        this.fburl = fburl;
        this.upload_url = upload_url;
        this.pPackage = pPackage;
        this.status = status;
    }

    public String getShopname() {
        return shopname;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getShopaddress() {
        return shopaddress;
    }

    public String getFburl() {
        return fburl;
    }

    public String getpPackage() {
        return pPackage;
    }

    public String getStatus() {
        return status;
    }

}
