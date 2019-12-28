package com.tarabd.tara.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tarabd.tara.PrefConfig;
import com.tarabd.tara.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    public static PrefConfig prefConfig;

    private ImageButton Back;
    private TextView Name, Number, Address, Email;
    private CircleImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prefConfig = new PrefConfig(this);

        Back = findViewById(R.id.profileBackId);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Name = findViewById(R.id.profileNameId);
        Number = findViewById(R.id.profileNumberId);
        Address = findViewById(R.id.profileAddressId);
        Email = findViewById(R.id.profileEmailId);
        profilePic = findViewById(R.id.profilePicId);

        Picasso.get().load(prefConfig.readProfileUrl()).placeholder(R.drawable.default_pro_pic).into(profilePic);

        Name.setText(prefConfig.readName());
        Number.setText(prefConfig.readNumber());
        Address.setText("Address: " + prefConfig.readAddress());
        Email.setText("Email: " + prefConfig.readEmail());

    }
}
