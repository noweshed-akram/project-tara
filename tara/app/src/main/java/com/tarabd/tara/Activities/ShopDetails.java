package com.tarabd.tara.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tarabd.tara.Adapters.PopularArchiveAdapter;
import com.tarabd.tara.Adapters.ProductDetailsAdapter;
import com.tarabd.tara.Adapters.FeatureVideoAdapter;
import com.tarabd.tara.ApiCall.ApiClient;
import com.tarabd.tara.ApiCall.ApiInterface;
import com.tarabd.tara.R;
import com.tarabd.tara.User;
import com.tarabd.tara.Video;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_CATEGORY;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_CODE;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_NAME;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_ORIGIN;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_PRICE;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_STATUS;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_URL;
import static com.tarabd.tara.Fragments.HomeFragment.SHOP_NAME;
import static com.tarabd.tara.Fragments.HomeFragment.SHOP_URL;
import static com.tarabd.tara.Fragments.HomeFragment.VIDEO_DATE;
import static com.tarabd.tara.Fragments.HomeFragment.VIDEO_TIME;
import static com.tarabd.tara.Fragments.HomeFragment.VIDEO_TITLE;
import static com.tarabd.tara.Fragments.HomeFragment.VIDEO_URL;
import static com.tarabd.tara.Fragments.HomeFragment.VIEW_COUNT;

public class ShopDetails extends AppCompatActivity implements PopularArchiveAdapter.OnItemClickListener,
        ProductDetailsAdapter.OnItemClickListener {

    private Toolbar mToolbar;

    private CircleImageView shopIcon;
    private RecyclerView productRecyclerView, videoRecyclerView;
    private Button productLoad, videoLoad;

    private TextView shopName, shopProduct, shopVideos;
    private TextView allProduct, allVideos;

    String baseUrl = "https://btlbd.xyz/myoffer/response.php";
    public static ApiInterface apiInterface;

    private ArrayList<User> ProductList;
    private ArrayList<Video> VideoList;

    private ProductDetailsAdapter productDetailsAdapter;
    private PopularArchiveAdapter popularArchiveAdapter;

    private RequestQueue mQueue;
    private int videoCount = 0, productCount = 0;

    String ShopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_details);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        mToolbar = findViewById(R.id.sDetailsToolbarId);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Shop Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        shopIcon = findViewById(R.id.dShopIconId);
        shopName = findViewById(R.id.dShopNameId);
//        shopProduct = findViewById(R.id.dProductCountId);
//        shopVideos = findViewById(R.id.dVideosId);

        allProduct = findViewById(R.id.dAllProductId);
        allVideos = findViewById(R.id.dAllVideoId);

        productRecyclerView = findViewById(R.id.allProductsRecyId);
        productRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ShopDetails.this, 2);
        productRecyclerView.setLayoutManager(layoutManager);

        videoRecyclerView = findViewById(R.id.allVideosRecyId);
        videoRecyclerView.setHasFixedSize(true);
        LinearLayoutManager vlayoutManager
                = new LinearLayoutManager(ShopDetails.this, LinearLayoutManager.HORIZONTAL, false);
        videoRecyclerView.setLayoutManager(vlayoutManager);

//        productLoad = findViewById(R.id.dProductLoadId);
//        videoLoad = findViewById(R.id.dVideoLoadId);

        //shop details
        Intent intent = getIntent();
        String ShopIcon = intent.getStringExtra(SHOP_URL);
        ShopName = intent.getStringExtra(SHOP_NAME);

        shopName.setText(ShopName);
        Picasso.get().load(ShopIcon).placeholder(R.drawable.ic_store).fit().centerInside().into(shopIcon);

        mQueue = Volley.newRequestQueue(ShopDetails.this);

        ProductList = new ArrayList<>();
        VideoList = new ArrayList<>();

        productParsing();
        videoParsing();
    }

    private void videoParsing() {
        JsonObjectRequest videoRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Videos");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject video = jsonArray.getJSONObject(i);

                        String shop = video.getString("shopname");
                        String title = video.getString("title");
                        String url = video.getString("url");
                        String date = video.getString("date");
                        String time = video.getString("time");
                        String viewCount = video.getString("view_count");
                        String thumb = video.getString("thumbnail_url");

                        if (ShopName.equals(shop)) {
                            videoCount++;
                            VideoList.add(new Video(shop, "", title, url, date, time, viewCount, thumb));
                        }
                    }

                    allVideos.setText("Video Gallery (" + videoCount + ")");

                    popularArchiveAdapter = new PopularArchiveAdapter(ShopDetails.this, VideoList);
                    videoRecyclerView.setAdapter(popularArchiveAdapter);
                    popularArchiveAdapter.setOnItemClickListener(ShopDetails.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(videoRequest);
    }

    private void productParsing() {

        JsonObjectRequest productRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Product");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject product = jsonArray.getJSONObject(i);

                        String code = product.getString("code");
                        String shop = product.getString("shopname");
                        String title = product.getString("title");
                        String price = product.getString("price");
                        String image = product.getString("url");
                        String origin = product.getString("origin");
                        String category = product.getString("category");
                        String status = product.getString("status");

                        if (ShopName.equals(shop)) {
                            productCount++;
                            ProductList.add(new User("", "", "", shop, code, title, price, category, "", origin, image, status));
                        }
                    }

                    allProduct.setText("Available Products (" + productCount + ")");

                    productDetailsAdapter = new ProductDetailsAdapter(ShopDetails.this, ProductList);
                    productRecyclerView.setAdapter(productDetailsAdapter);
                    productDetailsAdapter.setOnItemClickListener(ShopDetails.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(productRequest);
    }

    @Override
    public void onProductClick(int position) {
        Intent productDetails = new Intent(ShopDetails.this, ProductDetails.class);
        User clickedItem = ProductList.get(position);

        productDetails.putExtra(SHOP_NAME, clickedItem.getShopname());
        productDetails.putExtra(PRODUCT_NAME, clickedItem.getTitle());
        productDetails.putExtra(PRODUCT_URL, clickedItem.getUrl());
        productDetails.putExtra(PRODUCT_PRICE, clickedItem.getPrice());
        productDetails.putExtra(PRODUCT_ORIGIN, clickedItem.getOrigin());
        productDetails.putExtra(PRODUCT_CATEGORY, clickedItem.getCategory());
        productDetails.putExtra(PRODUCT_CODE, clickedItem.getCode());
        productDetails.putExtra(PRODUCT_STATUS, clickedItem.getStatus());

        startActivity(productDetails);
    }

    @Override
    public void onPopularVideoClick(int position) {
        Intent videoDetails = new Intent(ShopDetails.this, VideoDetails.class);
        Video clickedItem = VideoList.get(position);

        videoDetails.putExtra(SHOP_NAME, clickedItem.getShopname());
        videoDetails.putExtra(VIDEO_TITLE, clickedItem.getTitle());
        videoDetails.putExtra(VIDEO_URL, clickedItem.getUrl());
        videoDetails.putExtra(VIDEO_DATE, clickedItem.getDate());
        videoDetails.putExtra(VIDEO_TIME, clickedItem.getTime());
        videoDetails.putExtra(VIEW_COUNT, clickedItem.getview_count());

        Call<User> call = apiInterface.UploadVideo("", "", "", "", "", clickedItem.getview_count(), "");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                if (response.body().getResponse().equals("Update")) {
                    Toast.makeText(ShopDetails.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                } else if (response.body().getResponse().equals("error")) {

                    Toast.makeText(ShopDetails.this, "error updating!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        startActivity(videoDetails);
        finish();
    }
}
