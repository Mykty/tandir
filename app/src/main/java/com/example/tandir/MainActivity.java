package com.example.tandir;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.tandir.database.StoreDatabase;
import com.example.tandir.module.Food;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static com.example.tandir.database.StoreDatabase.COLUMN_FCOUNT;
import static com.example.tandir.database.StoreDatabase.COLUMN_FDESC;
import static com.example.tandir.database.StoreDatabase.COLUMN_FKEY;
import static com.example.tandir.database.StoreDatabase.COLUMN_FPRICE;
import static com.example.tandir.database.StoreDatabase.COLUMN_FTYPE;
import static com.example.tandir.database.StoreDatabase.COLUMN_NAME;
import static com.example.tandir.database.StoreDatabase.COLUMN_PHOTO;
import static com.example.tandir.database.StoreDatabase.TABLE_ORDER;

public class MainActivity extends AppCompatActivity {
    BottomNavigationMenuView mbottomNavigationMenuView;
    View view;
    BottomNavigationItemView itemView;
    View cart_badge;
    static TextView cart_badgeTv;
    StoreDatabase storeDb;
    static SQLiteDatabase sqdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);



        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_history, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();
        mbottomNavigationMenuView= (BottomNavigationMenuView) navView.getChildAt(0);
        view = mbottomNavigationMenuView.getChildAt(3);
        itemView = (BottomNavigationItemView) view;

        cart_badge = LayoutInflater.from(this).inflate(R.layout.custom_cart_item_layout, mbottomNavigationMenuView, false);
        cart_badgeTv = cart_badge.findViewById(R.id.cart_badge);

        itemView.addView(cart_badge);
        badgeDefault();
    }

    public static void badgeDefault(){
        Cursor res = sqdb.rawQuery("SELECT * FROM " + TABLE_ORDER, null);
        setBagdeCount(res.getCount());
    }

    public static void setBagdeCount(int n){
        cart_badgeTv.setText(""+n);
    }

    public static void increaseBagdeCount(){
        int n = Integer.parseInt(""+cart_badgeTv.getText())+1;
        cart_badgeTv.setText(""+n);
    }

    public static void decreaseBagdeCount(){
        int n = Integer.parseInt(""+cart_badgeTv.getText())-1;
        cart_badgeTv.setText(""+n);
    }

}
