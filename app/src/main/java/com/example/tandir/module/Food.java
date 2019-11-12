package com.example.tandir.module;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

public class Food {
    String fKey;
    String foodName;
    String foodDesc;
    String foodType;
    int foodPrice;
    String foodAvailable;

    public Food(String fKey, String foodName, String foodDesc, String foodType, int foodPrice, String foodAvailable) {
        this.fKey = fKey;
        this.foodName = foodName;
        this.foodDesc = foodDesc;
        this.foodType = foodType;
        this.foodPrice = foodPrice;
        this.foodAvailable = foodAvailable;
    }

    public String getfKey() {
        return fKey;
    }

    public void setfKey(String fKey) {
        this.fKey = fKey;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(int foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodAvailable() {
        return foodAvailable;
    }

    public void setFoodAvailable(String foodAvailable) {
        this.foodAvailable = foodAvailable;
    }
}