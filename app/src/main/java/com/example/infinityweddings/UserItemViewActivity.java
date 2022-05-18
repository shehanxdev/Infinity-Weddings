package com.example.infinityweddings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infinityweddings.dto.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Map;
import java.util.UUID;

public class UserItemViewActivity extends AppCompatActivity {

    private ImageButton b1, b2, b3;
    private Button btnGoToCart;
    private DatabaseReference referenceProfile;
    boolean bool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_item_view);

        b1 = findViewById(R.id.btnAddToCart1);
        b2 = findViewById(R.id.btnAddToCart2);
        b3 = findViewById(R.id.btnAddToCart3);
        btnGoToCart = findViewById(R.id.btnGoToCart);



        referenceProfile = FirebaseDatabase.getInstance().getReference("cart");

        btnGoToCart.setOnClickListener(view -> {
            Intent intent = new Intent(UserItemViewActivity.this, UserCartActivity.class);
            startActivity(intent);
        });

        b1.setOnClickListener(view -> {
            addToDB(1);
        });
        b2.setOnClickListener(view -> {
            addToDB(2);
        });
        b3.setOnClickListener(view -> {
            addToDB(3);
        });
    }

    private void addToDB(int prodId) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        String userID = firebaseUser.getUid();


        referenceProfile.child(userID).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    //                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot ds: dataSnapshot.getChildren()) {
                            bool = forEach((Map<String, Object>) dataSnapshot.getValue(), userID, prodId);
//                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                }


        );

        if (!bool) {
            addToCart(userID, prodId);
        }
    }

    private boolean addToCart(String userID, int prodId) {
        Cart cart = new Cart();
        cart.setProductId(prodId);
        cart.setQty(1);

        referenceProfile.child(userID).child(UUID.randomUUID().toString()).setValue(cart).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    Toast.makeText(UserItemViewActivity.this,"Added to Cart", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(UserItemViewActivity.this,"Failed to add to cart", Toast.LENGTH_LONG).show();
                }
            }
        });

        return true;
    }

    private boolean updateCart() {
        return true;
    }

    private boolean forEach(Map<String, Object> value, String userId,int prod) {
        System.out.println(">>>>>>>>>>>>>>>"+ value);
        if(value!=null) {
            for (Map.Entry<String, Object> entry : value.entrySet()) {
                if (entry != null) {
                    System.out.println("[[[[ "+entry.getValue().toString());
                    Cart cart = new Gson().fromJson(entry.getValue().toString(), Cart.class);
                    System.out.println(cart);
                    System.out.println(cart.getProductId() == prod);
                    if (cart.getProductId() == prod) {
                        cart.setQty(cart.getQty() + 1);
                        System.out.println("**************************************************************************************");
                        FirebaseDatabase.getInstance().getReference("cart").child(userId).child(entry.getKey()).setValue(cart);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

