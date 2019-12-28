package com.tarabd.tara.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.tarabd.tara.Activities.Customer;
import com.tarabd.tara.Activities.MainActivity;
import com.tarabd.tara.Activities.OwnersActivity;
import com.tarabd.tara.Activities.SearchActivity;
import com.tarabd.tara.Activities.SignInUp;
import com.tarabd.tara.Adapters.AllPopularArchiveAdapter;
import com.tarabd.tara.Adapters.AllStoresAdapter;
import com.tarabd.tara.Adapters.PopularArchiveAdapter;
import com.tarabd.tara.Adapters.ProductAdapter;
import com.tarabd.tara.Activities.ProductDetails;
import com.tarabd.tara.Adapters.ProductDetailsAdapter;
import com.tarabd.tara.ApiCall.ApiClient;
import com.tarabd.tara.ApiCall.ApiInterface;
import com.tarabd.tara.PrefConfig;
import com.tarabd.tara.R;
import com.tarabd.tara.Activities.ShopDetails;
import com.tarabd.tara.Store;
import com.tarabd.tara.SwipeController;
import com.tarabd.tara.User;
import com.tarabd.tara.Video;
import com.tarabd.tara.Adapters.FeatureVideoAdapter;
import com.tarabd.tara.Activities.VideoDetails;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment implements AllStoresAdapter.OnItemClickListener,
        ProductAdapter.OnItemClickListener, FeatureVideoAdapter.OnItemClickListener,
        ProductDetailsAdapter.OnItemClickListener, PopularArchiveAdapter.OnItemClickListener,
        AllPopularArchiveAdapter.OnItemClickListener {

    private static int VIDEO_REQUEST = 101;

    public static ApiInterface apiInterface;

    public static PrefConfig prefConfig;

    public static final String SHOP_URL = "upload_url";
    public static final String SHOP_NAME = "shopname";

    public static final String PRODUCT_URL = "url";
    public static final String PRODUCT_NAME = "title";
    public static final String PRODUCT_PRICE = "price";
    public static final String PRODUCT_ORIGIN = "origin";
    public static final String PRODUCT_CATEGORY = "category";
    public static final String PRODUCT_CODE = "code";
    public static final String PRODUCT_STATUS = "status";

    public static final String VIDEO_TITLE = "title";
    public static final String VIDEO_URL = "url";
    public static final String VIDEO_DATE = "date";
    public static final String VIDEO_TIME = "time";
    public static final String VIEW_COUNT = "0";

    View mMainView;

    SwipeController swipeController = null;

    private RecyclerView productRecyclerView;
    private RecyclerView sRecyclerView;
    private RecyclerView vRecyclerView;
    private static RecyclerView allVideoRecycler;
    private RecyclerView popuArchRecycler;
    private static RecyclerView allProductRecycler;

    private LinearLayout FilterLayout;

    private FeatureVideoAdapter featureVideoAdapter;
    private ProductDetailsAdapter productDetailsAdapter;
    private PopularArchiveAdapter popularArchiveAdapter;
    private AllPopularArchiveAdapter allPopularArchiveAdapter;
    private AllStoresAdapter allStoresAdapter;

    private static LinearLayout MainLayout;
    private static LinearLayout AllVideosLayout;

    private ArrayList<User> ProductList;
    private ArrayList<Store> StoreList;
    private ArrayList<Video> VideoList;

    private TextView ArchiveViewAll, backToHome, productViewAll, storeViewAll;
    private ImageView searchBtn;
    private static ImageView FilterBtn;

    private static TextView ArchiveTitle;
    private static TextView TotalContTitle;
    private TextView videoView;
    private int ProductCount = 0;
    private int VideoCount = 0;

    private CrystalRangeSeekbar rangeSeekbar;
    private TextView Minimum, Maximum;

    private CallbackManager callbackManager;

    private RequestQueue mQueue;

    private WebView mWebView;

    private LinearLayout GoLiveBtn;

    private StoreFragment storeFragment;

    String baseUrl = "https://btlbd.xyz/myoffer/response.php";

    String VideoUrl = "https://www.facebook.com/plugins/video.php?href=https%3A%2F%2Fwww.facebook.com%2FDadBodGaming00%2Fvideos%2F480191479427409%2F&show_text=0&width=560";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_home, container, false);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        prefConfig = new PrefConfig(mMainView.getContext());

        //layout
        MainLayout = mMainView.findViewById(R.id.homeMainLayId);
        AllVideosLayout = mMainView.findViewById(R.id.allVideosLayId);

        ArchiveTitle = mMainView.findViewById(R.id.ArchiveTitleId);
        productViewAll = mMainView.findViewById(R.id.allProductSeeBtn);
        backToHome = mMainView.findViewById(R.id.backToHomeId);
        searchBtn = mMainView.findViewById(R.id.allArchivsrchBtnId);
        TotalContTitle = mMainView.findViewById(R.id.totalCountId);
        videoView = mMainView.findViewById(R.id.VideoViewId);

        mWebView = mMainView.findViewById(R.id.wView);
        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setUseWideViewPort(true);

