package com.example.gr.utils;

import com.example.gr.model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    private DatabaseReference databaseReference;
    public static final int PAGE_SIZE = 10;
    public DataLoader() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void loadFoodPage(String searchKey, final FoodLoadCallback callback) {
        if(searchKey == null){
            searchKey = "";
        }
        Query query;
        if (searchKey.isEmpty()){
            query = databaseReference.child("shared/food")
                    .orderByChild("name")
                    .startAt(searchKey.trim())
                    .endAt(searchKey.trim() + "\uf8ff")
                    .limitToFirst(PAGE_SIZE);
        }else{
            query = databaseReference.child("shared/food")
                    .orderByChild("name")
                    .startAt(searchKey.trim())
                    .endAt(searchKey.trim() + "\uf8ff");
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Food> foodList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    foodList.add(snapshot.getValue(Food.class));
                }
                callback.onFoodLoaded(foodList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFoodLoadFailed(databaseError.toException());
            }
        });
    }

    public interface FoodLoadCallback {
        void onFoodLoaded(List<Food> foodList);
        void onFoodLoadFailed(Exception e);
    }
}
