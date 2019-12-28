package com.tarabd.tara.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.tarabd.tara.Adapters.AllPopularArchiveAdapter;
import com.tarabd.tara.Adapters.FeatureVideoAdapter;
import com.tarabd.tara.Adapters.PopularArchiveAdapter;
import com.tarabd.tara.Adapters.ProductDetailsAdapter;
import com.tarabd.tara.Fragments.HomeFragment;
import com.tarabd.tara.R;
import com.tarabd.tara.User;
import com.tarabd.tara.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_CATEGORY;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_CODE;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_NAME;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_ORIGIN;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_PRICE;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_STATUS;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_URL;
import static com.tarabd.tara.Fragments.HomeFragment.SHOP_NAME;
import static com.tarabd.tara.Fragments.HomeFragment.VIDEO_DATE;
import static com.tarabd.tara.Fragments.HomeFragment.VIDEO_TIME;
import static com.tarabd.tara.Fragments.HomeFragment.VIDEO_TITLE;
import static com.tarabd.tara.Fragments.HomeFragment.VIDEO_URL;
import static com.tarabd.tara.Fragments.HomeFragment.VIEW_COUNT;

public class SearchActivity extends AppCompatActivity implements ProductDetailsAdapter.OnItemClickListener, PopularArchiveAdapter.OnItemClickListener {

    String responseUrl = "https://btlbd.xyz/myoffer/response.php";

    private ImageButton BackBtn;
    private EditText SearchText;
    private RecyclerView searchRecyclerView, videoRecyclerView;
    private ArrayList<User> ProductList;
    private ArrayList<Video> videoArrayList;

    private TextView ProductTitle, VideoTitle;

    private ProductDetailsAdapter productDetailsAdapter;
    private PopularArchiveAdapter popularArchiveAdapter;

    private ProgressBar progressBar;

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ProductTitle = findViewById(R.id.productResultId);
        VideoTitle = findViewById(R.id.videoResultId);

        BackBtn = findViewById(R.id.searchBackId);
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        progressBar = findViewById(R.id.progressBarId);

        SearchText = findViewById(R.id.allSearchTextId);
        SearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                progressBar.setVisibility(View.VISIBLE);
                SearchResult(editable.toString());
                VideoSearchResult(editable.toString());
            }
        });

        searchRecyclerView = findViewById(R.id.searchRecyclerProductId);
        searchRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager playoutManager = new GridLayoutManager(SearchActivity.this, 2);
        searchRecyclerView.setLayoutManager(playoutManager);

        videoRecyclerView = findViewById(R.id.searchRecyclerVideoId);
        videoRecyclerView.setHasFixedSize(true);
        LinearLayoutManager vlayoutManager
                = new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.HORIZONTAL, false);
        videoRecyclerView.setLayoutManager(vlayoutManager);

        mQueue = Volley.newRequestQueue(SearchActivity.this);

        ProductList = new ArrayList<>();
        videoArrayList = new ArrayList<>();

//        VideoSearchResult();
    }

    private void SearchResult(final String text) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, responseUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (text.isEmpty()){
                        progressBar.setVisibility(View.GONE);
                        ProductTitle.setVisibility(View.GONE);
                        ProductList.clear();
                    }

                    JSONArray jsonArray = response.getJSONArray("Product");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject product = jsonArray.getJSONObject(i);

                        String shopname = product.getString("shopname");
                        String title = product.getString("title");
                        String price = product.getString("price");
                        String image = product.getString("url");
                        String origin = product.getString("origin");
                        String code = product.getString("code");
                        String category = product.getString("category");
                        String status = product.getString("status");

                        if (!text.isEmpty() && text.equals(shopname) || text.equals(price) || text.equals(title)) {

                            ProductTitle.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            ProductList.add(new User("", "", "", shopname, code, title, price, category, "", origin, image, status));

                        }

                    }

                    productDetailsAdapter = new ProductDetailsAdapter(SearchActivity.this, ProductList);
                    searchRecyclerView.setAdapter(productDetailsAdapter);
                    productDetailsAdapter.setOnItemClickListener(SearchActivity.this);

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

        mQueue.add(request);
    }

    private void VideoSearchResult(final String searchText) {
        JsonObjectRequest videoRequest = new JsonObjectRequest(Request.Method.GET, responseUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (searchText.isEmpty()){
                        progressBar.setVisibility(View.GONE);
                        VideoTitle.setVisibility(View.GONE);
                        videoArrayList.clear();
                    }

                    JSONArray jsonArray = response.getJSONArray("Videos");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject featureVideo = jsonArray.getJSONObject(i);

                        String shopname = featureVideo.getString("shopname");
                        String title = featureVideo.getString("title");
                        String url = featureVideo.getString("url");
                        String date = featureVideo.getString("date");
                        String time = featureVideo.getString("time");
                        String viewCount = featureVideo.getString("view_count");
                        String thumbnail = featureVideo.getString("thumbnail_url");

                        if (searchText.equals(shopname)) {

                            VideoTitle.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            videoArrayList.add(new Video(shopname, "", title, url, date, time, viewCount, thumbnail));
                        }
                    }

                    popularArchiveAdapter = new PopularArchiveAdapter(SearchActivity.this, videoArrayList);
                    videoRecyclerView.setAdapter(popularArchiveAdapter);
                    popularArchiveAdapter.setOnItemClickListener(SearchActivity.this);

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

    @Override
    public void onProductClick(int position) {
        Intent productDetails = new Intent(SearchActivity.this, ProductDetails.class);
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
        Intent videoDetails = new Intent(SearchActivity.this, VideoDetails.class);
        Video clickedItem = videoArrayList.get(position);

        videoDetails.putExtra(SHOP_NAME, clickedItem.getShopname());
        videoDetails.putExtra(VIDEO_TITLE, clickedItem.getTitle());
        videoDetails.putExtra(VIDEO_URL, clickedItem.getUrl());
        videoDetails.putExtra(VIDEO_DATE, clickedItem.getDate());
        videoDetails.putExtra(VIDEO_TIME, clickedItem.getTime());
        videoDetails.putExtra(VIEW_COUNT, clickedItem.getview_count());

        startActivity(videoDetails);
    }
}
