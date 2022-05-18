package com.example.infinityweddings.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.infinityweddings.dto.Cart;
import com.example.infinityweddings.R;

import java.util.ArrayList;

public class FoodListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Cart> foodList;


    public FoodListAdapter(Context context, int layout, ArrayList<Cart> foodList) {
        this.context = context;
        this.layout = layout;
        this.foodList = foodList;
    }

    @Override
    public int getCount() {
        return foodList.size();
    }

    @Override
    public Object getItem(int position) {
        return foodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder{
        ImageView imageView;
        TextView txtName,txtPrice,txtQty,txtDescription;
        Button btn;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);

            holder.txtName = row.findViewById(R.id.textView8);
            holder.txtPrice = row.findViewById(R.id.textView9);
            holder.txtQty = row.findViewById(R.id.textView10);

            row.setTag(holder);

        }else{
            holder = (ViewHolder) row.getTag();
        }

        Cart food = foodList.get(position);

        holder.txtName.setText("Name: "+food.getProductId());
        holder.txtPrice.setText("Price packages:\n10000");
        holder.txtQty.setText("Qty: "+food.getQty());



        return row;
    }



}
