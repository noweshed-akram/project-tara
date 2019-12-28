package com.tarabd.tara.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;
import com.tarabd.tara.ApiCall.ApiClient;
import com.tarabd.tara.ApiCall.ApiInterface;
import com.tarabd.tara.PrefConfig;
import com.tarabd.tara.R;
import com.tarabd.tara.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInUp extends AppCompatActivity {

//    OnLoginPerformActivityListner onLoginPerformActivityListner;
//    public interface OnLoginPerformActivityListner{
//        public void performLogin(String name);
//    }

    public static PrefConfig prefConfig;

    public static final String PROFILE_IMAGE = "profile_image";
    public static final String PROFILE_NAME = "profile_name";

    public static final String CUSTOMER_NUMBER = "phone_number";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String CUSTOMER_ADDRESS = "customer_address";

    private ProgressDialog mProgressDialog;

    private LinearLayout linearLayout1, linearLayout2, linearLayout3;
    private Button toCustomercSignup, toOwnerSignup, login, cSignup, oSignup;
    private ImageButton back;

    private EditText logMobile, logPass;
    private EditText regEmail, regName, regMobile, regPass, regAddress;

    private EditText oRegShopName, oRegName, oRegMobile, oRegPass, oRegAddress, ofburl;

    public static ApiInterface apiInterface;

    private ImageView storeImage;
    private Button Browse;
    private Button ReturnToLogin;

    private Bitmap bitmap;
    private Uri filePath;

    private static final int IMAGE_REQUEST_CODE = 77;

    private LoginButton loginButton;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_up);

        prefConfig = new PrefConfig(this);
        mProgressDialog = new ProgressDialog(this);

        linearLayout1 = findViewById(R.id.linlay1);
        linearLayout2 = findViewById(R.id.linlay2);
        linearLayout3 = findViewById(R.id.linlay3);

        back = findViewById(R.id.backId);
        login = findViewById(R.id.loginId);
        cSignup = findViewById(R.id.signupId);
        toCustomercSignup = findViewById(R.id.regbtnId);
        toOwnerSignup = findViewById(R.id.regOwnerbtnId);
        oSignup = findViewById(R.id.OwnersignupId);

        logMobile = findViewById(R.id.login_mobileId);
        logPass = findViewById(R.id.login_passId);

        //customer
        regEmail = findViewById(R.id.signup_emailId);
        regName = findViewById(R.id.signup_nameId);
        regMobile = findViewById(R.id.signup_phnId);
        regPass = findViewById(R.id.signup_passId);
        regAddress = findViewById(R.id.signup_addressId);
        ReturnToLogin = findViewById(R.id.returnToLoginId);
        ReturnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInUp.this, SignInUp.class));
                finish();
            }
        });

        //owner
        oRegShopName = findViewById(R.id.OwnerSignup_shopnameId);
        oRegMobile = findViewById(R.id.Ownersignup_phnId);
        oRegName = findViewById(R.id.OwnerNameId);
        //oRegEmail = findViewById(R.id.Own)
        ofburl = findViewById(R.id.FabpageUrlId);
        oRegAddress = findViewById(R.id.Ownersignup_addressId);
        oRegPass = findViewById(R.id.Ownersignup_passId);
        storeImage = findViewById(R.id.storeImageId);
        Browse = findViewById(R.id.browseId);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        toCustomercSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.VISIBLE);
                linearLayout3.setVisibility(View.GONE);
            }
        });

        toOwnerSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout1.setVisibility(View.GONE);
                linearLayout2.setVisibility(View.GONE);
                linearLayout3.setVisibility(View.VISIBLE);
            }
        });

        Browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent image = new Intent();
                image.setType("image/*");
                image.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(image, "Complete Action Using"), IMAGE_REQUEST_CODE);
            }
        });

        // Back to Main activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Home = new Intent(SignInUp.this, MainActivity.class);
                startActivity(Home);
                finish();
            }
        });

        //customer sign up
        cSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
            }
        });

        //owners sign up
        oSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath != null) {
                    OwnersSignup();
                } else {
                    Toast.makeText(SignInUp.this, "Choose your store image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //login for all users
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        //facebook login
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.fb_login_button);

        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    //facebook login result

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if (currentAccessToken == null) {

                oRegName.setText("");
//                oRegEmail.setText("");

            } else {
                loadUserProfile(currentAccessToken);
            }
        }
    };

    private void loadUserProfile(AccessToken accessToken) {

        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {

                    String first_name = object.getString("first_name");
                    String email = object.getString("email");
                    String id = object.getString("id");

                    // graph-video.facebook.com. host url for video uploads
//                    String Live_Video = "https://graph-video.facebook.com/" + id + "/live_videos?status=LIVE_NOW";

                    String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                    Toast.makeText(SignInUp.this, "Success", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    Intent owners = new Intent(SignInUp.this, OwnersActivity.class);
//                    owners.putExtra(PROFILE_IMAGE, image_url);

                    prefConfig.writeMerchantLoginStatus(true);
                    prefConfig.writeemail(email);
                    prefConfig.writeProfileUrl(image_url);
                    prefConfig.writename(first_name);
                    startActivity(owners);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle paramaters = new Bundle();
        paramaters.putString("fields", "first_name,email,id");
        request.setParameters(paramaters);
        request.executeAsync();

    }

    // end facebook login result

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                storeImage.setImageBitmap(bitmap);
                storeImage.setVisibility(View.VISIBLE);
                Browse.setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private String ImageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    private void OwnersSignup() {
        mProgressDialog.setTitle("Registering!");
        mProgressDialog.setMessage("Your account has been registered. Please wait...");
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);

        String shopName = oRegShopName.getText().toString();
        String contact = oRegMobile.getText().toString();
        String ownerName = oRegName.getText().toString();
        String shopAddress = oRegAddress.getText().toString();
        String fbUrl = ofburl.getText().toString();
        String Picture = ImageToString();
        String ownerPassword = oRegPass.getText().toString();

        Call<User> call = apiInterface.ownerRegistration(shopName, contact, "", ownerName, shopAddress, fbUrl, Picture, "", ownerPassword, "Active");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("success")) {
                    Toast.makeText(SignInUp.this, "Success", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    Intent Owners = new Intent(SignInUp.this, OwnersActivity.class);

                    prefConfig.writeMerchantLoginStatus(true);
                    prefConfig.writename(response.body().getShopname());
                    prefConfig.writenumber(response.body().getMobile());
                    prefConfig.writeaddress(response.body().getAddress());
                    prefConfig.writeemail(response.body().getEmail());

                    startActivity(Owners);
                    finish();

                } else if (response.body().getResponse().equals("exist")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(SignInUp.this, "Already Exist", Toast.LENGTH_SHORT).show();
                } else if (response.body().getResponse().equals("error")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(SignInUp.this, "Error", Toast.LENGTH_SHORT).show();
                } else if (response.body().getResponse().equals("empty")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(SignInUp.this, "Name or Mobile isn't filled!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void performLogin() {

        mProgressDialog.setTitle("Log In!");
        mProgressDialog.setMessage("Please wait while Log In...");
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);

        String mobile = logMobile.getText().toString();
        String password = logPass.getText().toString();

        Call<User> call = apiInterface.performUserLogin(mobile, password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("customer")) {
                    Toast.makeText(SignInUp.this, "Success", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();

                    Intent customer = new Intent(SignInUp.this, Customer.class);

                    prefConfig.writeLoginStatus(true);
                    prefConfig.writename(response.body().getUser());
                    prefConfig.writenumber(response.body().getMobile());
                    prefConfig.writeaddress(response.body().getAddress());
                    prefConfig.writeemail(response.body().getEmail());

                    startActivity(customer);
                    finish();

                    logMobile.setText("");
                    logPass.setText("");

                } else if (response.body().getResponse().equals("owners")) {
                    Toast.makeText(SignInUp.this, "Success", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    Intent Owners = new Intent(SignInUp.this, OwnersActivity.class);

                    prefConfig.writeMerchantLoginStatus(true);
                    prefConfig.writename(response.body().getShopname());
                    prefConfig.writenumber(response.body().getMobile());
                    prefConfig.writeaddress(response.body().getAddress());
                    prefConfig.writeemail(response.body().getEmail());

                    startActivity(Owners);
                    finish();

                    logMobile.setText("");
                    logPass.setText("");

                } else if (response.body().getResponse().equals("failed")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(SignInUp.this, "Failed. Try Again..", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    public void performRegistration() {

        mProgressDialog.setTitle("Registering!");
        mProgressDialog.setMessage("Your account has been registered. Please wait..");
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);

        final String name = regName.getText().toString();
        final String mobile = regMobile.getText().toString();
        String email = regEmail.getText().toString();
        final String address = regAddress.getText().toString();
        String password = regPass.getText().toString();

        Call<User> call = apiInterface.performRegistration(mobile, name, email, address, password);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("success")) {
                    Toast.makeText(SignInUp.this, "Success", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                    Intent Customer = new Intent(SignInUp.this, Customer.class);

                    prefConfig.writeLoginStatus(true);
                    prefConfig.writename(response.body().getUser());
                    prefConfig.writenumber(response.body().getMobile());
                    prefConfig.writeaddress(response.body().getAddress());
                    prefConfig.writeemail(response.body().getEmail());

                    startActivity(Customer);
                    finish();

                    regName.setText("");
                    regMobile.setText("");
                    regEmail.setText("");
                    regAddress.setText("");
                    regPass.setText("");

                } else if (response.body().getResponse().equals("exist")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(SignInUp.this, "Already Exist", Toast.LENGTH_SHORT).show();
                } else if (response.body().getResponse().equals("error")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(SignInUp.this, "Error", Toast.LENGTH_SHORT).show();
                } else if (response.body().getResponse().equals("empty")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(SignInUp.this, "Name or Mobile isn't filled!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

}
