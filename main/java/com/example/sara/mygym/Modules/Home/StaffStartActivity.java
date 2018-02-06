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
import com.example.sara.mygym.Common.Network.REST.ApiClient;
import com.example.sara.mygym.Common.Network.REST.ApiInterface;
import com.example.sara.mygym.Common.Network.Wrappers.PersonModel;
import com.example.sara.mygym.Modules.Members.MembersFragment;
import com.example.sara.mygym.Modules.News.Fragment.NewsFragment;
import com.example.sara.mygym.Modules.PriceList.Fragments.PriceListFragment;
import com.example.sara.mygym.Modules.Staff.StaffFragment;
import com.example.sara.mygym.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffStartActivity extends AppCompatActivity {
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
    private MembersFragment membersFragment;
    private AboutUsFragment aboutUsFragment;

    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_start);

        apiInterface = ApiClient.getClient();

        AHBottomNavigationItem itempriceList = new AHBottomNavigationItem(getResources().getString(R.string.price_list), R.drawable.icons8_money);
        AHBottomNavigationItem itemStuff = new AHBottomNavigationItem(getResources().getString(R.string.staff), R.drawable.icons8_personal_trainer_filled);
        AHBottomNavigationItem itemMembers = new AHBottomNavigationItem(getResources().getString(R.string.members), R.drawable.icons8_user_groups);
        AHBottomNavigationItem itemNews = new AHBottomNavigationItem(getResources().getString(R.string.news), R.drawable.icons8_news);
        AHBottomNavigationItem itemInfo = new AHBottomNavigationItem(getResources().getString(R.string.about_us), R.drawable.icons8_info);

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation_staff);

        bottomNavigation.addItem(itempriceList);
        bottomNavigation.addItem(itemStuff);
        bottomNavigation.addItem(itemMembers);
        bottomNavigation.addItem(itemNews);
        bottomNavigation.addItem(itemInfo);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#000000"));
        bottomNavigation.setAccentColor(Color.parseColor("#00B2EE"));


        bottomNavigation.setCurrentItem(2);

        MembersFragment membersFragment = new MembersFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frameStaff, membersFragment, CURRENT_FLAG).commit();

        setBottomNavigationListener();

        setNumberOfPresentMembers();
    }

    private void setBottomNavigationListener() {
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                FragmentManager fm;
                Bundle bundle = new Bundle();
                switch (position) {
                    case 0:
                        bundle.putString("Price_opened", "staff");
                        priceListFragment = new PriceListFragment();
                        priceListFragment.setArguments(bundle);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.frameStaff, priceListFragment, CURRENT_FLAG).commit();
                        CURRENT_FLAG = TAG_PRICE;
                        break;
                    case 1:
                        bundle.putString("Staff_opened", "staff");
                        staffFragment = new StaffFragment();
                        staffFragment.setArguments(bundle);
                        fm = getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.frameStaff, staffFragment, CURRENT_FLAG).commit();
                        CURRENT_FLAG = TAG_STUFF;
                        break;

                    case 2:
                        bundle.putString("Members_opened", "staff");
                        membersFragment = new MembersFragment();
                        membersFragment.setArguments(bundle);
                        fm = getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.frameStaff, membersFragment, CURRENT_FLAG).commit();
                        CURRENT_FLAG = TAG_MEMBERS;
                        break;
                    case 3:
                        bundle.putString("News_opened", "staff");
                        newsFragment = new NewsFragment();
                        newsFragment.setArguments(bundle);
                        fm = getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.frameStaff, newsFragment, CURRENT_FLAG).commit();
                        CURRENT_FLAG = TAG_NEWS;
                        break;
                    case 4:
                        bundle.putString("Info_opened", "staff");
                        aboutUsFragment = new AboutUsFragment();
                        aboutUsFragment.setArguments(bundle);
                        fm = getSupportFragmentManager();
                        fm.beginTransaction().replace(R.id.frameStaff, aboutUsFragment, CURRENT_FLAG).commit();
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

    private void setMembersInGym(final int number) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AHNotification notification = new AHNotification.Builder()
                        .setText(String.valueOf(number))
                        .setTextColor(Color.WHITE)
                        .build();
                bottomNavigation.setNotification(notification, 2);
            }
        }, 5000);
    }

    public void setNumberOfPresentMembers(){
        Call<List<PersonModel>> call = apiInterface.getListOfPersons();
        final int[] counter = {0};
        call.enqueue(new Callback<List<PersonModel>>() {
            @Override
            public void onResponse(Call<List<PersonModel>> call, Response<List<PersonModel>> response) {
                List<PersonModel> personModelList = response.body();
                for(PersonModel el: personModelList){
                    if(el.getPerson().getRole().getName().equals("member")){
                        if(el.getPerson().isInGym()){
                            counter[0]++;
                        }
                    }
                }
                setMembersInGym(counter[0]);
            }

            @Override
            public void onFailure(Call<List<PersonModel>> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