//        webVideo();

        Minimum = mMainView.findViewById(R.id.minAmountId);
        Maximum = mMainView.findViewById(R.id.maxAmountId);
        rangeSeekbar = mMainView.findViewById(R.id.rangeSeekbar);

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                Minimum.setText("BDT. " + minValue);
                Maximum.setText("BDT. " + maxValue);
            }
        });

        popuArchRecycler = mMainView.findViewById(R.id.popularArchiveRecyclerId);
        popuArchRecycler.setHasFixedSize(true);
        LinearLayoutManager populayoutManager
                = new LinearLayoutManager(mMainView.getContext(), LinearLayoutManager.HORIZONTAL, false);
//        RecyclerView.LayoutManager playoutManager = new GridLayoutManager(mMainView.getContext(), 3);
        popuArchRecycler.setLayoutManager(populayoutManager);

        productRecyclerView = mMainView.findViewById(R.id.productRecycler_view);
        productRecyclerView.setHasFixedSize(true);
        LinearLayoutManager playoutManager
                = new LinearLayoutManager(mMainView.getContext(), LinearLayoutManager.HORIZONTAL, false);
//        RecyclerView.LayoutManager playoutManager = new GridLayoutManager(mMainView.getContext(), 3);
        productRecyclerView.setLayoutManager(playoutManager);

        sRecyclerView = mMainView.findViewById(R.id.storeRecycler_view);
        sRecyclerView.setHasFixedSize(true);
        LinearLayoutManager slayoutManager
                = new LinearLayoutManager(mMainView.getContext(), LinearLayoutManager.VERTICAL, false);
        sRecyclerView.setLayoutManager(slayoutManager);

        vRecyclerView = mMainView.findViewById(R.id.featuredVideoRecycler_view);
        vRecyclerView.setHasFixedSize(true);
        LinearLayoutManager vlayoutManager
                = new LinearLayoutManager(mMainView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        vRecyclerView.setLayoutManager(vlayoutManager);

        allProductRecycler = mMainView.findViewById(R.id.viewAllProductId);
        allProductRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager allProlayoutManager = new GridLayoutManager(mMainView.getContext(), 2);
        allProductRecycler.setLayoutManager(allProlayoutManager);

        allVideoRecycler = mMainView.findViewById(R.id.viewAllVedioId);
        allVideoRecycler.setHasFixedSize(true);
        LinearLayoutManager videolayoutManager
                = new LinearLayoutManager(mMainView.getContext(), LinearLayoutManager.VERTICAL, false);
        allVideoRecycler.setLayoutManager(videolayoutManager);

        //swipe

//        swipeController = new SwipeController(new SwipeControllerActions() {
//            @Override
//            public void onRightClicked(int position) {
//                allPopularArchiveAdapter.notifyItemRangeChanged(position, allPopularArchiveAdapter.getItemCount());
//            }
//        });
//
//        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
//        itemTouchhelper.attachToRecyclerView(allVideoRecycler);
//
//        allVideoRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//                swipeController.onDraw(c);
//            }
//        });

        //end swipe

        ArchiveViewAll = mMainView.findViewById(R.id.ArchiveViewAllId);

        ArchiveViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainLayout.setVisibility(View.GONE);
                allVideoRecycler.setVisibility(View.VISIBLE);
                allProductRecycler.setVisibility(View.GONE);

                if (prefConfig.readloginstatus()) {
                    Customer.customerAppbarHide();
                } else if (prefConfig.readmerchantloginstatus()) {
                    OwnersActivity.ownerAppbarHide();
                } else {
                    MainActivity.appbarHide();
                }

                ArchiveTitle.setText("Popular Archive");
                AllVideosLayout.setVisibility(View.VISIBLE);

                FilterBtn.setVisibility(View.VISIBLE);
                TotalContTitle.setVisibility(View.VISIBLE);
            }
        });

        productViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                ViewAllProduct();

                MainLayout.setVisibility(View.GONE);
                allVideoRecycler.setVisibility(View.GONE);
                allProductRecycler.setVisibility(View.VISIBLE);
                if (prefConfig.readloginstatus()) {
                    Customer.customerAppbarHide();
                } else if (prefConfig.readmerchantloginstatus()) {
                    OwnersActivity.ownerAppbarHide();
                } else {
                    MainActivity.appbarHide();
                }
                AllVideosLayout.setVisibility(View.VISIBLE);
                ArchiveTitle.setText("Product Collection");

                FilterBtn.setVisibility(View.VISIBLE);
                TotalContTitle.setVisibility(View.VISIBLE);

            }
        });

        storeFragment = new StoreFragment();

        storeViewAll = mMainView.findViewById(R.id.allStoreSeeBtnId);
        storeViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainLayout.setVisibility(View.GONE);

                if (prefConfig.readloginstatus()) {
                    getFragmentManager().beginTransaction().add(R.id.customerframId, new StoreFragment()).commit();
                } else if (prefConfig.readmerchantloginstatus()) {
                    getFragmentManager().beginTransaction().add(R.id.OwnersframId, new StoreFragment()).commit();
                } else {
                    getFragmentManager().beginTransaction().add(R.id.framId, new StoreFragment()).commit();
                }
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mMainView.getContext(), SearchActivity.class));
            }
        });

        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prefConfig.readloginstatus()) {
                    Customer.customerAppbarShow();
                } else if (prefConfig.readmerchantloginstatus()) {
                    OwnersActivity.ownerAppbarShow();
                } else {
                    MainActivity.appbarShow();
                }
                MainLayout.setVisibility(View.VISIBLE);
                FilterLayout.setVisibility(View.GONE);
                AllVideosLayout.setVisibility(View.GONE);
            }
        });

        // filter

        FilterBtn = mMainView.findViewById(R.id.filterbtnId);
        FilterLayout = mMainView.findViewById(R.id.filterLayoutId);

        FilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TotalContTitle.setVisibility(View.GONE);
                ArchiveTitle.setText("Customized Search");
                FilterLayout.setVisibility(View.VISIBLE);
                allVideoRecycler.setVisibility(View.GONE);
                FilterBtn.setVisibility(View.GONE);
                allProductRecycler.setVisibility(View.GONE);
            }
        });

        callbackManager = CallbackManager.Factory.create();
        GoLiveBtn = mMainView.findViewById(R.id.goLiveBtnId);

