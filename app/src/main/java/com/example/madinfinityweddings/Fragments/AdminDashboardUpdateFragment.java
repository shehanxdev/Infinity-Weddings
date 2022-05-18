package com.example.madinfinityweddings.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madinfinityweddings.R;
import com.example.models.hotel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdminDashboardUpdateFragment extends Fragment {
    private String hotelname;
    TextView hotelNameField,hotelAddressField,hotelImageUrlField,hotelAboutField,hotelRankingField;
    String hotelName,hotelAddress,hotelImageUrl,hotelAbout;
    int hotelRank;
    hotel newHotel;
    Context context;
    AdminDashboardHomeFragment adminDashboardHomeFragment=new AdminDashboardHomeFragment();


    public AdminDashboardUpdateFragment(String hotelname){
        this.hotelname=hotelname;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_admin_dashboard_update, container, false);
        fetchHotel(view);
        Button updateButton=(Button) view.findViewById(R.id.adminDashboardUpdateHotelFormSubmitButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateHotel(view);
            }
        });

        Button deleteButton=(Button) view.findViewById(R.id.adminDashboardUpdateHotelFormDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteHotel(view);

            }
        });
        return view;
    }

    public void fetchHotel(View view){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("hotels").child(this.hotelname);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newHotel=snapshot.getValue(hotel.class);
                setTextFields(view);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setTextFields(View view){
        System.out.println(this.hotelname+" hotel nam,");
        System.out.println("inside setEtaxt");
       hotelNameField=(TextView) view.findViewById(R.id.adminDashboardUpdateHotelName);
       hotelAboutField=(TextView)view.findViewById(R.id.adminDashboardUpdateHotelAbout);
       hotelRankingField=(TextView)view.findViewById(R.id.adminDashboardUpdateHotelRating);
       hotelImageUrlField=(TextView)view.findViewById(R.id.adminDashboardUpdateHotelImageUrl);
       hotelAddressField=(TextView)view.findViewById(R.id.adminDashboardUpdateHotelAddress);

      if(newHotel==null){
          hotelNameField.setText("Deleted");
          hotelAddressField.setText("Deleted");
          hotelRankingField.setText("Deleted");
          hotelImageUrlField.setText("Deleted");
          hotelAboutField.setText("Deleted");
      }
      else{
          hotelNameField.setText(newHotel.getHotelName());
          hotelAddressField.setText(newHotel.getAddress());
          hotelRankingField.setText(Integer.toString(newHotel.getRatings()));
          hotelImageUrlField.setText(newHotel.getAddress());
          hotelAboutField.setText(newHotel.getAbout());
      }

    }

    public void updateHotel(View view){
        //retrieving text field values
        hotelName=hotelNameField.getText().toString();
        hotelAddress=hotelAddressField.getText().toString();
        hotelRank=Integer.parseInt(hotelRankingField.getText().toString());
        hotelImageUrl=hotelImageUrlField.getText().toString();
        hotelAbout=hotelAboutField.getText().toString();
        //creating a new hotel object
        hotel updatedHotel=new hotel(hotelName,hotelAddress,hotelRank,hotelImageUrl,hotelAbout);
        //retrieving database connection suing newHotel object which holds current values of the hotel
          DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("hotels").child(newHotel.getHotelName());
        databaseReference.setValue(updatedHotel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(),"Hotel Updated Successfully",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT);
            }
        });
    }

    public void deleteHotel(View view){
        //retrieving database connection suing newHotel object which holds current values of the hotel
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("hotels").child(newHotel.getHotelName());
        //setting value as null so the child node will be deleted
        databaseReference.setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(),"Hotel Deleted Successfully",Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.adminDashboardFragmentContainer,adminDashboardHomeFragment).commit();
            }
        });
    }
}