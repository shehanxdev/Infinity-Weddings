package com.example.madinfinityweddings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.models.hotel;

import java.util.ArrayList;

public class AdminAllHotelRecyclerViewAdapter extends RecyclerView.Adapter<AdminAllHotelRecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<hotel>hotelArrayList;
    public AdminAllHotelRecyclerViewAdapter(Context context, ArrayList<hotel> hotelArrayList){
        this.context=context;
        this.hotelArrayList=hotelArrayList;
    }
    @NonNull
    @Override
    public AdminAllHotelRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.recyclerview_admin_dashboard_all_hotels_base,parent,false);
        return new AdminAllHotelRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAllHotelRecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.hotelName.setText(hotelArrayList.get(position).getHotelName());
    }

    @Override
    public int getItemCount() {
        return hotelArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView hotelName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelName=(TextView) itemView.findViewById(R.id.admin_dashboard_all_hotels_base_hotel_name);
        }
    }
}
