package com.tarabd.tara.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarabd.tara.R;
import com.tarabd.tara.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeatureVideoAdapter extends RecyclerView.Adapter<FeatureVideoAdapter.ExampleViewHolder> {

    private Context mContext;
    private ArrayList<Video> videos;
    private FeatureVideoAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onVideoClick(int position);
    }

    public void setOnItemClickListener(FeatureVideoAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public FeatureVideoAdapter(Context context, ArrayList<Video> Video) {
        mContext = context;
        videos = Video;
    }

    @Override
    public FeatureVideoAdapter.ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.feature_video_item, parent, false);
        return new FeatureVideoAdapter.ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FeatureVideoAdapter.ExampleViewHolder holder, int position) {
        Video currentItem = videos.get(position);

        String thumbUrl = currentItem.getThumbnail_url();
        String vTitle = currentItem.getTitle();
        String vUrl = currentItem.getUrl();
        String vDate = currentItem.getDate();
        String vCount = currentItem.getview_count();

        holder.videoTitle.setText(vTitle);
        holder.videoUrl.setText(vUrl);
        holder.videoCount.setText(vCount);
//        holder.videoDate.setText(vDate);

        Picasso.get().load(thumbUrl).fit().centerInside().into(holder.videoThumb);



    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {

        public WebView videoView;
        public TextView videoTitle, videoUrl, videoCount; // videoDate;
        public ImageView videoThumb;

        @SuppressLint("ClickableViewAccessibility")
        public ExampleViewHolder(View itemView) {
            super(itemView);

            videoCount = itemView.findViewById(R.id.featureVideoCountId);
            videoThumb = itemView.findViewById(R.id.featuredVideoThumbId);
            videoTitle = itemView.findViewById(R.id.featureVideoTitleId);
            videoUrl = itemView.findViewById(R.id.featureVideoUrlId);
            videoView = itemView.findViewById(R.id.featureVideosId);
//            videoDate = itemView.findViewById(R.id.videosDateId);
            videoView.getSettings().setLoadWithOverviewMode(true);
            videoView.getSettings().setUseWideViewPort(true);
            videoView.setVerticalScrollBarEnabled(false);
            videoView.setHorizontalScrollBarEnabled(false);

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
                            mListener.onVideoClick(position);
                        }
                    }
                }
            });


        }
    }

}
