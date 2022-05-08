package com.example.infinityweddings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class ForgotPasswordActivity extends AppCompatActivity {
        private Button buttonPwdReset;
        private EditText editTextPwsResetEmail;
        private ProgressBar progressBar;
        private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().setTitle("Forgot Password");


        editTextPwsResetEmail = findViewById(R.id.editText_Password_reset_email);
        buttonPwdReset = findViewById(R.id.button_password_reset);
        progressBar = findViewById(R.id.progressBar);

        buttonPwdReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextPwsResetEmail.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please entered your registered Email!", Toast.LENGTH_LONG).show();
                    editTextPwsResetEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter valid Email!", Toast.LENGTH_LONG).show();
                    editTextPwsResetEmail.setError("Email is  Required");
                    editTextPwsResetEmail.requestFocus();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    resetPassword(email);
                }
            }

            private void resetPassword(String email) {
                authProfile =FirebaseAuth.getInstance();
                authProfile.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                            if ((task.isSuccessful())){
                                Toast.makeText(ForgotPasswordActivity.this, "Please check your inbox for password reset link!", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);

                                //Clear the stack to prevent user coming back to ForgotPasswordActivity
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish(); //close UsrProfile
                            }else {
                                Toast.makeText(ForgotPasswordActivity.this, "Something went wrong . Try again!!", Toast.LENGTH_LONG).show();
                            } progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });


    }
}