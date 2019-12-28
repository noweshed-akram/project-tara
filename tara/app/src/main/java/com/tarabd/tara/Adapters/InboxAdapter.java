package com.tarabd.tara.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tarabd.tara.Inbox;
import com.tarabd.tara.R;

import java.util.ArrayList;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ExampleViewHolder> {

    private Context mContext;
    private ArrayList<Inbox> inbox;
    private InboxAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onVideoClick(int position);
    }

    public void setOnItemClickListener(InboxAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public InboxAdapter(Context context, ArrayList<Inbox> Inbox) {
        mContext = context;
        inbox = Inbox;
    }

    @Override
    public InboxAdapter.ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.inboxitem_view, parent, false);
        return new InboxAdapter.ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(InboxAdapter.ExampleViewHolder holder, int position) {
        Inbox currentItem = inbox.get(position);

        String orderId = currentItem.getOrderId();
        String shopName = currentItem.getShopname();
        String customerName = currentItem.getCustomerName();
        String customerNumber = currentItem.getCustomerNumber();
        String customerAddress = currentItem.getCustomerAddress();
        String productCode = currentItem.getProductCode();
        String productPrice = currentItem.getProductPrice();
        String OrderDate = currentItem.getOrderDate();

        holder.msgTitle.setText(orderId);
        holder.orderDate.setText(OrderDate);
        holder.msgBody.setText("You have a new order of this product "+productCode+
                " which is priced "+productPrice + " including shipping. You have place the order from "+shopName);

    }

    @Override
    public int getItemCount() {
        return inbox.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {

        public TextView msgTitle, msgBody, orderDate;

        @SuppressLint("ClickableViewAccessibility")
        public ExampleViewHolder(View itemView) {
            super(itemView);

            msgTitle = itemView.findViewById(R.id.msgTitleId);
            msgBody = itemView.findViewById(R.id.msgBodyId);
            orderDate = itemView.findViewById(R.id.inboxOrderDateId);

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
