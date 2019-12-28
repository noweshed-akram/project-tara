package com.tarabd.tara.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Handler;
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
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tarabd.tara.BottomNavigationViewHelper;
import com.tarabd.tara.Fragments.HomeFragment;
import com.tarabd.tara.Fragments.CollectionFragment;
import com.tarabd.tara.Fragments.InboxFragment;
import com.tarabd.tara.Fragments.StoreFragment;
import com.tarabd.tara.PrefConfig;
import com.tarabd.tara.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    public static PrefConfig prefConfig;

    private Toolbar mToolbar;

    private CollectionFragment collectionFragment;
    private StoreFragment storeFragment;
    private HomeFragment homeFragment;
    private InboxFragment inboxFragment;

    private ImageView searchBtn;
    private CircleImageView publicProfile;

    private ImageButton homeBtn;

    private static AppBarLayout mainAppBar;

    private BottomNavigationView mBottomNavigationView;
    private FrameLayout mFrameLayout;

    Handler handler;
    private RelativeLayout splashLayout, mainLayout;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefConfig = new PrefConfig(this);

        //keep login status

        if (prefConfig.readloginstatus()) {
            startActivity(new Intent(MainActivity.this, Customer.class));
            finish();
        } else if (prefConfig.readmerchantloginstatus()) {
            startActivity(new Intent(MainActivity.this, OwnersActivity.class));
            finish();
        }

        //end of login status

        //splash
        splashLayout = findViewById(R.id.splashLayoutId);
        mainLayout = findViewById(R.id.mainLayoutId);

        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                splashLayout.setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);
            }
        }, 2500);

        // end splash

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    getPackageName(),
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
//                messageDigest.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
//            }
//        }
//        catch (PackageManager.NameNotFoundException e) {
//
//        }
//        catch (NoSuchAlgorithmException e) {
//
//        }

        // end of key hash

        searchBtn = findViewById(R.id.homeSearchBtnId);
        mainAppBar = findViewById(R.id.mainAppbarId);
        homeBtn = findViewById(R.id.homeBtnId);

        mBottomNavigationView = findViewById(R.id.botnavId);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);

        mFrameLayout = findViewById(R.id.framId);

        mToolbar = findViewById(R.id.toolbarId);
        setSupportActionBar(mToolbar);
//        ViewCompat.setLayoutDirection(mToolbar, ViewCompat.LAYOUT_DIRECTION_RTL);
        getSupportActionBar().setTitle("");

        final DrawerLayout drawer = findViewById(R.id.drawerlayId);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.BLACK);
//        toggle.setHomeAsUpIndicator(R.drawable.ic_humberg);
        toggle.setDrawerIndicatorEnabled(false);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        storeFragment = new StoreFragment();
        collectionFragment = new CollectionFragment();
        homeFragment = new HomeFragment();
        inboxFragment = new InboxFragment();

        publicProfile = findViewById(R.id.publicProfileId);
        publicProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(MainActivity.this, SignInUp.class);
                startActivity(login);
                finish();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
//                finish();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appbarShow();
                HomeFragment.hide();
                setFragment(homeFragment);
            }
        });

        setFragment(homeFragment);
//        mBottomNavigationView.setSelectedItemId(R.id.home);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.collection:
                        appbarShow();
                        setFragment(collectionFragment);
                        return true;
                    case R.id.store:
                        appbarShow();
                        setFragment(storeFragment);
                        return true;
                    case R.id.inbox:
                        appbarShow();
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

        NavigationView navigationView = findViewById(R.id.navviewId);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.pliveId) {
                    setFragment(collectionFragment);
                    mBottomNavigationView.setSelectedItemId(R.id.collection);
                    drawer.closeDrawers();
                }

                if (id == R.id.pArchiveId) {
                    setFragment(collectionFragment);
                    mBottomNavigationView.setSelectedItemId(R.id.collection);
                    drawer.closeDrawers();
                }

                if (id == R.id.pCategoryId) {
                    //
                }

                if (id == R.id.pProductId) {
                    HomeFragment.ViewAllProduct();
                    drawer.closeDrawers();
                }

                if (id == R.id.pStoreId) {
                    setFragment(storeFragment);
                    mBottomNavigationView.setSelectedItemId(R.id.store);
                    drawer.closeDrawers();
                }

                if (id == R.id.pLoginId) {
                    Intent SignIn = new Intent(MainActivity.this, SignInUp.class);
                    startActivity(SignIn);
                    finish();
                }

                return true;
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        fragmentTransaction.replace(R.id.framId, fragment);
        fragmentTransaction.commit();
    }

    public static void appbarHide() {
        mainAppBar.setVisibility(View.GONE);
    }

    public static void appbarShow() {
        mainAppBar.setVisibility(View.VISIBLE);
    }

}