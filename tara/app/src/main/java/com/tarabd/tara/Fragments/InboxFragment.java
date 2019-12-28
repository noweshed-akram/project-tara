package com.tarabd.tara.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tarabd.tara.Adapters.InboxAdapter;
import com.tarabd.tara.Inbox;
import com.tarabd.tara.PrefConfig;
import com.tarabd.tara.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */

public class InboxFragment extends Fragment {

    public RecyclerView inboxRecycler;

    public static PrefConfig prefConfig;

    private RequestQueue mQueue;
    private InboxAdapter inboxAdapter;
    private ArrayList<Inbox> inboxArrayList;

    public ImageView inboxcIcon;
    public TextView msg;

    View mMainView;

    String baseUrl = "https://btlbd.xyz/myoffer/response.php";

    public InboxFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_inbox, container, false);
        inboxcIcon = mMainView.findViewById(R.id.inboxIconId);
        msg = mMainView.findViewById(R.id.msgId);

        prefConfig = new PrefConfig(mMainView.getContext());

        inboxRecycler = mMainView.findViewById(R.id.inboxRecyclerId);
        inboxRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mMainView.getContext(), LinearLayoutManager.VERTICAL, false);
        inboxRecycler.setLayoutManager(layoutManager);

        mQueue = Volley.newRequestQueue(mMainView.getContext());

        inboxArrayList = new ArrayList<>();

        if (prefConfig.readloginstatus() || prefConfig.readmerchantloginstatus()) {
            InboxMessages();
        }

        return mMainView;
    }

    private void InboxMessages() {

        JsonObjectRequest ownersRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Order");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject order = jsonArray.getJSONObject(i);

                        String customerNumber = order.getString("customer_number");

                        if (prefConfig.readNumber().equals(customerNumber)) {

                            inboxRecycler.setVisibility(View.VISIBLE);
                            inboxcIcon.setVisibility(View.GONE);
                            msg.setVisibility(View.GONE);

                            String OrderId = order.getString("order_id");
                            String ShopName = order.getString("shopname");
                            String ProductCode = order.getString("product_code");
                            String ProductPrice = order.getString("product_price");
                            String date = order.getString("order_date");

                            inboxArrayList.add(new Inbox(OrderId, ShopName, "", customerNumber, "", ProductCode, ProductPrice, date));

                        }

                    }

                    inboxAdapter = new InboxAdapter(mMainView.getContext(), inboxArrayList);
                    inboxRecycler.setAdapter(inboxAdapter);

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

}
