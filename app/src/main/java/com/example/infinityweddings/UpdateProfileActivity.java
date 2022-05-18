package com.example.infinityweddings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateProfileActivity extends AppCompatActivity {
    private EditText editTextUpdateName , editTextUpdateDob , editTextUpdateMobile;
    private String textFullName, textDob, textMobile;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        getSupportActionBar().setTitle("Update Profile Details");

        progressBar =findViewById(R.id.progressBar);
        editTextUpdateName = findViewById(R.id.editText_update_profile_name);
        editTextUpdateDob = findViewById(R.id.editText_update_profile_dob);
        editTextUpdateMobile =findViewById(R.id.editText_update_profile_mobile);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        //Show profile data
        showProfile(firebaseUser);

        //Upload profile pic btn
        Button buttonUploadProfilePic = findViewById(R.id.button_update_profile_pic);
        buttonUploadProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(UpdateProfileActivity.this, UploadProfilePictureActivity.class);
                startActivity(intent);
                finish();
            }
        });


    //Update profile email
      /*  Button buttonUpdateEmail = findViewById(R.id.button_update_profile_email);
        buttonUpdateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(UpdateProfileActivity.this, UpdateEmailActivity.class);
                startActivity(intent);
                finish();
            }
        }); */

        //Setting up DatePicker
        editTextUpdateDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textSADOB [] = textDob.split("/");


                int day = Integer.parseInt(textSADOB[0]);
                int month = Integer.parseInt(textSADOB[1]) -1;  // to take care of month index starting from
                int year = Integer.parseInt(textSADOB[2]);
                DatePickerDialog picker;

                //Date picker Dialog
                picker = new DatePickerDialog(UpdateProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        editTextUpdateDob.setText( dayOfMonth + "/" +  (month + 1) + "/" + year);
                    }
                } , year , month,day);
                picker.show();
            }
        });


        //Update Button
        Button buttonUpdateProfile = findViewById(R.id.button_update_profile);
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(firebaseUser);
            }
        });


    }
//updating the profile
    private void updateProfile(FirebaseUser firebaseUser) {

        //Validating
        if (TextUtils.isEmpty(textFullName)){
            Toast.makeText(UpdateProfileActivity.this,"Please enter your full name!", Toast.LENGTH_LONG).show();
            editTextUpdateName.setError("Full name is Required");
            editTextUpdateName.requestFocus();

        }else if(TextUtils.isEmpty(textDob)){
            Toast.makeText(UpdateProfileActivity.this,"Please enter your date of birth!", Toast.LENGTH_LONG).show();
            editTextUpdateDob.setError("Date of Birth is Required");
            editTextUpdateDob.requestFocus();
        }else if(TextUtils.isEmpty(textMobile)){
            Toast.makeText(UpdateProfileActivity.this,"Please enter your Mobile!", Toast.LENGTH_LONG).show();
            editTextUpdateMobile.setError("Mobile No is Required");
            editTextUpdateMobile.requestFocus();
        }else if (textMobile.length() != 10){
            Toast.makeText(UpdateProfileActivity.this,"Please Re-enter your Mobile No!", Toast.LENGTH_LONG).show();
            editTextUpdateMobile.setError("Mobile number should be 10 digits!");
            editTextUpdateMobile.requestFocus();
        }else {
            //obtained the data entered from user
            textFullName = editTextUpdateName.getText().toString();
            textDob = editTextUpdateDob.getText().toString();
            textMobile = editTextUpdateMobile.getText().toString();

            //Enter user data into firebase
            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textFullName, textDob , textMobile);
            progressBar.setVisibility(View.VISIBLE);

            //Extract User reference from database
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Register Users");
            String userID =   firebaseUser.getUid();
            progressBar.setVisibility(View.GONE);
            referenceProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        //setting new display
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(textFullName).build();
                        firebaseUser.updateProfile(profileUpdates);

                        Toast.makeText(UpdateProfileActivity.this,"Update Successful!", Toast.LENGTH_LONG).show();

                        //Stop user returnProfileActivity on pressing back btn
                        Intent intent = new Intent(UpdateProfileActivity.this, UserProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }else {
                        try {
                            throw task.getException();
                        }catch (Exception e){
                            Toast.makeText(UpdateProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                }
            });



        }
    }





    //fetch data from firebase and display
    private void showProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        //Extracting database users
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Register Users");
        progressBar.setVisibility(View.VISIBLE);
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null){
                    textFullName =  firebaseUser.getDisplayName();
                    textDob = readUserDetails.doB;
                    textMobile = readUserDetails.mobile;

                    editTextUpdateName.setText(textFullName);
                    editTextUpdateDob.setText(textDob);
                    editTextUpdateMobile.setText(textMobile);
                }else {
                    Toast.makeText(UpdateProfileActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfileActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }




    //Creating action bar menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu items
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    //When any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh){
            //Refresh Activity
            startActivity(getIntent());
            progressBar.setVisibility(View.VISIBLE);
            finish();
            overridePendingTransition(0, 0);
        } else if(id == R.id.menu_update_profile){
            Intent intent = new Intent(UpdateProfileActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id== R.id.menu_update_email){
            Intent intent = new Intent(UpdateProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
        }else if(id == R.id.menu_settings){
            Toast.makeText(UpdateProfileActivity.this, "menu_settings!", Toast.LENGTH_LONG).show();
        }else if (id == R.id.menu_change_password){
            Intent intent = new Intent(UpdateProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        }else if (id == R.id.menu_delete_profile){
            Intent intent = new Intent(UpdateProfileActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_logout){
            authProfile.signOut();
            Toast.makeText(UpdateProfileActivity.this, "Logged out!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);

            //Clear the stack to prevent user coming back to UserProfile on pressing back button after logged out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); //close UsrProfile

        }else{
            Toast.makeText(UpdateProfileActivity.this, "Something went Wrong!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}