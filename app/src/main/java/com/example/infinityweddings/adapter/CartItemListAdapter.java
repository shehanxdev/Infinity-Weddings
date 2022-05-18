package com.example.infinityweddings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infinityweddings.LoginActivity;
import com.example.infinityweddings.UserCartActivity;
import com.example.infinityweddings.dto.Cart;
import com.example.infinityweddings.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class CartItemListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Cart> arrayList;
    private CartItemListAdapter cartItemListAdapter;

    public CartItemListAdapter(Context context, int layout, ArrayList<Cart> arrayList) {
        this.cartItemListAdapter = this;
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private class ViewHolder{
        ImageView imageView;
        TextView txtName,txtPrice,txtQty,txtDescription;
        ImageButton btnDelete,btnAdd, btnMinus;

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cart");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;

        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);

            holder.txtName = row.findViewById(R.id.textView8);
            holder.txtPrice = row.findViewById(R.id.textView9);
            holder.txtQty = row.findViewById(R.id.textView10);
            holder.btnDelete = row.findViewById(R.id.btnDelete);
            holder.btnAdd = row.findViewById(R.id.edit_button9);
            holder.btnMinus = row.findViewById(R.id.edit_button11);

            row.setTag(holder);

        }else{
            holder = (ViewHolder) row.getTag();
        }

        Cart item = arrayList.get(position);

        holder.txtName.setText("Name: "+item.getProductId());
        holder.txtPrice.setText("Price packages:\n10000");
        holder.txtQty.setText("Qty: "+item.getQty());

        holder.btnDelete.setOnClickListener(view -> {
            System.out.println(item.getCartId());

            arrayList.remove(item);
            cartItemListAdapter.notifyDataSetChanged();

            Query applesQuery = ref.child(firebaseUser.getUid()).child(item.getCartId());

            applesQuery.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        //                    @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().removeValue();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //handle databaseError
                        }
                    }
            );

            try {
                Thread.sleep(1000);
                this.notifyDataSetChanged();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        holder.btnAdd.setOnClickListener(view -> {
            item.setQty(item.getQty()+1);
            System.out.println(item);
            this.notifyDataSetChanged();
            Query applesQuery = ref.child(firebaseUser.getUid()).child(item.getCartId());

            applesQuery.addListenerForSingleValueEvent(
                    new ValueEventListener() {
//                                    @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            Get map of users in datasnapshot
                            Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                            System.out.println();
                            System.out.println(item);
                            System.out.println();
                            ref.child(firebaseUser.getUid()).child(item.getCartId()).setValue(item);
                            cartItemListAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
    //                        handle databaseError
                        }
                    }
            );

        });


        holder.btnMinus.setOnClickListener(view -> {
            if (item.getQty()>1) {
                item.setQty(item.getQty() - 1);
                System.out.println(item);
                this.notifyDataSetChanged();
                Query applesQuery = ref.child(firebaseUser.getUid()).child(item.getCartId());

                applesQuery.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            //                                    @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
//                            Get map of users in datasnapshot
                                Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                                System.out.println();
                                System.out.println(item);
                                System.out.println();
                                ref.child(firebaseUser.getUid()).child(item.getCartId()).setValue(item);
                                cartItemListAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //                        handle databaseError
                            }
                        }
                );
            }
        });
        return row;
    }



}