//        live now

        GoLiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mMainView.getContext(), "Go to facebook Live", Toast.LENGTH_SHORT).show();
            }
        });

//        end live

        mQueue = Volley.newRequestQueue(mMainView.getContext());

        ProductList = new ArrayList<>();
        StoreList = new ArrayList<>();
        VideoList = new ArrayList<>();

        productParsing();
        storeParsing();
        VideoParsing();

        return mMainView;
    }

    public static void ViewAllProduct() {
        MainLayout.setVisibility(View.GONE);
        allVideoRecycler.setVisibility(View.GONE);
        allProductRecycler.setVisibility(View.VISIBLE);
        MainActivity.appbarHide();
//                Customer.customerAppbarHide();
        AllVideosLayout.setVisibility(View.VISIBLE);
        ArchiveTitle.setText("Product Collection");

        FilterBtn.setVisibility(View.VISIBLE);
        TotalContTitle.setVisibility(View.VISIBLE);
    }

    private void VideoParsing() {
        JsonObjectRequest videoRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Videos");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject featureVideo = jsonArray.getJSONObject(i);
                        VideoCount++;

                        String shopname = featureVideo.getString("shopname");
                        String title = featureVideo.getString("title");
                        String url = featureVideo.getString("url");
                        String date = featureVideo.getString("date");
                        String time = featureVideo.getString("time");
                        String viewCount = featureVideo.getString("view_count");
                        String thumbnail = featureVideo.getString("thumbnail_url");

                        if (i == 0) {
                            webVideo(url);
                            videoView.setText(viewCount);
                        }

                        VideoList.add(new Video(shopname, "", title, url, date, time, viewCount, thumbnail));
                    }

                    TotalContTitle.setText("Total " + VideoCount + " Videos");

                    featureVideoAdapter = new FeatureVideoAdapter(mMainView.getContext(), VideoList);
                    vRecyclerView.setAdapter(featureVideoAdapter);
                    featureVideoAdapter.setOnItemClickListener(HomeFragment.this);

                    popularArchiveAdapter = new PopularArchiveAdapter(mMainView.getContext(), VideoList);
                    popuArchRecycler.setAdapter(popularArchiveAdapter);
                    popularArchiveAdapter.setOnItemClickListener(HomeFragment.this);

                    allPopularArchiveAdapter = new AllPopularArchiveAdapter(mMainView.getContext(), VideoList);
                    allVideoRecycler.setAdapter(allPopularArchiveAdapter);
                    allPopularArchiveAdapter.setOnItemClickListener(HomeFragment.this);

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

        VideoCount = 0;
        mQueue.add(videoRequest);
    }

    private void storeParsing() {

        JsonObjectRequest ownersRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Owners");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject product = jsonArray.getJSONObject(i);

                        String shopname = product.getString("shopname");
                        String image = product.getString("upload_url");

                        StoreList.add(new Store(shopname, "", "", "", "", "", image, "", "Active"));
                    }

                    allStoresAdapter = new AllStoresAdapter(mMainView.getContext(), StoreList);
                    sRecyclerView.setAdapter(allStoresAdapter);
                    allStoresAdapter.setOnItemClickListener(HomeFragment.this);

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

        mQueue.add(ownersRequest);
    }

    private void productParsing() {

        JsonObjectRequest productRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, null, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Product");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        ProductCount++;
                        JSONObject product = jsonArray.getJSONObject(i);

                        String shopname = product.getString("shopname");
                        String title = product.getString("title");
                        String price = product.getString("price");
                        String image = product.getString("url");
                        String origin = product.getString("origin");
                        String code = product.getString("code");
                        String category = product.getString("category");
                        String status = product.getString("status");

                        ProductList.add(new User("", "", "", shopname, code, title, price, category, "", origin, image, status));
                    }

                    TotalContTitle.setText("Total " + ProductCount + " Item");

                    productDetailsAdapter = new ProductDetailsAdapter(mMainView.getContext(), ProductList);
                    productRecyclerView.setAdapter(productDetailsAdapter);
                    productDetailsAdapter.setOnItemClickListener(HomeFragment.this);

                    allProductRecycler.setAdapter(productDetailsAdapter);

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

        ProductCount = 0;
        mQueue.add(productRequest);
    }

    private void webVideo(String TrendVideoUrl) {

        ///........

    }

    @Override
    public void onProductClick(int position) {
        Intent productDetails = new Intent(mMainView.getContext(), ProductDetails.class);
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
    public void onStoreClick(int position) {
        Intent shopDetails = new Intent(mMainView.getContext(), ShopDetails.class);
        Store clickedItem = StoreList.get(position);

        shopDetails.putExtra(SHOP_URL, clickedItem.getUpload_url());
        shopDetails.putExtra(SHOP_NAME, clickedItem.getShopname());

        startActivity(shopDetails);
    }

    @Override
    public void onVideoClick(int position) {
        Intent videoDetails = new Intent(mMainView.getContext(), VideoDetails.class);
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
                    Toast.makeText(mMainView.getContext(), "Update Successful!", Toast.LENGTH_SHORT).show();
                } else if (response.body().getResponse().equals("error")) {

                    Toast.makeText(mMainView.getContext(), "error updating!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        startActivity(videoDetails);
    }

    @Override
    public void onPopularVideoClick(int position) {
        Intent videoDetails = new Intent(mMainView.getContext(), VideoDetails.class);
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
                    Toast.makeText(mMainView.getContext(), "Update Successful!", Toast.LENGTH_SHORT).show();
                } else if (response.body().getResponse().equals("error")) {

                    Toast.makeText(mMainView.getContext(), "error updating!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        startActivity(videoDetails);
    }

    public static void hide() {
        MainActivity.appbarShow();
        MainLayout.setVisibility(View.VISIBLE);
        AllVideosLayout.setVisibility(View.GONE);
    }

    @Override
    public void AllPopularVideoClick(int position) {
        Intent videoDetails = new Intent(mMainView.getContext(), VideoDetails.class);
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
                    Toast.makeText(mMainView.getContext(), "Update Successful!", Toast.LENGTH_SHORT).show();
                } else if (response.body().getResponse().equals("error")) {

                    Toast.makeText(mMainView.getContext(), "error updating!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        startActivity(videoDetails);
    }

    @Override
    public void AddCollectionBtnClick(int position) {

        Video clickedItem = VideoList.get(position);

        if (prefConfig.readloginstatus()) {

            Call<User> call = apiInterface.VideoCollection(clickedItem.getShopname(), clickedItem.getTitle(), prefConfig.readNumber(), clickedItem.getUrl(),
                    clickedItem.getDate(), clickedItem.getTime(), clickedItem.getview_count(), clickedItem.getThumbnail_url());

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                    if (response.body().getResponse().equals("ok")) {
                        Toast.makeText(mMainView.getContext(), "Save Successful!", Toast.LENGTH_SHORT).show();

                    } else if (response.body().getResponse().equals("exist")) {

                        Toast.makeText(mMainView.getContext(), "Video is already exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

        } else {

            Snackbar snackbar = Snackbar
                    .make(mMainView, "Please Login to Continue!", Snackbar.LENGTH_LONG)
                    .setAction("Okay", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent SignIn = new Intent(mMainView.getContext(), SignInUp.class);
                            startActivity(SignIn);
                            getActivity().finish();
                        }
                    });
            snackbar.setActionTextColor(Color.parseColor("#FF6E5B"));
            snackbar.show();
        }

    }
}
