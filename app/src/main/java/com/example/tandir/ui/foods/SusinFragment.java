package com.example.tandir.ui.foods;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tandir.R;
import com.example.tandir.adapter.FoodAdapter;
import com.example.tandir.database.StoreDatabase;
import com.example.tandir.module.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.tandir.database.StoreDatabase.COLUMN_FAVAILABLE;
import static com.example.tandir.database.StoreDatabase.COLUMN_FDESC;
import static com.example.tandir.database.StoreDatabase.COLUMN_FKEY;
import static com.example.tandir.database.StoreDatabase.COLUMN_FOOD_VER;
import static com.example.tandir.database.StoreDatabase.COLUMN_FPRICE;
import static com.example.tandir.database.StoreDatabase.COLUMN_FTYPE;
import static com.example.tandir.database.StoreDatabase.COLUMN_NAME;
import static com.example.tandir.database.StoreDatabase.COLUMN_PHOTO;
import static com.example.tandir.database.StoreDatabase.TABLE_FOOD;
import static com.example.tandir.database.StoreDatabase.TABLE_VER;

public class SusinFragment extends Fragment {

    RecyclerView recyclerViewFoods;
    ArrayList<Food> listOfProducts;
    View root;
    FoodAdapter productAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    DatabaseReference mDatabaseRef;
    StoreDatabase storeDb;
    SQLiteDatabase sqdb;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_food_item, container, false);

        initViews();
        setupSwipeRefresh();
        checkVersion();
        fillFoods();

        return root;
    }

    public void initViews(){
        recyclerViewFoods = root.findViewById(R.id.recyclerView);
        listOfProducts = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        storeDb = new StoreDatabase(getActivity());
        sqdb = storeDb.getWritableDatabase();

        productAdapter = new FoodAdapter(listOfProducts);
        recyclerViewFoods.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFoods.setAdapter(productAdapter);

    }

    public void fillFoods() {
        Cursor foodCursor = sqdb.rawQuery("SELECT * FROM " + TABLE_FOOD + " WHERE " +
                COLUMN_FTYPE + "=?", new String[]{"susin"});


        listOfProducts.clear();

        if (((foodCursor != null) && (foodCursor.getCount() > 0))) {
            while (foodCursor.moveToNext()) {
                Food food = new Food(
                        foodCursor.getString(foodCursor.getColumnIndex(COLUMN_FKEY)),
                        foodCursor.getString(foodCursor.getColumnIndex(COLUMN_PHOTO)),
                        foodCursor.getString(foodCursor.getColumnIndex(COLUMN_NAME)),
                        foodCursor.getString(foodCursor.getColumnIndex(COLUMN_FDESC)),
                        foodCursor.getString(foodCursor.getColumnIndex(COLUMN_FTYPE)),
                        foodCursor.getInt(foodCursor.getColumnIndex(COLUMN_FPRICE)),
                        foodCursor.getString(foodCursor.getColumnIndex(COLUMN_FAVAILABLE)));

                listOfProducts.add(food);
            }
        }

        mSwipeRefreshLayout.setRefreshing(false);

    }

    public void setupSwipeRefresh() {
        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
    }

    public void refreshItems() {

        if (!isOnline()) {
            Toast.makeText(getActivity(), "Check internet connection", Toast.LENGTH_LONG).show();
        } else {
            checkVersion();
        }

    }

    public void checkVersion() {
        Query myTopPostsQuery = mDatabaseRef.child("food_ver");

        mSwipeRefreshLayout.setRefreshing(true);
        myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String newVersion = dataSnapshot.getValue().toString();
                    if (!getDayCurrentVersion().equals(newVersion)) {
                        refreshFoods(newVersion);
                    } else {
                        onItemsLoadComplete();
                    }
                } else {
                    mDatabaseRef.child("food_ver").setValue(0);
                    Toast.makeText(getActivity(), "food_ver created", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getDayCurrentVersion() {
        Cursor res = sqdb.rawQuery("SELECT "+COLUMN_FOOD_VER+" FROM "+TABLE_VER, null);
        res.moveToNext();
        return res.getString(0);
    }

    public void refreshFoods(String version) {
        new UpdateFoodAsyncTask(getActivity(), recyclerViewFoods, mSwipeRefreshLayout, version, "susin").execute();
    }

    public void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);

    }

    private boolean isOnline() {
        if (isNetworkAvailable()) {
            return true;

        } else {
            Toast.makeText(getActivity(), "Check Internet connection", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}