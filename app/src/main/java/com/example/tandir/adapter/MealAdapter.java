package com.example.tandir.adapter;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tandir.R;
import com.example.tandir.module.ClickItemInterface;
import com.example.tandir.module.Meals;

import java.util.ArrayList;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.EmployeeViewHolder> {
    ClickItemInterface clickItemInterface;
    ArrayList<Meals> dataList;

    public MealAdapter(ArrayList<Meals> dataList) {
        this.dataList = dataList;
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_food, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EmployeeViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Meals product = dataList.get(position);
        holder.title.setText(product.getTitleOfMeal());
        holder.description.setText(product.getDescriptionOfMeal());
        holder.price.setText(product.getPriceOfMeal()  + "Тг");
        Glide.with(holder.itemView)
                .load(product.getImageOfMeal())
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.imageView.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        checkAvailabilityOfMeal(product,holder);
    }

    public void checkAvailabilityOfMeal(Meals product,final EmployeeViewHolder holder){
        if(!product.isAvailable()){
            holder.availability.setBackgroundColor(holder.itemView.getResources().getColor(R.color.colorPrimaryDark));
            holder.availability.setText("Не доступно");
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title;
        TextView description;
        TextView price;
        TextView availability;
        EmployeeViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.titleOfMeal);
            description = itemView.findViewById(R.id.description);
            availability = itemView.findViewById(R.id.inProcess);
            price = itemView.findViewById(R.id.price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickItemInterface.clickItem(getPosition());
                }
            });

        }
    }

    public void setClickListener(ClickItemInterface clickItemInterface){
        this.clickItemInterface = clickItemInterface;
    }
}