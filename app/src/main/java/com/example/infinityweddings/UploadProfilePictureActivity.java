package com.example.infinityweddings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadProfilePictureActivity extends AppCompatActivity {


    private ProgressBar progressBar;
    private ImageView imageViewUploadPic;
    private FirebaseAuth authProfile;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_picture);

        getSupportActionBar().setTitle("Upload_Profile_Picture");

        Button buttonUploadPicChoose = findViewById(R.id.update_profile_pic_choose_button);
        Button buttonUploadPic = findViewById(R.id.upload_pic_button);
        progressBar = findViewById(R.id.progressBar);
        imageViewUploadPic = findViewById(R.id.imageView_profile_dp);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("DisplayPics");

        Uri uri = firebaseUser.getPhotoUrl();

        //Set users current Dp in imageView (if uploaded already)
        //Regular URIs.

        Picasso.with(UploadProfilePictureActivity.this).load(uri).into(imageViewUploadPic);

        //choosing image to upload

        buttonUploadPicChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        //Upload image
        buttonUploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                UploadPic();

            }
        });
    }


    private void openFileChooser() {
                    Intent intent = new Intent();
                    intent.setType("Image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, PICK_IMAGE_REQUEST);
                }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null  ) {
            uriImage = data.getData();
            imageViewUploadPic.setImageURI(uriImage);
        }
    }

    private void UploadPic(){
        if (uriImage != null){
            //save the  image uid of the current user logged user
            StorageReference fileReference = storageReference.child(authProfile.getCurrentUser().getUid() + "." + getFileExtention(uriImage));


            //Upload image to storage
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                            firebaseUser =authProfile.getCurrentUser();

                            //Set the display image after upload
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUri).build();
                            firebaseUser.updateProfile(profileUpdates);
                        }
                    });
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadProfilePictureActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(UploadProfilePictureActivity.this , UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadProfilePictureActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    //Obtain file extension of the image
    private String getFileExtention(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
            Intent intent = new Intent(UploadProfilePictureActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id== R.id.menu_update_email){
            Intent intent = new Intent(UploadProfilePictureActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
        }else if(id == R.id.menu_settings){
            Toast.makeText(UploadProfilePictureActivity.this, "menu_settings!", Toast.LENGTH_LONG).show();
        }else if (id == R.id.menu_change_password){
            Intent intent = new Intent(UploadProfilePictureActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        }else if (id == R.id.menu_delete_profile){
            Intent intent = new Intent(UploadProfilePictureActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.menu_logout){
            authProfile.signOut();
            Toast.makeText(UploadProfilePictureActivity.this, "Logged out!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(UploadProfilePictureActivity.this, MainActivity.class);

            //Clear the stack to prevent user coming back to UserProfile on pressing back button after logged out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); //close UsrProfile

        }else{
            Toast.makeText(UploadProfilePictureActivity.this, "Something went Wrong!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}

