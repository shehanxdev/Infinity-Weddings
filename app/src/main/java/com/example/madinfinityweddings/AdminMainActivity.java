package com.example.madinfinityweddings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);


    }
    public void nextPage(View view){

        Intent intent=new Intent(getApplicationContext(), AdminLoginActivity.class) ;
        startActivity(intent);

    }
}