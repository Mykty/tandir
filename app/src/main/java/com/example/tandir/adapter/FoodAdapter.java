package com.example.tandir.adapter;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
import com.example.tandir.database.StoreDatabase;
import com.example.tandir.module.ClickItemInterface;
import com.example.tandir.module.Food;
import com.example.tandir.module.Meals;

import java.util.ArrayList;

import static com.example.tandir.MainActivity.badgeDefault;
import static com.example.tandir.database.StoreDatabase.COLUMN_FCOUNT;
import static com.example.tandir.database.StoreDatabase.COLUMN_FDESC;
import static com.example.tandir.database.StoreDatabase.COLUMN_FKEY;
import static com.example.tandir.database.StoreDatabase.COLUMN_FPRICE;
import static com.example.tandir.database.StoreDatabase.COLUMN_FTYPE;
import static com.example.tandir.database.StoreDatabase.COLUMN_NAME;
import static com.example.tandir.database.StoreDatabase.COLUMN_PHOTO;
import static com.example.tandir.database.StoreDatabase.TABLE_ORDER;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.EmployeeViewHolder> {
    ClickItemInterface clickItemInterface;
    ArrayList<Food> dataList;
    StoreDatabase storeDb;
    SQLiteDatabase sqdb;

    public FoodAdapter(ArrayList<Food> dataList) {
        this.dataList = dataList;
    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_food, parent, false);
        storeDb = new StoreDatabase(parent.getContext());
        sqdb = storeDb.getWritableDatabase();
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EmployeeViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Food food = dataList.get(position);

        final String fKey = food.getfKey();
        final String foodPhoto = food.getFoodPhoto();
        final String foodName = food.getFoodName();
        final String foodDesc = food.getFoodDesc();
        final String foodType = food.getFoodType();
        final int foodPrice = food.getFoodPrice();

        holder.title.setText(foodName);
        holder.description.setText(foodDesc);
        holder.price.setText(foodPrice  + "Тг / шт");

        Glide.with(holder.itemView)
                .load(foodPhoto)
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


                Cursor res = sqdb.rawQuery("SELECT * FROM " + TABLE_ORDER + " WHERE " +
                        COLUMN_FKEY + "=?", new String[]{fKey});

                if (res != null && res.getCount() > 0){
                    res.moveToFirst();
                    String fCount = res.getString(res.getColumnIndex(StoreDatabase.COLUMN_FCOUNT));

                    ContentValues updateValues = new ContentValues();
                    updateValues.put(COLUMN_FCOUNT, (Integer.parseInt(fCount) + 1));

                    sqdb.update(TABLE_ORDER, updateValues, COLUMN_FKEY + "='"+ fKey +"'", null);

                }else{

                    ContentValues foodValue = new ContentValues();
                    foodValue.put(COLUMN_FKEY, fKey);
                    foodValue.put(COLUMN_PHOTO, foodPhoto);
                    foodValue.put(COLUMN_NAME, foodName);
                    foodValue.put(COLUMN_FDESC, foodDesc);
                    foodValue.put(COLUMN_FTYPE, foodType);
                    foodValue.put(COLUMN_FCOUNT, 1);
                    foodValue.put(COLUMN_FPRICE, foodPrice);

                    sqdb.insert(TABLE_ORDER, null, foodValue);
                }
                badgeDefault();
                printDbStore();

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
                }

                Cursor res = sqdb.rawQuery("SELECT * FROM " + TABLE_ORDER + " WHERE " +
                        COLUMN_FKEY + "=?", new String[]{fKey});

                if (res != null && res.getCount() > 0){
                    res.moveToFirst();
                    int fCount = res.getInt(res.getColumnIndex(StoreDatabase.COLUMN_FCOUNT));

                    if(fCount == 1){
                        sqdb.delete(TABLE_ORDER, COLUMN_FKEY + "='"+ fKey +"'", null);

                    }else {
                        ContentValues updateValues = new ContentValues();
                        updateValues.put(COLUMN_FCOUNT, (fCount - 1));

                        sqdb.update(TABLE_ORDER, updateValues, COLUMN_FKEY + "='" + fKey + "'", null);
                    }
                }
                badgeDefault();

                printDbStore();
            }
        });
    }

    public void printDbStore(){
        Cursor res = sqdb.rawQuery("SELECT * FROM " + TABLE_ORDER, null);

        Log.i("FoodAdapter","printDbStore: ");
        if(res != null && res.getCount() > 0) {
            Log.i("FoodAdapter","moveToFirst: ");
            while (res.moveToNext()) {

                String fName = res.getString(res.getColumnIndex(COLUMN_NAME));
                String fDesc = res.getString(res.getColumnIndex(COLUMN_FDESC));
                int fCount = res.getInt(res.getColumnIndex(COLUMN_FCOUNT));
            }
        }
    }

    public void checkAvailabilityOfMeal(Food product,final EmployeeViewHolder holder){
        if(!product.isAvailable()){
            holder.availability.setBackgroundColor(holder.itemView.getResources().getColor(R.color.colorPrimaryDark));
            holder.availability.setText("Не доступно");
            holder.add_circle.setVisibility(View.INVISIBLE);
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