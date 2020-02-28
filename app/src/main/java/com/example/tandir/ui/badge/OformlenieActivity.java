package com.example.tandir.ui.badge;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tandir.R;
import com.example.tandir.database.StoreDatabase;
import com.example.tandir.module.Food;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;

import static com.example.tandir.MainActivity.setBagdeCount;
import static com.example.tandir.database.StoreDatabase.COLUMN_FCOUNT;
import static com.example.tandir.database.StoreDatabase.COLUMN_FDESC;
import static com.example.tandir.database.StoreDatabase.COLUMN_FKEY;
import static com.example.tandir.database.StoreDatabase.COLUMN_FPRICE;
import static com.example.tandir.database.StoreDatabase.COLUMN_FTYPE;
import static com.example.tandir.database.StoreDatabase.COLUMN_NAME;
import static com.example.tandir.database.StoreDatabase.COLUMN_PHOTO;
import static com.example.tandir.database.StoreDatabase.TABLE_ORDER;

public class OformlenieActivity extends AppCompatActivity {
    StoreDatabase storeDb;
    static SQLiteDatabase sqdb;
    EditText userPhone;
    Button finishBtn;
    int sumPrice = 0;
    int countOrder = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oformlenie);

        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();
        userPhone = findViewById(R.id.userPhone);
        finishBtn = findViewById(R.id.finishBtn);

        getFoodFromDb();
        String orderTxt = "Перейти к оплате\n"+countOrder+" товара, "+sumPrice+" Тг";

        finishBtn.setText(orderTxt);

        userPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int i1, int i2) {
                //0123456789
                //+7 747 119 9701
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void getFoodFromDb(){
        Cursor res = sqdb.rawQuery("SELECT count, price  FROM " + TABLE_ORDER, null);
        if(res != null && res.getCount() > 0) {
            countOrder = res.getCount();

            while (res.moveToNext()) {
                int foodCount = res.getInt(res.getColumnIndex(COLUMN_FCOUNT));
                int foodPrice = res.getInt(res.getColumnIndex(COLUMN_FPRICE));

                sumPrice += foodPrice * foodCount;
            }
        }
    }
}