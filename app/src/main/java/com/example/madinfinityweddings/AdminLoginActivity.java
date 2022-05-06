package com.example.madinfinityweddings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.models.admin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdminLoginActivity extends AppCompatActivity {
    Button loginButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        loginButton=(Button) findViewById(R.id.loginPageLogin);
        //onClick Listener for Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameEdit=(EditText) findViewById(R.id.username);
                EditText passwordEdit=(EditText) findViewById(R.id.password);
                String username=usernameEdit.getText().toString();
                String password=passwordEdit.getText().toString();
                auth(username,password);

            }
        });
    }

    private void auth(String username,String password){

        final admin[] admin=new admin[1];

        //database operations
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("admin");
        //event listener for database
        dbRef.child("admin1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                admin[0]=snapshot.getValue(com.example.models.admin.class);
                System.out.println(admin[0].email);
                if(admin[0].password.equals(password)){
                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),AdminHomeActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Incorrect Credentials",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                System.out.println("The read failed: " + error.getCode());

            }
        });
    }


}
