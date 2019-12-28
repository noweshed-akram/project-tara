package com.tarabd.tara.Fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tarabd.tara.Adapters.AllPopularArchiveAdapter;
import com.tarabd.tara.Adapters.PopularArchiveAdapter;
import com.tarabd.tara.PrefConfig;
import com.tarabd.tara.R;
import com.tarabd.tara.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CollectionFragment extends Fragment implements PopularArchiveAdapter.OnItemClickListener, AllPopularArchiveAdapter.OnItemClickListener {

    public static final String VIDEO_URL = "url";

    public static PrefConfig prefConfig;

    public LinearLayout linearLayout;
    public ImageView icon;
    public TextView msg;

    View mMainView;

    private RecyclerView recyclerView;
    private PopularArchiveAdapter videoAdapter;
    private AllPopularArchiveAdapter archiveAdapter;
    private ArrayList<Video> VideoList;

    private RequestQueue mQueue;

    String baseUrl = "https://btlbd.xyz/myoffer/response.php";

    public CollectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_collection, container, false);
        icon = mMainView.findViewById(R.id.collectionIconId);
        msg = mMainView.findViewById(R.id.collectionMsgId);
        linearLayout = mMainView.findViewById(R.id.collLayId);

        prefConfig = new PrefConfig(mMainView.getContext());

        recyclerView = mMainView.findViewById(R.id.allVideoRecycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mMainView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        mQueue = Volley.newRequestQueue(mMainView.getContext());

        VideoList = new ArrayList<>();

        if (prefConfig.readloginstatus() || prefConfig.readmerchantloginstatus()) {
            videoParsing();
//            icon.setVisibility(View.GONE);
//            msg.setVisibility(View.GONE);
//            linearLayout.setVisibility(View.VISIBLE);
        }

        return mMainView;

    }

    private void videoParsing() {
        JsonObjectRequest videoRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Collections");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject video = jsonArray.getJSONObject(i);

                        String userNumber = video.getString("user_number");

                        if (prefConfig.readNumber().equals(userNumber)) {

                            icon.setVisibility(View.GONE);
                            msg.setVisibility(View.GONE);
                            linearLayout.setVisibility(View.VISIBLE);

                            String shopname = video.getString("shopname");
                            String title = video.getString("title");
                            String url = video.getString("url");
                            String date = "Date: " + video.getString("date");
                            String time = "Time: " + video.getString("time");
                            String viewCount = video.getString("view_count");
                            String thumb = video.getString("thumbnail_url");

                            VideoList.add(new Video(shopname, "",title, url, date, time, viewCount, thumb));
                        }
                    }

                    archiveAdapter = new AllPopularArchiveAdapter(mMainView.getContext(), VideoList);
                    recyclerView.setAdapter(archiveAdapter);
                    archiveAdapter.setOnItemClickListener(CollectionFragment.this);

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
    public void onPopularVideoClick(int position) {
//        Intent liveDialog = new Intent(mMainView.getContext(),DialogLive.class);
        Video clickedItem = VideoList.get(position);

//        liveDialog.putExtra(VIDEO_URL,clickedItem.getUrl());
//
//        startActivity(liveDialog);
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_livevideo);
        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.copyFrom(dialog.getWindow().getAttributes());
        dialog.getWindow().setAttributes(lp);
        WebView webView = dialog.findViewById(R.id.dialogVideosId);
        webView.getSettings().setLoadWithOverviewMode(true);

//        TextView textView = dialog.findViewById(R.id.dialogUrlId);

        String url = clickedItem.getUrl();

//        textView.setText(url);

        String videos = "<html>" +
                "<body>" +
//                "\t<h6>Facebook Live..</h6>" +
                "<iframe src='" + url + "' width=\"100%\" height=\"100%\" " +
                "style=\"border:none;overflow:hidden\" scrolling=\"no\" frameborder=\"0\" " +
                "allowTransparency=\"true\" " +
                "allowFullScreen=\"true\">" +
                "</iframe>" +
                "</body>" +
                "</html>";

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(videos, "text/html", "UTF-8");

    }

    @Override
    public void AllPopularVideoClick(int position) {
        //        Intent liveDialog = new Intent(mMainView.getContext(),DialogLive.class);
        Video clickedItem = VideoList.get(position);

//        liveDialog.putExtra(VIDEO_URL,clickedItem.getUrl());
//
//        startActivity(liveDialog);
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_livevideo);
        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.copyFrom(dialog.getWindow().getAttributes());
        dialog.getWindow().setAttributes(lp);
        WebView webView = dialog.findViewById(R.id.dialogVideosId);
        webView.getSettings().setLoadWithOverviewMode(true);

//        TextView textView = dialog.findViewById(R.id.dialogUrlId);

        String url = clickedItem.getUrl();

//        textView.setText(url);

        String videos = "<html>" +
                "<body>" +
//                "\t<h6>Facebook Live..</h6>" +
                "<iframe src='" + url + "' width=\"100%\" height=\"100%\" " +
                "style=\"border:none;overflow:hidden\" scrolling=\"no\" frameborder=\"0\" " +
                "allowTransparency=\"true\" " +
                "allowFullScreen=\"true\">" +
                "</iframe>" +
                "</body>" +
                "</html>";

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(videos, "text/html", "UTF-8");

    }

    @Override
    public void AddCollectionBtnClick(int position) {

    }
}
