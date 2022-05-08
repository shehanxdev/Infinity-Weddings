package com.example.madinfinityweddings.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.madinfinityweddings.R;
import com.example.models.admin;

public class AdminDashboardHomeFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private admin currentAdmin;
    private TextView greeting;
    private ImageButton viewHotelImageButton;
    private AdminDashboardAllHotelsFragment adminDashboardAllHotels=new AdminDashboardAllHotelsFragment();
    public AdminDashboardHomeFragment() {

    }
    public AdminDashboardHomeFragment(admin currentAdmin){
        this.currentAdmin=currentAdmin;
    }

    public static AdminDashboardHomeFragment newInstance(String param1, String param2) {
        AdminDashboardHomeFragment fragment = new AdminDashboardHomeFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void setOnClickListeners(View view){
        viewHotelImageButton=(ImageButton) view.findViewById(R.id.admin_dashboard_homefragment_tile1);
        viewHotelImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.adminDashboardFragmentContainer,adminDashboardAllHotels).commit();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_admin_dashboard_home, container, false);
        greeting=(TextView) view.findViewById(R.id.adminDashboardHomeGreeting);
        greeting.setText("Welcome "+currentAdmin.getFname().replace("\"",""));
        setOnClickListeners(view);

        return view;
    }
}