package com.example.madinfinityweddings.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.madinfinityweddings.AdminAllHotelRecyclerViewAdapter;
import com.example.madinfinityweddings.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.models.hotel;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardAllHotelsFragment extends Fragment {
    //Attribute declaration
    ArrayList<hotel>hotelList=new ArrayList<hotel>();
    RecyclerView recyclerView;

;
    public AdminDashboardAllHotelsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void fetchHotels(){
        //Getting a database reference
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("hotels");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot topSnapshot) {
                //this for loop fetches all the child nodes in each iteration
                for(DataSnapshot snapshot: topSnapshot.getChildren()){
                    //A console print to make sure data is correct
                    System.out.println(snapshot.getValue());
                    hotel newHotel=snapshot.getValue(hotel.class);
                    //adding each fetched hotel to hotel arraylist
                    hotelList.add(newHotel);
                }
                //attaching the recyclerview adapter only after all the data is fetched
                attachRecyclerViewAdapter();
                Toast.makeText(getContext(),"You have a total of "+hotelList.size()+" hotels",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Error fetching data",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void attachRecyclerViewAdapter(){
        AdminAllHotelRecyclerViewAdapter adminAllHotelRecyclerViewAdapter=new AdminAllHotelRecyclerViewAdapter(getContext(),hotelList);
        recyclerView.setAdapter(adminAllHotelRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_admin_dashboard_all_hotels, container, false);
        recyclerView=view.findViewById(R.id.admin_dashboard_all_hotels_recyclerview);
        fetchHotels();
        return view;

    }

}


