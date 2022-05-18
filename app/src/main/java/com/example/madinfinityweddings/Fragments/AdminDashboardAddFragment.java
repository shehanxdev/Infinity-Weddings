package com.example.madinfinityweddings.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.madinfinityweddings.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.example.models.hotel;


public class AdminDashboardAddFragment extends Fragment {


    public AdminDashboardAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    private void setEventListeners(View view){
        Button AdminHotelAdd=(Button) view.findViewById(R.id.admiDashboardAddHotellFormSubmitButton);
        TextInputEditText hotelNameField=(TextInputEditText)view.findViewById(R.id.adminDashboardAddHotelName);
        TextInputEditText hotelAddressField=(TextInputEditText)view.findViewById(R.id.adminDashboardAddHotelAddress);
        TextInputEditText hotelRatingField=(TextInputEditText)view.findViewById(R.id.adminDashboardAddHotelRating);
        TextInputEditText hotelImageUrlField=(TextInputEditText)view.findViewById(R.id.adminDashboardAddHotelImageUrl);
        TextInputEditText hotelAboutField=(TextInputEditText)view.findViewById(R.id.adminDashboardAddHotelAbout);
        AdminHotelAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hotelNameField.getText().toString().equals("")||hotelAddressField.getText().toString().equals("")||hotelRatingField.getText().toString().equals("")||hotelImageUrlField.getText().toString().equals("")||hotelAboutField.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Please fill all the fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    //Fetching user inputs
                    String hotelName=hotelNameField.getText().toString();
                    String hotelAddress=hotelAddressField.getText().toString();
                    int ratings=Integer.parseInt(hotelRatingField.getText().toString());
                    String hotelImageUrl=hotelImageUrlField.getText().toString();
                    String hotelAbout=hotelAboutField.getText().toString();
                    hotel newHotel=new hotel(hotelName,hotelAddress,ratings,hotelImageUrl,hotelAbout);


                    //Generating a reference in the database
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("hotels");
                    databaseReference.child(hotelName).setValue(newHotel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(),"New Hotel Is Added",Toast.LENGTH_SHORT).show();
                            //Removing entered values after a successful addition
                            hotelNameField.setText("");
                            hotelAddressField.setText("");
                            hotelRatingField.setText("");
                            hotelImageUrlField.setText("");
                            hotelAboutField.setText("");

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_admin_dashboard_add, container, false);
        setEventListeners(view);
        return view;
    }
}