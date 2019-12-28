package com.tarabd.tara.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tarabd.tara.R;
import com.tarabd.tara.Activities.ShopDetails;
import com.tarabd.tara.Store;
import com.tarabd.tara.Adapters.AllStoresAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragment extends Fragment implements AllStoresAdapter.OnItemClickListener {

    public static final String SHOP_URL = "upload_url";
    public static final String SHOP_NAME = "shopname";

    View mMainView;

    private RecyclerView recyclerView;
    private AllStoresAdapter AllStoresAdapter;
    private ArrayList<Store> StoreList;

    private TextView allStore;

    private RequestQueue mQueue;

    String baseUrl = "https://btlbd.xyz/myoffer/response.php";

    private int storeCount;

    public StoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView =  inflater.inflate(R.layout.fragment_store, container, false);

        allStore = mMainView.findViewById(R.id.allStoreCountId);

        recyclerView = mMainView.findViewById(R.id.allStoreRecycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mMainView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        StoreList = new ArrayList<>();

        mQueue = Volley.newRequestQueue(mMainView.getContext());

        storeCount = 0;
        storeParsing();

        return  mMainView;
    }

    private void storeParsing() {

        JsonObjectRequest ownersRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Owners");

                    for (int i = 0; i<jsonArray.length();i++){
                        JSONObject product = jsonArray.getJSONObject(i);

                        storeCount++;

                        String shopname = product.getString("shopname");
                        String image = product.getString("upload_url");

                        StoreList.add(new Store(shopname,"","","","","",image,"","Active"));
                    }

                    allStore.setText("Store Found ("+storeCount+")");

                    AllStoresAdapter = new AllStoresAdapter(mMainView.getContext(), StoreList);
                    recyclerView.setAdapter(AllStoresAdapter);
                    AllStoresAdapter.setOnItemClickListener(StoreFragment.this);

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

    @Override
    public void onStoreClick(int position) {
        Intent shopDetails = new Intent(mMainView.getContext(), ShopDetails.class);
        Store clickedItem = StoreList.get(position);

        shopDetails.putExtra(SHOP_URL,clickedItem.getUpload_url());
        shopDetails.putExtra(SHOP_NAME,clickedItem.getShopname());

        startActivity(shopDetails);
    }
}
