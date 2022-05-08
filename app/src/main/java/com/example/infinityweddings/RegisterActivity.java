package com.example.infinityweddings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterFullName,  editTextRegisterEmail , editTextRegisterDoB, editTextRegisterMobile , editTextRegisterPwd, editTextRegisterConfirmPwd;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private RadioButton radioButtonRegisterGenderSelected;
    private DatePickerDialog picker;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");


        Toast.makeText(RegisterActivity.this,"You can register now", Toast.LENGTH_LONG).show();

        editTextRegisterFullName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDoB = findViewById(R.id.editText_register_dob);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterConfirmPwd = findViewById(R.id.editText_confirm_password);
        progressBar = findViewById(R.id.progressBar);


        //Setting up DatePicker
        editTextRegisterDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date picker Dialog
                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        editTextRegisterDoB.setText( dayOfMonth + "/" +  (month + 1) + "/" + year);
                    }
                } , year , month,day);
                picker.show();
            }
        });


        //RadioButton for gender

        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);
        radioGroupRegisterGender.clearCheck();

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected = findViewById(selectedGenderId);

                //obtain the entered data

                String textFullName = editTextRegisterFullName.getText().toString();
                String textEmail = editTextRegisterEmail.getText().toString();
                String textDob = editTextRegisterDoB.getText().toString();
                String textMobile =  editTextRegisterMobile.getText().toString();
                String textPwd = editTextRegisterPwd.getText().toString();
                String textConfirmPassword = editTextRegisterConfirmPwd.getText().toString();
                String textGender ; // Can;t obtain the value before verifying any button was selected or not


                //Validate the mobile number
                String mobileRegex = "[6-9][0-9]{9}"; //First number can be {6,8,9} abd rest 9 numbers can be any
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(textMobile);

                //Validate Register form
                if (TextUtils.isEmpty(textFullName)){
                    Toast.makeText(RegisterActivity.this,"Please enter your full name!", Toast.LENGTH_LONG).show();
                    editTextRegisterFullName.setError("Full name is Required");
                    editTextRegisterFullName.requestFocus();
                }else if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(RegisterActivity.this,"Please enter your Email!", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Email Required");
                    editTextRegisterEmail.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(RegisterActivity.this,"Please Re-enter your Email!", Toast.LENGTH_LONG).show();
                    editTextRegisterEmail.setError("Valid Email is  Required");
                    editTextRegisterEmail.requestFocus();
                }else if(TextUtils.isEmpty(textDob)){
                    Toast.makeText(RegisterActivity.this,"Please enter your date of birth!", Toast.LENGTH_LONG).show();
                    editTextRegisterDoB.setError("Date of Birth is Required");
                    editTextRegisterDoB.requestFocus();
                }else if(radioGroupRegisterGender.getCheckedRadioButtonId() == -1){
                    Toast.makeText(RegisterActivity.this,"Please select your gender!", Toast.LENGTH_LONG).show();
                    radioButtonRegisterGenderSelected.setError("Gender is Required");
                    radioButtonRegisterGenderSelected.requestFocus();
                }else if(TextUtils.isEmpty(textMobile)){
                    Toast.makeText(RegisterActivity.this,"Please enter your Mobile!", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile No is Required");
                    editTextRegisterMobile.requestFocus();
                }else if (textMobile.length() != 10){
                    Toast.makeText(RegisterActivity.this,"Please Re-enter your Mobile No!", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile number should be 10 digits!");
                    editTextRegisterMobile.requestFocus();
                }else if (mobileMatcher.find()){
                    Toast.makeText(RegisterActivity.this,"Please Re-enter your Mobile No!", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Mobile number is not valid");
                    editTextRegisterMobile.requestFocus();
                }else if(TextUtils.isEmpty(textPwd)){
                    Toast.makeText(RegisterActivity.this,"Please enter your Password!", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Password is required");
                    editTextRegisterPwd.requestFocus();
                }else if (textPwd.length() != 6){
                    Toast.makeText(RegisterActivity.this,"Please Re-enter your Password!", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Password Should be 6 digits" );
                    editTextRegisterPwd.requestFocus();
                }else if (TextUtils.isEmpty(textConfirmPassword)){
                    Toast.makeText(RegisterActivity.this,"Please Confirm your Password!", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("Password Confirmation Required");
                    editTextRegisterConfirmPwd.requestFocus();
                }else if (!textPwd.equals(textConfirmPassword)){
                    Toast.makeText(RegisterActivity.this,"Password does not matched!", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("Password must be same!");
                    editTextRegisterConfirmPwd.requestFocus();
                    //clear the entered password
                    editTextRegisterPwd.clearComposingText();
                    editTextRegisterConfirmPwd.clearComposingText();
                }else{
                    textGender = radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName, textEmail, textDob ,textMobile ,textPwd);
                }
            }
            //Register the user using Credentials given
            private void registerUser(String textFullName, String textEmail, String textDob, String textMobile, String textPwd) {

                FirebaseAuth auth = FirebaseAuth.getInstance();

                //Create User Profile

                auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            FirebaseUser firebaseUSer = auth.getCurrentUser();


                            //Update display name if user
                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                            firebaseUSer.updateProfile(profileChangeRequest);

                            //Enter User data into firebase realtime database

                            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails( textFullName, textDob,textMobile );

                            //Extracting user reference from database fro "Registered Users
                            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Register Users");

                            referenceProfile.child(firebaseUSer.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        //Send Verification Email
                                        firebaseUSer.sendEmailVerification();

                                        Toast.makeText(RegisterActivity.this,"Registered Successfully!. Please verify your Email", Toast.LENGTH_LONG).show();
                                         //open user profile after successful registration
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

                                    //to prevent User from returning back to register Activity on pressing back button after registration
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                    startActivity(intent);
                                    finish(); //to close register activity
                                    }else {
                                        Toast.makeText(RegisterActivity.this,"Registration Failed!. Please Try again", Toast.LENGTH_LONG).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                            //EXCEPTION HANDLING
                        }else {
                            try {
                                throw  task.getException();
                            }catch (FirebaseAuthWeakPasswordException e){
                                editTextRegisterPwd.setError(("Your Password is too weak. Kindly use a mix of alphabets, numbers and special characters"));
                                editTextRegisterPwd.requestFocus();
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                editTextRegisterEmail.setError("Your email is invalid or Already in use! Try again");
                                editTextRegisterEmail.requestFocus();
                            }catch (FirebaseAuthUserCollisionException e){
                                editTextRegisterEmail.setError("User is already registered with this email. Use another Email!");
                                editTextRegisterEmail.requestFocus();
                            }catch (Exception e){
                                Log.e(TAG, e.getMessage() );
                                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}