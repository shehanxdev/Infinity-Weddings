package com.example.infinityweddings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLoginEmail, editTextLoginPwd;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;



    private  static  final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        editTextLoginEmail = findViewById(R.id.editText_login_email);
        editTextLoginPwd = findViewById(R.id.editText_login_pwd);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();

        //Forgot Password
        TextView textViewForgotPassword = findViewById(R.id.textView_forgot_password);
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "You can reset your password now!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        //Login user
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextLoginEmail.getText().toString();
                String textPwd = editTextLoginPwd.getText().toString();

                if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(LoginActivity.this, "Please enter your Email!", Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("Email Required");
                    editTextLoginPwd.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(LoginActivity.this, "Please Re-enter your Email!", Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("Valid Email is  Required");
                    editTextLoginEmail.requestFocus();
                }else if(TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(LoginActivity.this, "Please enter your Password!", Toast.LENGTH_LONG).show();
                    editTextLoginPwd.setError("Password is required");
                    editTextLoginPwd.requestFocus();
                }else {
                    progressBar.setVisibility((View.VISIBLE));
                    loginUSer(textEmail, textPwd);
                }
            }

            private void loginUSer(String email, String pwd) {
                authProfile.signInWithEmailAndPassword(email , pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Gen the instance of the current user
                            FirebaseUser firebaseUser = authProfile.getCurrentUser();

                            //Check if email is verified or not before user can access their profiles
                            if (firebaseUser.isEmailVerified()) {
                                Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_LONG).show();

                                //open user profile
                                //start the UserProfileActivity
                                startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));

                                finish();

                            }
                            firebaseUser.sendEmailVerification();
                            Toast.makeText(LoginActivity.this,"Pleas check  your Email to Continue!",Toast.LENGTH_LONG).show();
                        } else {

                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                editTextLoginEmail.setError("User does not exists! Please Register now!");
                                editTextLoginEmail.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                editTextLoginEmail.setError("Invalid Email or Password!. Try again!.");
                                editTextLoginEmail.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            Toast.makeText(LoginActivity.this, "Something went Wrong!", Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);

                    }

                });
            }

                    //setup the alert builder
                    private void showAlertDialog() {

                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("Email Not Verified");
                        builder.setMessage("Please Verify your Email");

                        //Open Email apps if user clicks / taps Continue button
                        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // To email app in new window and not within our app
                                startActivity(intent);
                            }


                        });

                        //create the AlertBox
                        AlertDialog alertDialog = builder.create();

                        //Show the AlertDialog Box
                        alertDialog.show();

                    }



        });

    }

      //Check if user is already logged in  And Straightly take the user to the users profile
            @Override
            protected void onStart() {
                super.onStart();{
                    if (authProfile.getCurrentUser() != null) {
                        Toast.makeText(LoginActivity.this, "Already logged in", Toast.LENGTH_LONG).show();

                        //Start the UserProfile Activity
                        startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
                        finish(); //Close loginActivity
                    } else {
                        Toast.makeText(LoginActivity.this, "Please login", Toast.LENGTH_LONG).show();
                    }
                }
            }

}
















     /*  ///Show hide password eye Icon
        ImageView imageViewShowHidePwd = findViewById(R.id.imageView_show_hide_pdw);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
        imageViewShowHidePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextLoginPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    //if pwd is visible
                    editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    //change Icon
                    imageViewShowHidePwd.setImageResource((R.drawable.ic_hide_pwd));
                }else {
                    editTextLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);
                }
            }
             <ImageButton
            android:id="@+id/imageView_show_hide_pdw"
            android:layout_width="30dp"
            android:layout_height="30dp"
            android:layout_below="@id/textView_login_password"
            android:layout_marginTop="14dp"
            android:layout_marginLeft="2dp"/>

        });
*/