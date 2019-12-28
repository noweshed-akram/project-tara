package com.tarabd.tara.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.tarabd.tara.ApiCall.ApiClient;
import com.tarabd.tara.ApiCall.ApiInterface;
import com.tarabd.tara.PrefConfig;
import com.tarabd.tara.R;
import com.tarabd.tara.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProducts extends AppCompatActivity {

    private Spinner origin, category;
    private ImageButton back;

    private EditText pCode, pTitle, pPrice;
    private CheckBox negotiate;
    private Bitmap bitmap;
    private Uri filePath;
    private ImageView prdouctImage;
    private Button browse, submit;

    private ProgressDialog mProgressDialog;
    public static ApiInterface apiInterface;

    public static PrefConfig prefConfig;

    private static final int IMAGE_REQUEST_CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        mProgressDialog = new ProgressDialog(this);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        prefConfig = new PrefConfig(this);

        origin = findViewById(R.id.originId);
        category = findViewById(R.id.categoryId);
        back = findViewById(R.id.backBtnId);
        prdouctImage = findViewById(R.id.imageId);
        pCode = findViewById(R.id.pCodeId);
        pTitle = findViewById(R.id.pTitleId);
        pPrice = findViewById(R.id.pPriceId);
        negotiate = findViewById(R.id.negotiateCheckId);

        browse = findViewById(R.id.browseBtnId);
        submit = findViewById(R.id.submitId);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(AddProducts.this, OwnersActivity.class);
                startActivity(home);
                finish();
            }
        });

        //set the spinners adapter to the previously created one.
        String[] Countries = new String[]{"Bangladesh", "India", "Pakistan", "China", "USA", "UK"};
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, Countries);
        origin.setAdapter(countryAdapter);
//        countryAdapter.add("Choose country");

        String[] categoryItems = new String[]{"Electronic", "Life Style", "Home Appliance"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, categoryItems);
        category.setAdapter(categoryAdapter);
//        categoryAdapter.add("Choose product category");


        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent image = new Intent();
                image.setType("image/*");
                image.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(image, "Complete Action Using"), IMAGE_REQUEST_CODE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filePath != null) {
                    addProduct();
                } else {
                    Toast.makeText(AddProducts.this, "Choose a product image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                prdouctImage.setImageBitmap(bitmap);
                prdouctImage.setVisibility(View.VISIBLE);
                browse.setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String ImageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }

    private void addProduct() {
        mProgressDialog.setTitle("Add Product!");
        mProgressDialog.setMessage("Please wait during your product go live..");
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);

        String Code = pCode.getText().toString();
        String Title = pTitle.getText().toString();
        String Price = "৳" + pPrice.getText().toString();
        String Category = category.getSelectedItem().toString();
        String Origin = origin.getSelectedItem().toString();
        String Picture = ImageToString();
        Boolean Negotiate = negotiate.isChecked();

        Call<User> call = apiInterface.AddProducts(prefConfig.readName(), Code, Title, Price, Category, Origin, Negotiate, Picture, "Available");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("ok")) {
                    Toast.makeText(AddProducts.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();

                    pCode.setText("");
                    pTitle.setText("");
                    pPrice.setText("");
                    prdouctImage.setVisibility(View.GONE);
                    browse.setEnabled(true);

                } else if (response.body().getResponse().equals("exist")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(AddProducts.this, "Product Code is already exist", Toast.LENGTH_SHORT).show();
                } else if (response.body().getResponse().equals("error")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(AddProducts.this, "error uploading! Check Connection", Toast.LENGTH_SHORT).show();
                } else if (response.body().getResponse().equals("empty")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(AddProducts.this, "Title or Code is empty!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
