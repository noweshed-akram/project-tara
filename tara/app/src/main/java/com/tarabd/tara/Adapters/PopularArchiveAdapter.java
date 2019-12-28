package com.tarabd.tara.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tarabd.tara.R;
import com.tarabd.tara.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PopularArchiveAdapter extends RecyclerView.Adapter<PopularArchiveAdapter.ExampleViewHolder> {

    private Context mContext;
    private ArrayList<Video> videos;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onPopularVideoClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public PopularArchiveAdapter(Context context, ArrayList<Video> Video) {
        mContext = context;
        videos = Video;
    }

    @Override
    public PopularArchiveAdapter.ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.popular_archiv_item, parent, false);
        final ExampleViewHolder vHolder = new ExampleViewHolder(v);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(PopularArchiveAdapter.ExampleViewHolder holder, int position) {
        Video currentItem = videos.get(position);

        String thumbUrl = currentItem.getThumbnail_url();
        String vShopname = currentItem.getShopname();
        String vTitle = currentItem.getTitle();
        String vUrl = currentItem.getUrl();
        String vDate = currentItem.getDate();
        String vTime = currentItem.getTime();
        String vCount = currentItem.getview_count();

        holder.vTitle.setText(vTitle);
        holder.vDate.setText(vDate);
        holder.vCount.setText(vCount);
//        holder.vTime.setText(vTime);

//        Glide.with(mContext).asBitmap().load(vUrl).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.videoThumb);

        Picasso.get().load(thumbUrl).fit().centerInside().into(holder.videoThumb);

//        Glide.with(mContext).load(thumbUrl).error(R.drawable.videothumbnail).into(holder.videoThumb);

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public WebView videoView;
        public TextView vTitle, vDate, vCount;// vTime;
        public ImageView videoThumb;

        public ExampleViewHolder(View itemView) {
            super(itemView);

            videoThumb = itemView.findViewById(R.id.popularVideoThumbId);
//            shopName = itemView.findViewById(R.id.shopNameId);
            vTitle = itemView.findViewById(R.id.popularVideosTitleId);
            vDate = itemView.findViewById(R.id.popularVideosDateId);
//            vTime = itemView.findViewById(R.id.timeId);
            vCount = itemView.findViewById(R.id.popArchiveViewsId);
            videoView = itemView.findViewById(R.id.popularVideosId);
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
                            mListener.onPopularVideoClick(position);
                        }
                    }
                }
            });

        }
    }
}
