package com.example.tandir.ui.foods;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tandir.adapter.FoodAdapter;
import com.example.tandir.database.StoreDatabase;
import com.example.tandir.module.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.tandir.database.StoreDatabase.COLUMN_FAVAILABLE;
import static com.example.tandir.database.StoreDatabase.COLUMN_FDESC;
import static com.example.tandir.database.StoreDatabase.COLUMN_FKEY;
import static com.example.tandir.database.StoreDatabase.COLUMN_FPRICE;
import static com.example.tandir.database.StoreDatabase.COLUMN_FTYPE;
import static com.example.tandir.database.StoreDatabase.COLUMN_NAME;
import static com.example.tandir.database.StoreDatabase.COLUMN_PHOTO;
import static com.example.tandir.database.StoreDatabase.TABLE_FOOD;

public class UpdateFoodAsyncTask extends AsyncTask<Void, Food, Void> {

    ArrayList<Food> foodList = new ArrayList<>();
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    StoreDatabase storeDb;
    SQLiteDatabase sqdb;
    DatabaseReference mDatabaseRef, foodRef;
    FoodAdapter foodAdapter;
    Context context;
    String version = "";
    String fType = "";

    public UpdateFoodAsyncTask(Context context, RecyclerView recyclerView, SwipeRefreshLayout refreshLayout, String version, String fType) {
        this.recyclerView = recyclerView;
        this.swipeRefreshLayout = refreshLayout;
        this.context = context;
        this.version = version;
        this.fType = fType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        swipeRefreshLayout.setRefreshing(true);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        storeDb = new StoreDatabase(context);
        sqdb = storeDb.getWritableDatabase();
        foodRef = mDatabaseRef.child("food_list");
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Cursor res = sqdb.rawQuery("SELECT food_ver FROM versions ", null);
        res.moveToNext();
        String getDay = res.getString(0);

        ContentValues versionValues = new ContentValues();
        versionValues.put("food_ver", version);
        sqdb.update("versions", versionValues, "food_ver=" + getDay, null);

        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foodList.clear();
                storeDb.cleanFoods(sqdb);

                for (DataSnapshot foodsSnapshot : dataSnapshot.getChildren()) {
                        Food food = foodsSnapshot.getValue(Food.class);

                        String fKey = food.getfKey();
                        String foodPhoto = food.getFoodPhoto();
                        String foodName = food.getFoodName();
                        String foodDesc = food.getFoodDesc();
                        String foodType = food.getFoodType();
                        int foodPrice = food.getFoodPrice();
                        String foodAvailable = food.getFoodAvailable();

                        ContentValues foodValue = new ContentValues();
                        foodValue.put(COLUMN_FKEY, fKey);
                        foodValue.put(COLUMN_PHOTO, foodPhoto);
                        foodValue.put(COLUMN_NAME, foodName);
                        foodValue.put(COLUMN_FDESC, foodDesc);
                        foodValue.put(COLUMN_FAVAILABLE, foodAvailable);
                        foodValue.put(COLUMN_FTYPE, foodType);
                        foodValue.put(COLUMN_FPRICE, foodPrice);
                        
                        sqdb.insert(TABLE_FOOD, null, foodValue);


                        if(fType.equals("nan") && food.getFoodType().equals("nan"))
                            foodList.add(new Food(fKey, foodPhoto, foodName, foodDesc, foodType, foodPrice, foodAvailable));
                        if(fType.equals("samsa") && food.getFoodType().equals("samsa"))
                            foodList.add(new Food(fKey, foodPhoto, foodName, foodDesc, foodType, foodPrice, foodAvailable));
                        if(fType.equals("susin") && food.getFoodType().equals("susin"))
                            foodList.add(new Food(fKey, foodPhoto, foodName, foodDesc, foodType, foodPrice, foodAvailable));

                }

                Collections.reverse(foodList);
                foodAdapter = new FoodAdapter(foodList);
                recyclerView.setAdapter(foodAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return null;
    }

    @Override
    protected void onProgressUpdate(Food... values) {
        super.onProgressUpdate(values);
        foodList.add(values[0]);
        foodAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        swipeRefreshLayout.setRefreshing(false);
    }
}

