package com.example.infinityweddings;

import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.infinityweddings.adapter.CartItemListAdapter;
import com.example.infinityweddings.dto.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class UserCartActivity extends AppCompatActivity {

    GridView gridView;
    ArrayList<Cart> list;
    CartItemListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_cart);

        gridView = (GridView) findViewById(R.id.gridCartItem);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String userID = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("cart");

        referenceProfile.child(userID).addListenerForSingleValueEvent(
                new ValueEventListener() {
//                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();
                        System.out.println();
                        System.out.println(value);
                        System.out.println();


                        list = extractCartOBJ(value);

                        adapter = new CartItemListAdapter(getApplicationContext(),R.layout.user_cart_item,list);
                        gridView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                }
        );
    }

    private ArrayList<Cart> extractCartOBJ(Map<String,Object> cartItem) {
        ArrayList<Cart> arrayList=new ArrayList<>();
        if(cartItem!=null) {
            for (Map.Entry<String, Object> entry : cartItem.entrySet()) {

                Map singleUser = (Map) entry.getValue();
                System.out.println(singleUser);
                Cart cart = new Gson().fromJson(singleUser.toString(), Cart.class);
                cart.setCartId(entry.getKey());
                System.out.println(cart);
                arrayList.add(cart);
            }
        }
        return arrayList;
    }
}
