package com.example.tandir.ui.foods;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tandir.R;
import com.example.tandir.adapter.MealAdapter;
import com.example.tandir.module.Meals;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FoodsFragment extends Fragment {

    ImageView filter1, filter2, filter3;
    RecyclerView recyclerViewFoods;
    ArrayList<Meals> listOfProducts = new ArrayList<>();
    View root;
    MealAdapter productAdapter;
    FloatingActionButton floatingActionButton;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_foods, container, false);

        initFilters();
        generateListOfProducts();
        initViews();

        return root;
    }

    public void initViews(){
        recyclerViewFoods = root.findViewById(R.id.recyclerView);
        floatingActionButton = root.findViewById(R.id.floatingActionButton);

        productAdapter = new MealAdapter(listOfProducts);
        recyclerViewFoods.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFoods.setAdapter(productAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), AddFood.class));
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

            }
        });
        filter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                filter2.setBackgroundResource(R.drawable.border_selected);
                filter2.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_samsa_selected));

            }
        });
        filter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                filter3.setBackgroundResource(R.drawable.border_selected);
                filter3.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_butilka_selected));

            }
        });
    }

    public void generateListOfProducts(){
        listOfProducts.add(new Meals("Салат","https://cdn.lifehacker.ru/wp-content/uploads/2018/08/2Cesar_1533672706-630x315.jpeg","Картошка с ново выпеченным",700,200,true));
        listOfProducts.add(new Meals("Манты","http://1cookit.com/uploads/posts/2013-05/1369083155_69910584_0_9ef0_1ecaede5_xl.jpg","Картошка с ново выпеченным",700,200,false));
        listOfProducts.add(new Meals("Котлеты","http://povarenysh.ru/upload/content/ochen-vkusnoe-blyudo.jpg","Картошка с ново выпеченным",700,200,true));
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