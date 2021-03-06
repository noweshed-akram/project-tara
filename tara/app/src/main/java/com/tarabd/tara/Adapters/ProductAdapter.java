package com.tarabd.tara.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarabd.tara.R;
import com.tarabd.tara.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ExampleViewHolder>{

    private Context mContext;
    private ArrayList<User> products;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onProductClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public ProductAdapter(Context context, ArrayList<User> product) {
        mContext = context;
        products = product;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.productitem_view, parent, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        User currentItem = products.get(position);

        String imageUrl = currentItem.getUrl();
        String title = currentItem.getTitle();
        String price = currentItem.getPrice();

        holder.pTitle.setText(title);
        holder.pPrice.setText(price);
        Picasso.get().load(imageUrl).fit().centerInside().into(holder.pImageView);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView pImageView;
        public TextView pTitle,pPrice;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            pImageView = itemView.findViewById(R.id.psImageId);
            pTitle = itemView.findViewById(R.id.psNameId);
            pPrice = itemView.findViewById(R.id.psPriceId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener!=null){
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION){
                            mListener.onProductClick(position);
                        }
                    }
                }
            });

        }
    }

}
