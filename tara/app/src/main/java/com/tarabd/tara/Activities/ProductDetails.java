package com.tarabd.tara.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tarabd.tara.Adapters.ProductAdapter;
import com.tarabd.tara.Adapters.ProductDetailsAdapter;
import com.tarabd.tara.ApiCall.ApiClient;
import com.tarabd.tara.ApiCall.ApiInterface;
import com.tarabd.tara.PrefConfig;
import com.tarabd.tara.R;
import com.tarabd.tara.User;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import static com.tarabd.tara.Activities.SignInUp.CUSTOMER_ADDRESS;
import static com.tarabd.tara.Activities.SignInUp.CUSTOMER_NAME;
import static com.tarabd.tara.Activities.SignInUp.CUSTOMER_NUMBER;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_CATEGORY;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_CODE;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_NAME;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_ORIGIN;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_PRICE;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_STATUS;
import static com.tarabd.tara.Fragments.HomeFragment.PRODUCT_URL;
import static com.tarabd.tara.Fragments.HomeFragment.SHOP_NAME;

public class ProductDetails extends AppCompatActivity implements ProductAdapter.OnItemClickListener,
        ProductDetailsAdapter.OnItemClickListener {

    public static PrefConfig prefConfig;

    String baseUrl = "https://btlbd.xyz/myoffer/response.php";
    private ImageView productImage;
    private TextView productName, productPrice, productOrigin;
    private TextView Shopname, productCode, productCategory, productStatus;
    private RecyclerView recyclerView;

    private ProgressDialog mProgressDialog;

    public static ApiInterface apiInterface;

    private ProductDetailsAdapter productDetailsAdapter;
    private ProductAdapter psAdapter;
    private ArrayList<User> ProductList;
    private RequestQueue mQueue;

    private ImageButton nextBtn, previousBtn;

    private Button BuyNow, AddCart;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        mProgressDialog = new ProgressDialog(this);
        prefConfig = new PrefConfig(this);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        productImage = findViewById(R.id.productIconId);

        productName = findViewById(R.id.productNameId);
        productPrice = findViewById(R.id.productPriceId);
        productOrigin = findViewById(R.id.productOriginId);
        Shopname = findViewById(R.id.productShopId);
        productCode = findViewById(R.id.productCodeId);
        productCategory = findViewById(R.id.productCategoryId);
        productStatus = findViewById(R.id.statusId);

        BuyNow = findViewById(R.id.orderNowId);
        AddCart = findViewById(R.id.addCartId);

//        nextBtn = findViewById(R.id.nextId);
//        previousBtn = findViewById(R.id.previousId);

        mToolbar = findViewById(R.id.pDetailsToolbarId);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.allProductRecyclerId);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager vlayoutManager
                = new LinearLayoutManager(ProductDetails.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(vlayoutManager);

        Intent intent = getIntent();
        String url = intent.getStringExtra(PRODUCT_URL);
        String shopName = intent.getStringExtra(SHOP_NAME);
        String pName = intent.getStringExtra(PRODUCT_NAME);
        String pPrice = "BDT " + intent.getStringExtra(PRODUCT_PRICE);
        String pOrigin = intent.getStringExtra(PRODUCT_ORIGIN);
        String pCode = intent.getStringExtra(PRODUCT_CODE);
        String pCategory = intent.getStringExtra(PRODUCT_CATEGORY);
        String pStatus = intent.getStringExtra(PRODUCT_STATUS);

        final String customerName = intent.getStringExtra(CUSTOMER_NAME);
        final String customerMobile = intent.getStringExtra(CUSTOMER_NUMBER);
        final String customerAddress = intent.getStringExtra(CUSTOMER_ADDRESS);

        ProductList = new ArrayList<>();
        mQueue = Volley.newRequestQueue(ProductDetails.this);

        Picasso.get().load(url).placeholder(R.drawable.ic_store).fit().centerInside().into(productImage);
        productName.setText(pName);
        productPrice.setText(pPrice);
        productOrigin.setText(pOrigin);
        Shopname.setText(shopName);
        productCategory.setText(pCategory);
        productCode.setText(pCode);
        productStatus.setText(pStatus);

        productParsing();

        BuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (prefConfig.readloginstatus()) {
                    mProgressDialog.setTitle("Order In Progress!");
                    mProgressDialog.setMessage("Please wait while your order in progress...");
                    mProgressDialog.show();
                    mProgressDialog.setCanceledOnTouchOutside(false);

                    String Shop = Shopname.getText().toString();
                    String Code = productCode.getText().toString();
                    String Price = productPrice.getText().toString();
                    String OrderId = Shopname.getText().toString() + productCode.getText().toString();

                    String orderDate = new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(new Date());
                    String orderTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());

                    Call<User> call = apiInterface.PlaceOrder(OrderId, Shop, prefConfig.readName(),
                            prefConfig.readNumber(), prefConfig.readAddress(), Code, Price, orderDate);

                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                            if (response.body().getResponse().equals("successful")) {
                                mProgressDialog.dismiss();

                                final Dialog dialog = new Dialog(ProductDetails.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.order_complete_dialog);
                                dialog.show();
                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                                        FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                                lp.copyFrom(dialog.getWindow().getAttributes());
                                dialog.getWindow().setAttributes(lp);

                                Button dialogButton = dialog.findViewById(R.id.okayId);
                                // if button is clicked, close the custom dialog
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                            } else {
                                mProgressDialog.dismiss();
                                Toast.makeText(ProductDetails.this, "Order Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
                } else {

                    View layoutView = findViewById(R.id.productDetailsLayoutId);

                    Snackbar snackbar = Snackbar
                            .make(layoutView, "Please Login to Continue!", Snackbar.LENGTH_LONG)
                            .setAction("Okay", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent SignIn = new Intent(ProductDetails.this, SignInUp.class);
                                    startActivity(SignIn);
                                    finish();
                                }
                            });
                    snackbar.setActionTextColor(Color.parseColor("#FF6E5B"));
                    snackbar.show();
                }

            }
        });

