package com.example.infinityweddings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DeleteProfileActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private EditText editTextUserPwd;
    private TextView textViewAuthenticated;
    private ProgressBar progressBar;
    private String userPwd;
    private Button buttonReAuthenticate, buttonDeleteUser;
    private final static String TAG = "DeleteProfileActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_profile);

        getSupportActionBar().setTitle("Delete Your Profile");

        progressBar = findViewById(R.id.progressBar);
        editTextUserPwd = findViewById(R.id.editText_delete_user_pwd);
        textViewAuthenticated = findViewById(R.id.textView_delete_user_authenticated);
        buttonDeleteUser = findViewById(R.id.button_delete_user);
        buttonReAuthenticate = findViewById(R.id.button_delete_user_authenticated);

        //Disable delete user button until user is authenticated
        buttonDeleteUser.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser.equals("")) {
            Toast.makeText(this, "Something went wrong!" + "User details are not available at the moment", Toast.LENGTH_SHORT).show();
            Intent intent  = new Intent(DeleteProfileActivity.this, UserProfileActivity.class);
            startActivity(intent);
            finish();
        }else {
            reAuthenticateUser(firebaseUser);
        }


    }

    //Re authenticate user before changing password
    private void reAuthenticateUser(FirebaseUser firebaseUser) {
        buttonReAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userPwd = editTextUserPwd.getText().toString();

                if (TextUtils.isEmpty(userPwd)){
                    Toast.makeText(DeleteProfileActivity.this, "Password is needed!", Toast.LENGTH_SHORT).show();
                    editTextUserPwd.setError("Please enter your  current password to authenticate");
                    editTextUserPwd.requestFocus();
                }else{
                    progressBar.setVisibility(View.VISIBLE);

                    //Authenticate User now
                    AuthCredential credential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),userPwd);

                    firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);

                                //Disable edittext for password .
                                editTextUserPwd.setEnabled(false);

                                //Enable Delete user button . Disable authenticate button
                                buttonReAuthenticate.setEnabled(false);
                                buttonDeleteUser.setEnabled(true);

                                //set Text view to show user is authenticated verified
                                textViewAuthenticated.setText("Password has been verified!.");
                                Toast.makeText(DeleteProfileActivity.this, "You can delete your password now ." + "Be careful, this action irreversible", Toast.LENGTH_SHORT).show();

                                //Update color of change password
                                buttonDeleteUser.setBackgroundTintList(ContextCompat.getColorStateList(DeleteProfileActivity.this,R.color.teal_200));

                                buttonDeleteUser.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        showAlertDialog();
                                    }
                                });
                            }else {
                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }


            }
        });
    }

    //creating alert dialog box
    private void showAlertDialog() {
        //setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(DeleteProfileActivity.this);
            builder.setTitle("Delete User and Related data?");
            builder.setMessage("Do you really want to delete your profile? This action is irreversible!");

            //Open Email apps if user clicks / taps Continue button
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteUser(firebaseUser);
                }
            });

            //Return the User profile activity if user presses cancel button
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(DeleteProfileActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            //create the AlertBox
            AlertDialog alertDialog = builder.create();

            //change the button color of continue
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red));
            }
        });

            //Show the AlertDialog Box
            alertDialog.show();
    }


    //delete user authentication
    private void deleteUser(FirebaseUser firebaseUser) {
        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //delete user details
                    deleteUserData();
                    authProfile.signOut();
                    Toast.makeText(DeleteProfileActivity.this, "User has been deleted successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DeleteProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    try{
                        throw task.getException();
                    }catch (Exception e){
                        Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    //Delete the all data of user
    private void deleteUserData() {
     /*   //Delete Display pic
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(firebaseUser.getPhotoUrl().toString());
        storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "OnSuccess: Photo Deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
                Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */

        //Delete data from realtime data base
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Register Users");
        databaseReference.child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "OnSuccess:User Data Deleted");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, e.getMessage());
                Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

            finish();
            overridePendingTransition(0, 0);
        } else if(id == R.id.menu_update_profile){
            Intent intent = new Intent(DeleteProfileActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
        }else if (id== R.id.menu_update_email){
            Intent intent = new Intent(DeleteProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
        }else if(id == R.id.menu_settings){
            Toast.makeText(DeleteProfileActivity.this, "menu_settings!", Toast.LENGTH_LONG).show();
        }else if (id == R.id.menu_change_password){
            Intent intent = new Intent(DeleteProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        }else if (id == R.id.menu_delete_profile){
            Intent intent = new Intent(DeleteProfileActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_logout){
            Toast.makeText(DeleteProfileActivity.this, "Logged out!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(DeleteProfileActivity.this, MainActivity.class);

            //Clear the stack to prevent user coming back to UserProfile on pressing back button after logged out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); //close UsrProfile

        }else{
            Toast.makeText(DeleteProfileActivity.this, "Something went Wrong!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}