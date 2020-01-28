package com.example.tandir.ui.foods;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.example.tandir.module.Meals;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

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

public class FoodsFragment extends Fragment {

    ImageView filter1, filter2, filter3;
    RecyclerView recyclerViewFoods;
    ArrayList<Food> listOfProducts, listOfProductsNan, listOfProductSamsa, listOfProductsSusin;
    View root;
    FoodAdapter productAdapter;
    FloatingActionButton floatingActionButton;
    SwipeRefreshLayout mSwipeRefreshLayout;
    DatabaseReference mDatabaseRef;
    StoreDatabase storeDb;
    SQLiteDatabase sqdb;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_foods, container, false);

        initViews();
        setupSwipeRefresh();
        checkVersion();

        fillFoods();
        initFilters();

        return root;
    }
    
    public void initViews(){
        recyclerViewFoods = root.findViewById(R.id.recyclerView);
        floatingActionButton = root.findViewById(R.id.floatingActionButton);

        listOfProducts = new ArrayList<>();
        listOfProductsNan = new ArrayList<>();
        listOfProductSamsa = new ArrayList<>();
        listOfProductsSusin = new ArrayList<>();

        /*listOfProductsNan.add(new Meals("Тандыр нан","https://mir-biz.ru/wp-content/uploads/2017/07/xleb-iz-tandyra1.jpg","тандыр нан",700,200,true));
        listOfProductsNan.add(new Meals("Хлеб в тандыре","https://md-eksperiment.org/images/posts/b9424c45-4b8d-4e0e-9336-ec71e308cf5f.jpeg","Хлеб в тандыре",700,200,false));
        listOfProductsNan.add(new Meals("Узбекские лепешки в тандыре","https://omangale.ru/wp-content/uploads/2016/12/tandyr-nan.jpg","Узбекские лепешки в тандыре",700,200,true));

        listOfProductSamsa.add(new Meals("Самса1","https://e1.edimdoma.ru/data/recipes/0004/8261/48261-ed4_wide.jpg?1468504047","тандыр нан",700,200,true));
        listOfProductSamsa.add(new Meals("Самса2","https://alimero.ru/uploads/images/16/38/88/2018/03/21/1d4634_wmark.jpg","Хлеб в тандыре",700,200,false));
        listOfProductSamsa.add(new Meals("Самса3","https://www.gastronom.ru/binfiles/images/20161118/b0a987b6.jpg","Узбекские лепешки в тандыре",700,200,true));

        listOfProductsSusin.add(new Meals("Сусын1","https://mir-biz.ru/wp-content/uploads/2017/07/xleb-iz-tandyra1.jpg","тандыр нан",700,200,true));
        listOfProductsSusin.add(new Meals("Сусын2","https://md-eksperiment.org/images/posts/b9424c45-4b8d-4e0e-9336-ec71e308cf5f.jpeg","Хлеб в тандыре",700,200,false));
        listOfProductsSusin.add(new Meals("Сусын3","https://omangale.ru/wp-content/uploads/2016/12/tandyr-nan.jpg","Узбекские лепешки в тандыре",700,200,true));
        */

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        storeDb = new StoreDatabase(getActivity());
        sqdb = storeDb.getWritableDatabase();

        listOfProducts = listOfProductsNan;
        productAdapter = new FoodAdapter(listOfProducts);


        recyclerViewFoods.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFoods.setAdapter(productAdapter);

        recyclerViewFoods.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && floatingActionButton.getVisibility() == View.VISIBLE) {
                    floatingActionButton.hide();
                } else if (dy < 0 && floatingActionButton.getVisibility() != View.VISIBLE) {
                    floatingActionButton.show();
                }
            }
        });

    }

    public void initFilters(){

        filter1 = root.findViewById(R.id.filter1);
        filter2 = root.findViewById(R.id.filter2);
        filter3 = root.findViewById(R.id.filter3);

        filter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                filter1.setBackgroundResource(R.drawable.border_selected);
                filter1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_nan_selected));

                listOfProducts = listOfProductsNan;

                productAdapter = new FoodAdapter(listOfProducts);
                recyclerViewFoods.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewFoods.setAdapter(productAdapter);

            }
        });
        filter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                filter2.setBackgroundResource(R.drawable.border_selected);
                filter2.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_samsa_selected));

                listOfProducts = listOfProductSamsa;

                productAdapter = new FoodAdapter(listOfProducts);
                recyclerViewFoods.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewFoods.setAdapter(productAdapter);
                Toast.makeText(getActivity(), "filter2", Toast.LENGTH_SHORT).show();

            }
        });
        filter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                filter3.setBackgroundResource(R.drawable.border_selected);
                filter3.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_butilka_selected));
                listOfProducts = listOfProductsSusin;
                productAdapter = new FoodAdapter(listOfProducts);
                recyclerViewFoods.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewFoods.setAdapter(productAdapter);

                Toast.makeText(getActivity(), "filter3", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void setDefault(){

        filter1.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_nan));
        filter2.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_samsa));
        filter3.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_butilka));

        filter1.setBackgroundResource(R.drawable.border);
        filter2.setBackgroundResource(R.drawable.border);
        filter3.setBackgroundResource(R.drawable.border);
    }

    public void fillFoods() {
        Cursor foodCursor = sqdb.rawQuery("SELECT * FROM " + TABLE_FOOD, null);

        listOfProductsNan.clear();
        listOfProductSamsa.clear();
        listOfProductsSusin.clear();

//public Food(String fKey, String foodPhoto,  String foodName, String foodDesc, String foodType, int foodPrice, String foodAvailable) {

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

                if(food.getFoodType().equals("nan")) listOfProductsNan.add(food);
                if(food.getFoodType().equals("samsa")) listOfProductSamsa.add(food);
                if(food.getFoodType().equals("susin")) listOfProductsSusin.add(food);

            }
        }

        listOfProducts = listOfProductsNan;
//        Log.i("array", listOfProducts.get(0).getFoodName());
        mSwipeRefreshLayout.setRefreshing(false);
        productAdapter.notifyDataSetChanged();

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

        Log.i("FoodsFragment", "UpdateFoodAsyncTask");
        new UpdateFoodAsyncTask(getActivity(), recyclerViewFoods, mSwipeRefreshLayout, version).execute();
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