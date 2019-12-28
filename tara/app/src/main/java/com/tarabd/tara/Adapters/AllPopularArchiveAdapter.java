package com.tarabd.tara.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tarabd.tara.Activities.AddLiveVideos;
import com.tarabd.tara.ApiCall.ApiClient;
import com.tarabd.tara.ApiCall.ApiInterface;
import com.tarabd.tara.PrefConfig;
import com.tarabd.tara.R;
import com.tarabd.tara.User;
import com.tarabd.tara.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPopularArchiveAdapter extends RecyclerView.Adapter<AllPopularArchiveAdapter.ExampleViewHolder> {

    private Context mContext;
    private ArrayList<Video> videos;
    private OnItemClickListener mListener;
    public PrefConfig prefConfig;

    public interface OnItemClickListener {
        void AllPopularVideoClick(int position);

        void AddCollectionBtnClick(int position);
    }

    public void setOnItemClickListener(AllPopularArchiveAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public AllPopularArchiveAdapter(Context context, ArrayList<Video> Video) {
        mContext = context;
        videos = Video;
    }

    @Override
    public AllPopularArchiveAdapter.ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        prefConfig = new PrefConfig(mContext);

        v = LayoutInflater.from(mContext).inflate(R.layout.all_populararchiv_item, parent, false);
        final ExampleViewHolder vHolder = new ExampleViewHolder(v);

        return vHolder;
    }


    @Override
    public void onBindViewHolder(final AllPopularArchiveAdapter.ExampleViewHolder holder, int position) {
        Video currentItem = videos.get(position);

        final String thumbUrl = currentItem.getThumbnail_url();
        final String vShopname = currentItem.getShopname();
        final String vTitle = currentItem.getTitle();
        final String vUrl = currentItem.getUrl();
        final String vDate = currentItem.getDate();
        final String vCount = currentItem.getview_count();
        final String vTime = currentItem.getTime();

        holder.shopName.setText(vShopname);
        holder.vTitle.setText(vTitle);
        holder.vDate.setText(vDate);
        holder.vTime.setText(vTime);
        holder.viewCount.setText(vCount);

        Picasso.get().load(thumbUrl).fit().centerInside().into(holder.videoThumb);

//        holder.AddToCollection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(mContext, "Clicked"+holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
//                Call<User> call = apiInterface.VideoCollection("bdshop","","","","","","","");
//
//                call.enqueue(new Callback<User>() {
//                    @Override
//                    public void onResponse(Call<User> call, Response<User> response) {
//                        if (response.body().getResponse().equals("ok")) {
//                            Toast.makeText(mContext, "Upload Successful!", Toast.LENGTH_SHORT).show();
//
//                        } else if (response.body().getResponse().equals("exist")) {
//
//                            Toast.makeText(mContext, "Video is already exist", Toast.LENGTH_SHORT).show();
//                        }
//                        else if (response.body().getResponse().equals("error")) {
//
//                            Toast.makeText(mContext, "error uploading! Check Connection", Toast.LENGTH_SHORT).show();
//                        }else if (response.body().getResponse().equals("empty")) {
//
//                            Toast.makeText(mContext, "Title or Url is empty!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<User> call, Throwable t) {
//
//                    }
//                });
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public WebView videoView;
        public TextView vTitle, vDate, shopName, vTime, viewCount;
        public ImageView videoThumb;
        public ImageButton AddToCollection;

        public ExampleViewHolder(View itemView) {
            super(itemView);

            videoThumb = itemView.findViewById(R.id.allPopArchvThumbId);
            shopName = itemView.findViewById(R.id.allPopArchvShopId);
            vTitle = itemView.findViewById(R.id.allPopArchvTitleId);
            vDate = itemView.findViewById(R.id.allPopArchvDateId);
            vTime = itemView.findViewById(R.id.allPopArchvTimeId);
            viewCount = itemView.findViewById(R.id.popularArchiveViewCountId);
            videoView = itemView.findViewById(R.id.allPopArchvVideoId);
            videoView.getSettings().setLoadWithOverviewMode(true);
            videoView.getSettings().setUseWideViewPort(true);
            videoView.setVerticalScrollBarEnabled(false);
            videoView.setHorizontalScrollBarEnabled(false);
            AddToCollection = itemView.findViewById(R.id.addToCollectionId);

            AddToCollection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            if (prefConfig.readloginstatus()) {
                                AddToCollection.setImageResource(R.drawable.ic_favorite_red);
                            }
                            mListener.AddCollectionBtnClick(position);
                        }
                    }
                }
            });

            videoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return (event.getAction() == MotionEvent.ACTION_MOVE);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.AllPopularVideoClick(position);
                        }
                    }
                }
            });

        }
    }
}
