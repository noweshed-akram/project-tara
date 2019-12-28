package com.tarabd.tara.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tarabd.tara.ApiCall.ApiClient;
import com.tarabd.tara.ApiCall.ApiInterface;
import com.tarabd.tara.PrefConfig;
import com.tarabd.tara.R;
import com.tarabd.tara.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLiveVideos extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    // video upload
    private EditText vTitle, vUrl;
    private Button vUpload;
    private ImageButton Back;

    public static ApiInterface apiInterface;
    public static PrefConfig prefConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_live_videos);

        prefConfig = new PrefConfig(this);

        // video upload
        vTitle = findViewById(R.id.videoTitle);
        vUrl = findViewById(R.id.videoUrl);
        vUpload = findViewById(R.id.upVideoID);
        Back = findViewById(R.id.btnBackId);

        mProgressDialog = new ProgressDialog(this);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(AddLiveVideos.this,OwnersActivity.class);
                startActivity(home);
                finish();
            }
        });

        vUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadVideo();
            }
        });
    }

    private void UploadVideo() {

        mProgressDialog.setTitle("Uploading!");
        mProgressDialog.setMessage("Please wait during upload your video..");
        mProgressDialog.show();
        mProgressDialog.setCanceledOnTouchOutside(false);

        String Title = vTitle.getText().toString();
        String Url = vUrl.getText().toString();
        String currentDate = new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());

        Call<User> call = apiInterface.UploadVideo(prefConfig.readName(),Title,Url,currentDate,currentTime,"","");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("ok")) {
                    Toast.makeText(AddLiveVideos.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();

                    vTitle.setText("");
                    vUrl.setText("");

                } else if (response.body().getResponse().equals("exist")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(AddLiveVideos.this, "Video is already exist", Toast.LENGTH_SHORT).show();
                }
                else if (response.body().getResponse().equals("error")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(AddLiveVideos.this, "error uploading! Check Connection", Toast.LENGTH_SHORT).show();
                }else if (response.body().getResponse().equals("empty")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(AddLiveVideos.this, "Title or Url is empty!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
