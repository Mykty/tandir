package com.example.tandir.ui.badge;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tandir.R;
import com.example.tandir.database.StoreDatabase;
import com.example.tandir.module.Food;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;

import java.util.ArrayList;

import static com.example.tandir.MainActivity.badgeDefault;
import static com.example.tandir.MainActivity.setBagdeCount;
import static com.example.tandir.database.StoreDatabase.COLUMN_FCOUNT;
import static com.example.tandir.database.StoreDatabase.COLUMN_FDESC;
import static com.example.tandir.database.StoreDatabase.COLUMN_FKEY;
import static com.example.tandir.database.StoreDatabase.COLUMN_FPRICE;
import static com.example.tandir.database.StoreDatabase.COLUMN_FTYPE;
import static com.example.tandir.database.StoreDatabase.COLUMN_NAME;
import static com.example.tandir.database.StoreDatabase.COLUMN_PHOTO;
import static com.example.tandir.database.StoreDatabase.TABLE_ORDER;

public class BadgeFragment extends Fragment implements View.OnClickListener {

    View root;
    StoreDatabase storeDb;
    SQLiteDatabase sqdb;
    BadgeAdapter badgeAdapter;
    RecyclerView recyclerView;
    ArrayList<Food> listOfFoods;
    LinearLayoutManager linearLayoutManager;
    LinearLayout oformlenieLayout;
    LinearLayout notOrderedYet;
    Button submitBtn;
    Button cancelBtn;
    TextView initialPrice;
    TextView totalPrice;
    int sumPrice;
    AlertDialog.Builder builder;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_badge, container, false);

        storeDb = new StoreDatabase(getActivity());
        sqdb = storeDb.getWritableDatabase();
        listOfFoods = new ArrayList<>();
        oformlenieLayout = root.findViewById(R.id.oformlenie);
        notOrderedYet = root.findViewById(R.id.notOrderedYet);
        submitBtn = root.findViewById(R.id.submitBtn);
        cancelBtn = root.findViewById(R.id.cancelBtn);
        initialPrice = root.findViewById(R.id.initialPrice);
        totalPrice = root.findViewById(R.id.totalPrice);

        badgeAdapter = new BadgeAdapter(getActivity(), listOfFoods);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(badgeAdapter);

        sumPrice = 0;
        oformlenieLayout.setVisibility(View.GONE);

        printDbStore();
        getFoodFromDb();
        submitBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);


        return root;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.submitBtn:
                startActivity(new Intent(getActivity(), OformlenieActivity.class));

                break;

            case R.id.cancelBtn:
                builder = new AlertDialog.Builder(getActivity());
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                sqdb.delete(TABLE_ORDER, null, null);
                                listOfFoods.clear();
                                badgeAdapter.notifyDataSetChanged();
                                emptyBadgeState();
                                setBagdeCount(0);

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                builder.setMessage("Очистить корзину").setPositiveButton("Да", dialogClickListener)
                        .setNegativeButton("Нет", dialogClickListener).show();

                break;
        }

    }

    public void emptyBadgeState(){
        oformlenieLayout.setVisibility(View.GONE);
        notOrderedYet.setVisibility(View.VISIBLE);
    }

    public void getFoodFromDb(){
        Cursor res = sqdb.rawQuery("SELECT * FROM " + TABLE_ORDER, null);
        if(res != null && res.getCount() > 0) {

            notOrderedYet.setVisibility(View.GONE);
            oformlenieLayout.setVisibility(View.VISIBLE);

            while (res.moveToNext()) {
                String fKey = res.getString(res.getColumnIndex(COLUMN_FKEY));
                String foodPhoto = res.getString(res.getColumnIndex(COLUMN_PHOTO));
                String foodName = res.getString(res.getColumnIndex(COLUMN_NAME));
                String foodDesc = res.getString(res.getColumnIndex(COLUMN_FDESC));
                String foodType = res.getString(res.getColumnIndex(COLUMN_FTYPE));
                int foodCount = res.getInt(res.getColumnIndex(COLUMN_FCOUNT));
                int foodPrice = res.getInt(res.getColumnIndex(COLUMN_FPRICE));

                listOfFoods.add(new Food(fKey, foodPhoto, foodName, foodDesc, foodType, foodCount, foodPrice));
                sumPrice += foodPrice * foodCount;
            }

            badgeAdapter.notifyDataSetChanged();
            setTotalSum(sumPrice);
            setBagdeCount(listOfFoods.size());
        }
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

                Log.i("BadgeFragment","fCount: "+fCount);
                Log.i("BadgeFragment","fName: "+fName);
                Log.i("BadgeFragment","fDesc: "+fDesc);
                Log.i("BadgeFragment","fCount2: "+fCount);
            }
        }
    }

    public void setTotalSum(int sum){
        initialPrice.setText(""+sum+ " тг");
        totalPrice.setText(""+sum+ " тг");
    }

    class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.BasketProductViewHolder> {
        ArrayList<Food> dataList;
        Context context;

        public BadgeAdapter(Context context, ArrayList<Food> dataList) {
            this.context = context;
            this.dataList = dataList;
        }

        @Override
        public BasketProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.item_basket_product, parent, false);
            return new BasketProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final BasketProductViewHolder holder, final int position) {
            final Food food = dataList.get(position);

            int amount = food.getFoodCount();

            holder.title.setText(food.getFoodName());
            holder.price.setText((food.getFoodPrice() * amount) + "Тг");
            holder.amount_count.setText("" + amount);

            Glide.with(holder.productImage)
                    .load(food.getFoodPhoto())
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            holder.productImage.setBackground(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });

            holder.add_circle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int increaseCount = Integer.parseInt(holder.amount_count.getText().toString()) + 1;
                    holder.amount_count.setText("" + increaseCount);
                    holder.price.setText((food.getFoodPrice() * increaseCount) + "Тг");

                    sumPrice += food.getFoodPrice();
                    setTotalSum(sumPrice);

                    Cursor res = sqdb.rawQuery("SELECT * FROM " + TABLE_ORDER + " WHERE " +
                            COLUMN_FKEY + "=?", new String[]{food.getfKey()});

                    if (res != null && res.getCount() > 0){
                        res.moveToFirst();

                        ContentValues updateValues = new ContentValues();
                        updateValues.put(COLUMN_FCOUNT, increaseCount);

                        sqdb.update(TABLE_ORDER, updateValues, COLUMN_FKEY + "='"+ food.getfKey() +"'", null);
                    }

                    badgeDefault();

                }
            });

            holder.del_circle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int decreaseCount = Integer.parseInt(holder.amount_count.getText().toString()) - 1;

                    sumPrice -= food.getFoodPrice();
                    holder.price.setText((food.getFoodPrice() * decreaseCount) + "Тг");
                    setTotalSum(sumPrice);

                    if (decreaseCount == 0) {
                        Toast.makeText(context, "Товар удален из корзины", Toast.LENGTH_LONG).show();
                        dataList.remove(food);
                        notifyDataSetChanged();


                        sqdb.delete(TABLE_ORDER, COLUMN_FKEY + "='"+ food.getfKey() +"'", null);

                    }else{
                        holder.amount_count.setText("" + decreaseCount);

                        ContentValues updateValues = new ContentValues();
                        updateValues.put(COLUMN_FCOUNT, decreaseCount);

                        sqdb.update(TABLE_ORDER, updateValues, COLUMN_FKEY + "='" + food.getfKey()  + "'", null);

                    }

                    if(dataList.size() == 0){
                        emptyBadgeState();
                    }

                    badgeDefault();
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class BasketProductViewHolder extends RecyclerView.ViewHolder {
            ImageView productImage;
            TextView title;
            TextView price;
            TextView amount_count;
            ImageView del_circle;
            ImageView add_circle;

            BasketProductViewHolder(View itemView) {
                super(itemView);

                productImage = itemView.findViewById(R.id.productImage);
                title = itemView.findViewById(R.id.title);
                price = itemView.findViewById(R.id.price);

                add_circle = itemView.findViewById(R.id.add_circle);
                del_circle = itemView.findViewById(R.id.del_circle);
                amount_count = itemView.findViewById(R.id.text_count);

            }
        }

    }
}