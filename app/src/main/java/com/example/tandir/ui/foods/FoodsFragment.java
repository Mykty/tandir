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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
    View root;

    FragmentManager fragmentManager;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_foods, container, false);

        initViews();
        initFilters();

        return root;
    }

    public void initViews(){
        fragmentManager = getFragmentManager();

        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.fragment_content, new NanFragment());
            ft.commit();
        }

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

                if (fragmentManager != null) {
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.fragment_content, new NanFragment());
                    ft.commit();
                }

            }
        });
        filter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                filter2.setBackgroundResource(R.drawable.border_selected);
                filter2.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_samsa_selected));


                if (fragmentManager != null) {
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.fragment_content, new SamsaFragment());
                    ft.commit();
                }

            }
        });
        filter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                filter3.setBackgroundResource(R.drawable.border_selected);
                filter3.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_butilka_selected));

                if (fragmentManager != null) {
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.fragment_content, new SusinFragment());
                    ft.commit();
                }
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
}