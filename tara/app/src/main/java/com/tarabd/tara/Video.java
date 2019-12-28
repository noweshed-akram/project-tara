package com.tarabd.tara;

public class Video {

    private String shopname;
    private String userNumber;

    private String title;
    private String url;
    private String date;
    private String time;
    private String view_count;
    private String thumbnail_url;

    public Video(String shopname, String userNumber,String title, String url, String date, String time,String view_count,String thumbnail_url) {
        this.shopname = shopname;
        this.userNumber = userNumber;
        this.title = title;
        this.url = url;
        this.date = date;
        this.time = time;
        this.view_count = view_count;
        this.thumbnail_url = thumbnail_url;
    }

    public String getShopname() {
        return shopname;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getview_count() {
        return view_count;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }
}
