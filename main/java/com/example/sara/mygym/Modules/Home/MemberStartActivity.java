package com.example.sara.mygym.Modules.Home;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.example.sara.mygym.Common.Fragments.AboutUsFragment;
import com.example.sara.mygym.Modules.News.Fragment.NewsFragment;
import com.example.sara.mygym.Modules.PriceList.Fragments.PriceListFragment;
import com.example.sara.mygym.Modules.Staff.StaffFragment;
import com.example.sara.mygym.Modules.Members.OnlyMember.Fragments.HomeMemberFragment;
import com.example.sara.mygym.R;

public class MemberStartActivity extends AppCompatActivity {
    public AHBottomNavigation bottomNavigation;

    private static final String TAG_PRICE = "price";
    private static final String TAG_STUFF = "stuff";
    private static final String TAG_MEMBERS = "members";
    private static final String TAG_NEWS = "news";
    private static final String TAG_INFO = "info";
    public static String CURRENT_FLAG = TAG_MEMBERS;

    private PriceListFragment priceListFragment;
    private StaffFragment staffFragment;
    private NewsFragment newsFragment;
    private HomeMemberFragment homeMemberFragment;
    private AboutUsFragment aboutUsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_start);

        AHBottomNavigationItem itempriceList = new AHBottomNavigationItem(getResources().getString(R.string.price_list), R.drawable.icons8_money);
        AHBottomNavigationItem itemStuff = new AHBottomNavigationItem(getResources().getString(R.string.staff), R.drawable.icons8_personal_trainer_filled);
        AHBottomNavigationItem itemMembers = new AHBottomNavigationItem(getResources().getString(R.string.home), R.drawable.icons8_account);
        AHBottomNavigationItem itemNews = new AHBottomNavigationItem(getResources().getString(R.string.news), R.drawable.icons8_news);
        AHBottomNavigationItem itemInfo = new AHBottomNavigationItem(getResources().getString(R.string.about_us), R.drawable.icons8_info);

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation_member);

        bottomNavigation.addItem(itempriceList);
        bottomNavigation.addItem(itemStuff);
        bottomNavigation.addItem(itemMembers);
        bottomNavigation.addItem(itemNews);
        bottomNavigation.addItem(itemInfo);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#000000"));
        bottomNavigation.setAccentColor(Color.parseColor("#00B2EE"));


        bottomNavigation.setCurrentItem(2);

        HomeMemberFragment homeMemberFragment = new HomeMemberFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frameMember, homeMemberFragment, CURRENT_FLAG).commit();

        setBottomNavigationListener();
    }

    private void setBottomNavigationListener() {
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                FragmentManager fm;
                Bundle bundle = new Bundle();
                switch (position) {
                    case 0:
                        bundle.putString("Price_opened", "member");
                        priceListFragment = new PriceListFragment();
                        priceListFragment.setArguments(bundle);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.frameMember, priceListFragment, CURRENT_FLAG).commit();
                        CURRENT_FLAG = TAG_PRICE;
                        break;
                    case 1:
                        bundle.putString("Staff_opened", "member");
                        staffFragment = new StaffFragment();
                        staffFragment.setArguments(bundle);
                        fm = getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.frameMember, staffFragment, CURRENT_FLAG).commit();
                        CURRENT_FLAG = TAG_STUFF;
                        break;

                    case 2:
                        homeMemberFragment = new HomeMemberFragment();
                        fm = getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.frameMember, homeMemberFragment, CURRENT_FLAG).commit();
                        CURRENT_FLAG = TAG_MEMBERS;
                        break;
                    case 3:
                        bundle.putString("News_opened", "member");
                        newsFragment = new NewsFragment();
                        newsFragment.setArguments(bundle);
                        fm = getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.frameMember, newsFragment, CURRENT_FLAG).commit();
                        CURRENT_FLAG = TAG_NEWS;
                        break;
                    case 4:
                        bundle.putString("Info_opened", "member");
                        aboutUsFragment = new AboutUsFragment();
                        aboutUsFragment.setArguments(bundle);
                        fm = getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.frameMember, aboutUsFragment, CURRENT_FLAG).commit();
                        CURRENT_FLAG = TAG_INFO;
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
