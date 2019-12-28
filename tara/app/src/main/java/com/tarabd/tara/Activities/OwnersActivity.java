package com.tarabd.tara.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;
import com.tarabd.tara.BottomNavigationViewHelper;
import com.tarabd.tara.Fragments.HomeFragment;
import com.tarabd.tara.Fragments.CollectionFragment;
import com.tarabd.tara.Fragments.InboxFragment;
import com.tarabd.tara.Fragments.StoreFragment;
import com.tarabd.tara.PrefConfig;
import com.tarabd.tara.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.tarabd.tara.Activities.SignInUp.PROFILE_IMAGE;

public class OwnersActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private CollectionFragment collectionFragment;
    private StoreFragment storeFragment;
    private HomeFragment homeFragment;
    private InboxFragment inboxFragment;

    public static PrefConfig prefConfig;

    private BottomNavigationView mBottomNavigationView;
    private FrameLayout mFrameLayout;

    private ImageView SearchBtn;
    private ImageButton HomeBtn;

    private static AppBarLayout AppBar;

    private CircleImageView ProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owners);

        prefConfig = new PrefConfig(this);

        mBottomNavigationView = findViewById(R.id.OwnersbotnavId);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mFrameLayout = findViewById(R.id.OwnersframId);
        AppBar = findViewById(R.id.OwnersmainAppbarId);

        mToolbar = findViewById(R.id.OwnerstoolbarId);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        final DrawerLayout drawer = findViewById(R.id.OwnersdrawerlayId);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        toggle.setDrawerIndicatorEnabled(false);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        storeFragment = new StoreFragment();
        collectionFragment = new CollectionFragment();
        homeFragment = new HomeFragment();
        inboxFragment = new InboxFragment();

        SearchBtn = findViewById(R.id.ownerSearchBtnId);
        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OwnersActivity.this, SearchActivity.class));
            }
        });

        HomeBtn = findViewById(R.id.ownerHomeBtnId);
        HomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ownerAppbarShow();
                setFragment(homeFragment);
                HomeFragment.hide();
            }
        });

        ProfileImage = findViewById(R.id.ownerProfileId);
        Picasso.get().load(prefConfig.readProfileUrl()).placeholder(R.drawable.default_pro_pic).into(ProfileImage);
        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OwnersActivity.this, ProfileActivity.class));
            }
        });

        setFragment(homeFragment);
        mBottomNavigationView.setSelectedItemId(R.id.home);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.home:
                        ownerAppbarShow();
                        setFragment(homeFragment);
                        return true;
                    case R.id.collection:
                        ownerAppbarShow();
                        setFragment(collectionFragment);
                        return true;
                    case R.id.store:
                        ownerAppbarShow();
                        setFragment(storeFragment);
                        return true;
                    case R.id.inbox:
                        ownerAppbarShow();
                        setFragment(inboxFragment);
                        return true;
                    case R.id.menu:
                        drawer.openDrawer(GravityCompat.START);
                        return true;
                    default:
                        return false;
                }
            }
        });

        NavigationView navigationView = findViewById(R.id.OwnersnavviewId);
        View header = navigationView.getHeaderView(0);
        TextView userName = header.findViewById(R.id.headerNameId);
        TextView userNumber = header.findViewById(R.id.headerNumberId);
        CircleImageView userPic = header.findViewById(R.id.headerPicId);

        userName.setText(prefConfig.readName());
        userNumber.setText(prefConfig.readEmail());

        Picasso.get().load(prefConfig.readProfileUrl()).placeholder(R.drawable.default_pro_pic).into(userPic);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.oProfileId) {
                    ownerAppbarShow();
                    startActivity(new Intent(OwnersActivity.this, ProfileActivity.class));
                    drawer.closeDrawers();
                }

//                if (id == R.id.oliveId) {
//                    ownerAppbarShow();
//                    Intent Live = new Intent(OwnersActivity.this, AddLiveVideos.class);
//                    startActivity(Live);
//                    finish();
//                }
//
//                if (id == R.id.oArchivId) {
//                    ownerAppbarShow();
//                    Intent Archive = new Intent(OwnersActivity.this, AddArchiveVideos.class);
//                    startActivity(Archive);
//                    finish();
//                }

                if (id == R.id.oProductId) {
                    ownerAppbarShow();
                    Intent Products = new Intent(OwnersActivity.this, AddProducts.class);
                    startActivity(Products);
                    finish();
                }

                if (id == R.id.oOrderId) {
                    ownerAppbarShow();
                    setFragment(inboxFragment);
                    drawer.closeDrawers();
                }

                if (id == R.id.oLogoutId) {
                    prefConfig.writeMerchantLoginStatus(false);
                    prefConfig.writeemail("");
                    prefConfig.writeaddress("");
                    prefConfig.writenumber("");
                    prefConfig.writename("");
                    prefConfig.writeProfileUrl("");
                    LoginManager.getInstance().logOut();
                    Intent Home = new Intent(OwnersActivity.this, MainActivity.class);
                    startActivity(Home);
                    finish();
                }

                return true;
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.OwnersframId, fragment);
        fragmentTransaction.commit();
    }

    public static void ownerAppbarHide() {
        AppBar.setVisibility(View.GONE);
    }

    public static void ownerAppbarShow() {
        AppBar.setVisibility(View.VISIBLE);
    }

}
