package com.tarabd.tara.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarabd.tara.R;
import com.tarabd.tara.Store;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ExampleViewHolder>{
    private Context mContext;
    private ArrayList<Store> stores;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onStoreClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public StoreAdapter(Context context, ArrayList<Store> store) {
        mContext = context;
        stores = store;
    }

    @Override
    public StoreAdapter.ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.productitem_view, parent, false);
        return new StoreAdapter.ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StoreAdapter.ExampleViewHolder holder, int position) {
        Store currentItem = stores.get(position);

        String imageUrl = currentItem.getUpload_url();
        String shopname = currentItem.getShopname();

        holder.pTitle.setText(shopname);
        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_store).fit().centerInside().into(holder.pImageView);
    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView pImageView;
        public TextView pTitle,pPrice;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            pImageView = itemView.findViewById(R.id.psImageId);
            pTitle = itemView.findViewById(R.id.psNameId);
            pPrice = itemView.findViewById(R.id.psPriceId);

            pPrice.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener!=null){
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            mListener.onStoreClick(position);
                        }
                    }
                }
            });
        }
    }
}
