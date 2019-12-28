package com.tarabd.tara.Activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tarabd.tara.ApiCall.ApiClient;
import com.tarabd.tara.ApiCall.ApiInterface;
import com.tarabd.tara.PrefConfig;
import com.tarabd.tara.R;
import com.tarabd.tara.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddArchiveVideos extends AppCompatActivity {

    public static PrefConfig prefConfig;

    // video upload
    private EditText vTitle, vUrl, vDate;
    private Button vUpload, datePickerBtn;
    private ImageButton Back;

    private DatePickerDialog datePicker;

    private ProgressDialog mProgressDialog;

    public static ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_archive_videos);

        mProgressDialog = new ProgressDialog(this);

        prefConfig = new PrefConfig(this);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        vTitle = findViewById(R.id.ArchvideoTitle);
        vUrl = findViewById(R.id.ArchvideoUrl);
        vUpload = findViewById(R.id.ArchVideoupId);
        vDate = findViewById(R.id.ArchvideoDate);
        Back = findViewById(R.id.btnArchBackId);
        datePickerBtn = findViewById(R.id.datePickerId);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(AddArchiveVideos.this, OwnersActivity.class);
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

        vDate.setEnabled(false);
        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                datePicker = new DatePickerDialog(AddArchiveVideos.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
                                cldr.set(year, monthOfYear, dayOfMonth);
                                Date SelectedDate = cldr.getTime();
                                String dateString = dateFormat.format(SelectedDate);
                                vDate.setText(dateString);
//                                vDate.setText(dayOfMonth + " " + (monthOfYear + 1) + "," + year);
                            }
                        }, year, month, day);
                datePicker.show();
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
        String Date = vDate.getText().toString();
//        String currentDate = new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());

        Call<User> call = apiInterface.UploadVideo(prefConfig.readName(), Title, Url, Date, currentTime, "","");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body().getResponse().equals("ok")) {
                    Toast.makeText(AddArchiveVideos.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();

                    vTitle.setText("");
                    vUrl.setText("");
                    vDate.setText("");

                } else if (response.body().getResponse().equals("exist")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(AddArchiveVideos.this, "Video is already exist", Toast.LENGTH_SHORT).show();
                } else if (response.body().getResponse().equals("error")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(AddArchiveVideos.this, "error uploading! Check Connection", Toast.LENGTH_SHORT).show();
                } else if (response.body().getResponse().equals("empty")) {
                    mProgressDialog.dismiss();
                    Toast.makeText(AddArchiveVideos.this, "Title or Url is empty!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
