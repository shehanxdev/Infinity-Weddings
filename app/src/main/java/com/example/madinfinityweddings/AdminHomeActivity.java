package com.example.madinfinityweddings;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madinfinityweddings.Fragments.AdminDashboardCategoriesFragment;
import com.example.madinfinityweddings.Fragments.AdminDashboardHomeFragment;
import com.example.madinfinityweddings.Fragments.AdminDashboardNotificationFragment;
import com.example.madinfinityweddings.Fragments.AdminDashboardSettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminHomeActivity extends AppCompatActivity {
    //Declaring references for bottom navigation view and fragments
    BottomNavigationView bottomNavigationView;
    AdminDashboardHomeFragment adminDashboardHomeFragment=new AdminDashboardHomeFragment();
    AdminDashboardCategoriesFragment adminDashboardCategoriesFragment=new AdminDashboardCategoriesFragment();
    AdminDashboardNotificationFragment adminDashboardNotificationFragment=new AdminDashboardNotificationFragment();
    AdminDashboardSettingsFragment adminDashboardSettingsFragment=new AdminDashboardSettingsFragment();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        initializeBottomNavbar();
    }
    private void initializeBottomNavbar(){
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.adminDashboardBottomNavView);
        getSupportFragmentManager().beginTransaction().replace(R.id.adminDashboardFragmentContainer,adminDashboardHomeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.admin_dashboard_bottom_nav_menu_categories:
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
}
