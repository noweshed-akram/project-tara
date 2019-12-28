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

import static com.tarabd.tara.Activities.SignInUp.CUSTOMER_ADDRESS;
import static com.tarabd.tara.Activities.SignInUp.CUSTOMER_NAME;
import static com.tarabd.tara.Activities.SignInUp.CUSTOMER_NUMBER;
import static com.tarabd.tara.Activities.SignInUp.PROFILE_IMAGE;
import static com.tarabd.tara.Activities.SignInUp.PROFILE_NAME;

public class Customer extends AppCompatActivity {

    private Toolbar cToolbar;

    public static PrefConfig prefConfig;

    private CollectionFragment cCollectionFragment;
    private StoreFragment cstoreFragment;
    private HomeFragment chomeFragment;
    private InboxFragment inboxFragment;

    private BottomNavigationView cBottomNavigationView;
    private FrameLayout cFrameLayout;

    private ImageView SearchBtn, HomeBtn;

    private static AppBarLayout customerMainAppBar;

    private CircleImageView Profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        prefConfig = new PrefConfig(this);

        cBottomNavigationView = findViewById(R.id.customerbotnavId);
        BottomNavigationViewHelper.disableShiftMode(cBottomNavigationView);
        cFrameLayout = findViewById(R.id.customerframId);

        cToolbar = findViewById(R.id.customertoolbarId);
        setSupportActionBar(cToolbar);
        getSupportActionBar().setTitle("");

        final DrawerLayout drawer = findViewById(R.id.customerdrawerlayId);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, cToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(Color.WHITE);
        toggle.setDrawerIndicatorEnabled(false);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Intent intent = getIntent();
        String ROFILE_PIC = intent.getStringExtra(PROFILE_IMAGE);
        String User_name = intent.getStringExtra(PROFILE_NAME);
        String Customer_Name = intent.getStringExtra(CUSTOMER_NAME);
        String Customer_mobile = intent.getStringExtra(CUSTOMER_NUMBER);
        String Customer_address = intent.getStringExtra(CUSTOMER_ADDRESS);
//        String Pages_List = intent.getStringExtra(PAGES_LIST);

        Profile_image = findViewById(R.id.userProfileId);
        Picasso.get().load(ROFILE_PIC).placeholder(R.drawable.default_pro_pic).into(Profile_image);

        Profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Customer.this,ProfileActivity.class));
            }
        });

        SearchBtn = findViewById(R.id.customerSearchBtnId);
        SearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Customer.this, SearchActivity.class));
//                finish();
            }
        });

        customerMainAppBar = findViewById(R.id.customermainAppbarId);

        HomeBtn = findViewById(R.id.customerHomeBtn);

        cstoreFragment = new StoreFragment();
        cCollectionFragment = new CollectionFragment();
        chomeFragment = new HomeFragment();
        inboxFragment = new InboxFragment();

        HomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerAppbarShow();
                setFragment(chomeFragment);
                HomeFragment.hide();
            }
        });

        setFragment(chomeFragment);
        cBottomNavigationView.setSelectedItemId(R.id.home);
        cBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.collection:
                        customerAppbarShow();
                        setFragment(cCollectionFragment);
                        return true;
                    case R.id.store:
                        customerAppbarShow();
                        setFragment(cstoreFragment);
                        return true;
                    case R.id.inbox:
                        customerAppbarShow();
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

        NavigationView navigationView = findViewById(R.id.customernavviewId);
        View header = navigationView.getHeaderView(0);
        TextView userName = header.findViewById(R.id.headerNameId);
        TextView userNumber = header.findViewById(R.id.headerNumberId);
        CircleImageView userPic = header.findViewById(R.id.headerPicId);

        userName.setText(prefConfig.readName());
        userNumber.setText(prefConfig.readNumber());

        Picasso.get().load(ROFILE_PIC).placeholder(R.drawable.default_pro_pic).into(userPic);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.cOrderId) {
                    setFragment(inboxFragment);
                    drawer.closeDrawers();
                }

                if (id == R.id.cFavId) {
                    setFragment(cCollectionFragment);
                    drawer.closeDrawers();
                }

                if (id == R.id.cProfileId) {
                    startActivity(new Intent(Customer.this,ProfileActivity.class));
                    drawer.closeDrawers();
                }

                if (id == R.id.clogoutId) {
                    prefConfig.writeLoginStatus(false);
                    prefConfig.writeemail("");
                    prefConfig.writeaddress("");
                    prefConfig.writenumber("");
                    prefConfig.writename("");
                    prefConfig.writeProfileUrl("");
                    LoginManager.getInstance().logOut();
                    Intent Main = new Intent(Customer.this, MainActivity.class);
                    startActivity(Main);
                    finish();
                }

                return true;
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.customerframId, fragment);
        fragmentTransaction.commit();
    }

    public static void customerAppbarHide() {
        customerMainAppBar.setVisibility(View.GONE);
    }

    public static void customerAppbarShow() {
        customerMainAppBar.setVisibility(View.VISIBLE);
    }

//    @Override
//    public void performLogin(String name) {
//        prefConfig.writename(name);
//    }
}
