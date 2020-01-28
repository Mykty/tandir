package com.example.tandir.adapter;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tandir.R;
import com.example.tandir.module.ClickItemInterface;
import com.example.tandir.module.Food;
import com.example.tandir.module.Meals;

import java.util.ArrayList;

import static com.example.tandir.MainActivity.decreaseBagdeCount;
import static com.example.tandir.MainActivity.increaseBagdeCount;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.EmployeeViewHolder> {
    ClickItemInterface clickItemInterface;
    ArrayList<Food> dataList;

    public FoodAdapter(ArrayList<Food> dataList) {
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
        Food food = dataList.get(position);

        holder.title.setText(food.getFoodName());
        holder.description.setText(food.getFoodDesc());
        holder.price.setText(food.getFoodPrice()  + "Тг / шт");

        Glide.with(holder.itemView)
                .load(food.getFoodPhoto())
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        holder.imageView.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        checkAvailabilityOfMeal(food, holder);

        holder.add_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.text_count.setVisibility(View.VISIBLE);
                holder.del_circle.setVisibility(View.VISIBLE);

                holder.text_count.setText("" + (Integer.parseInt(holder.text_count.getText().toString()) + 1));
                increaseBagdeCount();
            }
        });

        holder.del_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.text_count.setVisibility(View.VISIBLE);
                holder.del_circle.setVisibility(View.VISIBLE);

                int c = Integer.parseInt(holder.text_count.getText().toString());

                if (c != 0) {
                    holder.text_count.setText("" + (c - 1));

                    if ((c - 1) == 0) {
                        holder.text_count.setVisibility(View.GONE);
                        holder.del_circle.setVisibility(View.GONE);
                    }
                    decreaseBagdeCount();
                }

            }
        });
    }

    public void checkAvailabilityOfMeal(Food product,final EmployeeViewHolder holder){
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
        TextView text_count;
        ImageView del_circle;
        ImageView add_circle;

        EmployeeViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.titleOfMeal);
            description = itemView.findViewById(R.id.description);
            availability = itemView.findViewById(R.id.inProcess);
            price = itemView.findViewById(R.id.price);

            add_circle = itemView.findViewById(R.id.add_circle);
            del_circle = itemView.findViewById(R.id.del_circle);
            text_count = itemView.findViewById(R.id.text_count);

        }
    }

    public void setClickListener(ClickItemInterface clickItemInterface){
        this.clickItemInterface = clickItemInterface;
    }
}