//        nextBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recyclerView.getLayoutManager().scrollToPosition(vlayoutManager.findLastVisibleItemPosition() + 1);
//            }
//        });
//
//        previousBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recyclerView.getLayoutManager().scrollToPosition(vlayoutManager.findFirstVisibleItemPosition() - 1);
//            }
//        });

        AddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ProductDetails.this, "This Feature isn't Available Now!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void productParsing() {

        JsonObjectRequest productRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("Product");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject product = jsonArray.getJSONObject(i);

                        String shopname = product.getString("shopname");
                        String title = product.getString("title");
                        String price = product.getString("price");
                        String image = product.getString("url");
                        String origin = product.getString("origin");
                        String code = product.getString("code");
                        String category = product.getString("category");
                        String status = product.getString("status");

                        ProductList.add(new User("", "", "", shopname, code, title, price, category, "", origin, image, status));
                    }

                    productDetailsAdapter = new ProductDetailsAdapter(ProductDetails.this, ProductList);
                    recyclerView.setAdapter(productDetailsAdapter);
                    productDetailsAdapter.setOnItemClickListener(ProductDetails.this);

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

        mQueue.add(productRequest);
    }

    @Override
    public void onProductClick(int position) {
        Intent productDetails = new Intent(this, ProductDetails.class);
        User clickedItem = ProductList.get(position);

        productDetails.putExtra(SHOP_NAME, clickedItem.getShopname());
        productDetails.putExtra(PRODUCT_NAME, clickedItem.getTitle());
        productDetails.putExtra(PRODUCT_URL, clickedItem.getUrl());
        productDetails.putExtra(PRODUCT_PRICE, clickedItem.getPrice());
        productDetails.putExtra(PRODUCT_ORIGIN, clickedItem.getOrigin());
        productDetails.putExtra(PRODUCT_CATEGORY, clickedItem.getCategory());
        productDetails.putExtra(PRODUCT_CODE, clickedItem.getCode());
        productDetails.putExtra(PRODUCT_STATUS, clickedItem.getStatus());

        startActivity(productDetails);
        finish();

    }
}
