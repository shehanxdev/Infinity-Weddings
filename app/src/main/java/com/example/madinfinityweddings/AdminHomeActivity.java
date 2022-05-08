package com.example.madinfinityweddings;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madinfinityweddings.Fragments.AdminDashboardAddFragment;
import com.example.madinfinityweddings.Fragments.AdminDashboardHomeFragment;
import com.example.madinfinityweddings.Fragments.AdminDashboardNotificationFragment;
import com.example.madinfinityweddings.Fragments.AdminDashboardSettingsFragment;
import com.example.madinfinityweddings.Fragments.AdminDashboardUpdateFragment;
import com.example.models.admin;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminHomeActivity extends AppCompatActivity {
    //Declaring references for bottom navigation view and fragments
    BottomNavigationView bottomNavigationView;
    admin currentAdmin;
    AdminDashboardHomeFragment adminDashboardHomeFragment;
    AdminDashboardAddFragment adminDashboardCategoriesFragment=new AdminDashboardAddFragment();
    AdminDashboardNotificationFragment adminDashboardNotificationFragment=new AdminDashboardNotificationFragment();
    AdminDashboardSettingsFragment adminDashboardSettingsFragment=new AdminDashboardSettingsFragment();
    AdminDashboardUpdateFragment adminDashboardUpdateFragment;
    TextView greetings;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        initializeContent();
        initializeBottomNavbar();

    }
    private void initializeContent(){
        Intent intent=getIntent();
        currentAdmin=(admin)intent.getSerializableExtra("admin");
        adminDashboardHomeFragment=new AdminDashboardHomeFragment(this.currentAdmin);

    }
    private void initializeBottomNavbar(){
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.adminDashboardBottomNavView);
        getSupportFragmentManager().beginTransaction().replace(R.id.adminDashboardFragmentContainer,adminDashboardHomeFragment).commit();
        //Setting badge for the notification icon
        BadgeDrawable badgeDrawable= bottomNavigationView.getOrCreateBadge(R.id.admin_dashboard_bottom_nav_menu_notification);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(4);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.admin_dashboard_bottom_nav_menu_add:
                        getSupportFragmentManager().beginTransaction().replace(R.id.adminDashboardFragmentContainer,adminDashboardCategoriesFragment).commit();
                        return true;
                    case R.id.admin_dashboard_bottom_nav_menu_notification:
                        getSupportFragmentManager().beginTransaction().replace(R.id.adminDashboardFragmentContainer,adminDashboardNotificationFragment).commit();
                        return true;
                    case R.id.admin_dashboard_bottom_nav_menu_settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.adminDashboardFragmentContainer,adminDashboardSettingsFragment).commit();
                        return true;
                    case R.id.admin_dashboard_bottom_nav_menu_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.adminDashboardFragmentContainer,adminDashboardHomeFragment).commit();
                        return true;
                }
                return false;
            }
        });

    }
    public void updateHotel(View view){

        TextView hotelTextView=(TextView)view.findViewById(R.id.admin_dashboard_all_hotels_base_hotel_name);
        String hotelName=hotelTextView.getText().toString();
        adminDashboardUpdateFragment=new AdminDashboardUpdateFragment(hotelName);
        getSupportFragmentManager().beginTransaction().replace(R.id.adminDashboardFragmentContainer,adminDashboardUpdateFragment).commit();
    }
}
