package com.tarabd.tara.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tarabd.tara.Adapters.FeatureVideoAdapter;
import com.tarabd.tara.ApiCall.ApiClient;
import com.tarabd.tara.ApiCall.ApiInterface;
import com.tarabd.tara.R;
import com.tarabd.tara.User;
import com.tarabd.tara.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

import static com.tarabd.tara.Fragments.HomeFragment.SHOP_NAME;
import static com.tarabd.tara.Fragments.HomeFragment.VIDEO_DATE;
import static com.tarabd.tara.Fragments.HomeFragment.VIDEO_TIME;
import static com.tarabd.tara.Fragments.HomeFragment.VIDEO_TITLE;
import static com.tarabd.tara.Fragments.HomeFragment.VIDEO_URL;
import static com.tarabd.tara.Fragments.HomeFragment.VIEW_COUNT;

public class VideoDetails extends AppCompatActivity implements FeatureVideoAdapter.OnItemClickListener{

    String baseUrl = "https://btlbd.xyz/myoffer/response.php";

    public static ApiInterface apiInterface;

    private Toolbar mToolbar;
    private RequestQueue mQueue;
    private RecyclerView mRcyclerView;

    private WebView mWebView;

    private ArrayList<Video> VideoList;
    private FeatureVideoAdapter featureVideoAdapter;

    private TextView vTitle, vDate, vTime, secondtile, shopName, postedOn;
    private ImageButton previous, next;
    private TextView viewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_details);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        mToolbar = findViewById(R.id.vDetailsToolbarId);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Video Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mWebView = findViewById(R.id.wViewId);
        mWebView.getSettings().setLoadWithOverviewMode(true);

        mRcyclerView = findViewById(R.id.videoDetailsRecyclerId);
        mRcyclerView.setHasFixedSize(true);
        final LinearLayoutManager slayoutManager
                = new LinearLayoutManager(VideoDetails.this, LinearLayoutManager.HORIZONTAL, false);
        mRcyclerView.setLayoutManager(slayoutManager);

        vTitle = findViewById(R.id.titleId);
        vDate = findViewById(R.id.VideoDateId);
        vTime = findViewById(R.id.VideoTimeId);
        secondtile = findViewById(R.id.secondTitleId);
        shopName = findViewById(R.id.videoShopNameId);
        postedOn = findViewById(R.id.postedOnId);
        viewText = findViewById(R.id.videoDetailsViewCountId);

        previous = findViewById(R.id.vDetailsBackId);
        next = findViewById(R.id.vDetailsNextId);

        Intent intent = getIntent();
        String ShopName = "Posted By: "+intent.getStringExtra(SHOP_NAME);
        String title = intent.getStringExtra(VIDEO_TITLE);
        String url = intent.getStringExtra(VIDEO_URL);
        String time = intent.getStringExtra(VIDEO_TIME);
        String date = intent.getStringExtra(VIDEO_DATE);
        String views = intent.getStringExtra(VIEW_COUNT);

        vTitle.setText(title);
        secondtile.setText(title);
        vDate.setText(date);
        vTime.setText(time);
        viewText.setText(views);
        postedOn.setText("Posted On "+date+" at "+time);
        shopName.setText(ShopName);
        String fblive = "<html>" +
                "<body>" +
//                "\t<h6>Facebook ur..</h6>" +
                "<iframe src='" + url + "' width=\"100%\" height=\"100%\" " +
                "style=\"border:none;overflow:hidden\" scrolling=\"no\" frameborder=\"0\" " +
                "allowTransparency=\"true\" " +
                "allowFullScreen=\"true\">" +
                "</iframe>" +
                "</body>" +
                "</html>";

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadData(fblive, "text/html", "UTF-8");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRcyclerView.getLayoutManager().scrollToPosition(slayoutManager.findLastVisibleItemPosition() + 1);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRcyclerView.getLayoutManager().scrollToPosition(slayoutManager.findFirstVisibleItemPosition() - 1);
            }
        });

        mQueue = Volley.newRequestQueue(VideoDetails.this);
        VideoList = new ArrayList<>();
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

                        String shopname = video.getString("shopname");
                        String title = video.getString("title");
                        String url = video.getString("url");
                        String date = video.getString("date");
                        String time = video.getString("time");
                        String viewCount = video.getString("view_count");
                        String thumbUrl = video.getString("thumbnail_url");

                        VideoList.add(new Video(shopname, "",title, url, date, time,viewCount,thumbUrl));
                    }

                    featureVideoAdapter = new FeatureVideoAdapter(VideoDetails.this, VideoList);
                    mRcyclerView.setAdapter(featureVideoAdapter);
                    featureVideoAdapter.setOnItemClickListener(VideoDetails.this);

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
    public void onVideoClick(int position) {
        Intent videoDetails = new Intent(VideoDetails.this, VideoDetails.class);
        Video clickedItem = VideoList.get(position);

        videoDetails.putExtra(SHOP_NAME, clickedItem.getShopname());
        videoDetails.putExtra(VIDEO_TITLE,clickedItem.getTitle());
        videoDetails.putExtra(VIDEO_URL,clickedItem.getUrl());
        videoDetails.putExtra(VIDEO_DATE,clickedItem.getDate());
        videoDetails.putExtra(VIDEO_TIME,clickedItem.getTime());
        videoDetails.putExtra(VIEW_COUNT,clickedItem.getview_count());

        Call<User> call = apiInterface.UploadVideo("","","","","",clickedItem.getview_count(),"");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                if (response.body().getResponse().equals("Update")) {
                    Toast.makeText(VideoDetails.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                }
                else if (response.body().getResponse().equals("error")) {

                    Toast.makeText(VideoDetails.this, "error updating!", Toast.LENGTH_SHORT).show();
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
