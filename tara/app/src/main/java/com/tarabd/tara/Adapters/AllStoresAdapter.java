package com.tarabd.tara.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tarabd.tara.R;
import com.tarabd.tara.Store;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllStoresAdapter extends RecyclerView.Adapter<AllStoresAdapter.ExampleViewHolder>{
    private Context mContext;
    private ArrayList<Store> stores;
    private AllStoresAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onStoreClick(int position);
    }

    public void setOnItemClickListener(AllStoresAdapter.OnItemClickListener listener){
        mListener = listener;
    }

    public AllStoresAdapter(Context context, ArrayList<Store> store) {
        mContext = context;
        stores = store;
    }

    @Override
    public AllStoresAdapter.ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.shopitem_view, parent, false);
        return new AllStoresAdapter.ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AllStoresAdapter.ExampleViewHolder holder, int position) {
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
        public CircleImageView pImageView;
        public TextView pTitle;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            pImageView = itemView.findViewById(R.id.shopImageId);
            pTitle = itemView.findViewById(R.id.shopItemNameId);

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
