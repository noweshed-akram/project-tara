package com.tarabd.tara.ApiCall;

import com.tarabd.tara.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("register.php")
    Call<User> performRegistration(@Query("mobile") String Mobile, @Query("name") String Name,
                                   @Query("email") String Email, @Query("address") String Address,
                                   @Query("password") String Password);

    @FormUrlEncoded
    @POST("ownerregister.php")
    Call<User> ownerRegistration(@Field("shopname") String Shopname,@Field("mobile") String Mobile,
                                 @Field("email") String email, @Field("name") String name,
                                 @Field("shopaddress") String ShopAddress, @Field("fburl") String fburl,
                                 @Field("picture") String Picture, @Field("package") String Package,
                                 @Field("password") String Password, @Field("status") String Status);

    @GET("login.php")
    Call<User> performUserLogin(@Query("mobile") String Mobile,@Query("password") String Password);

    @FormUrlEncoded
    @POST("videoupload.php")
    Call<User> UploadVideo(@Field("shopname") String Shopname,@Field("title") String Title,
                           @Field("url") String Url,@Field("date") String Date,
                           @Field("time") String Time,@Field("view_count") String ViewCount,
                           @Field("picture") String Thumbnail);

    @FormUrlEncoded
    @POST("VideoCollections.php")
    Call<User> VideoCollection(@Field("shopname") String Shopname,@Field("title") String Title,
                               @Field("user_number") String UserNumber, @Field("url") String Url,
                               @Field("date") String Date, @Field("time") String Time,
                               @Field("view_count") String ViewCount, @Field("thumbnail_url") String Thumbnail);

    @FormUrlEncoded
    @POST("addproducts.php")
    Call<User> AddProducts(@Field("shopname") String Shopname,@Field("code") String Code,
                           @Field("title")String Title, @Field("price")String Price,
                           @Field("category")String Category, @Field("origin")String Origin,
                           @Field("negotiable")Boolean Negotiable, @Field("picture")String Picture,
                           @Field("status")String Status);

    @FormUrlEncoded
    @POST("order.php")
    Call<User> PlaceOrder(@Field("order_id") String OrderId,@Field("shopname") String Shopname,
                          @Field("customer_name") String CustomerName,@Field("customer_number") String CustomerNum,
                          @Field("customer_address") String CustomerAddr, @Field("product_code") String ProductCode,
                          @Field("product_price") String ProductPrice, @Field("order_date") String OrderDate);
}
