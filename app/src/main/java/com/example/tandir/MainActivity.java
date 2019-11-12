package com.example.tandir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    BottomNavigationMenuView mbottomNavigationMenuView;
    View view;
    BottomNavigationItemView itemView;
    View cart_badge;
    static TextView cart_badgeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_history, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        mbottomNavigationMenuView= (BottomNavigationMenuView) navView.getChildAt(0);
        view = mbottomNavigationMenuView.getChildAt(3);
        itemView = (BottomNavigationItemView) view;

        cart_badge = LayoutInflater.from(this).inflate(R.layout.custom_cart_item_layout, mbottomNavigationMenuView, false);
        cart_badgeTv = cart_badge.findViewById(R.id.cart_badge);

        itemView.addView(cart_badge);
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
