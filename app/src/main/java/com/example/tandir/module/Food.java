package com.example.tandir.module;

public class Food {
    String fKey;
    String foodName;
    String foodPhoto;
    String foodDesc;
    String foodType;
    int foodPrice;
    String foodAvailable;
    int foodCount;

    public Food(){}

    public Food(String fKey, String foodPhoto,  String foodName, String foodDesc, String foodType, int foodPrice, String foodAvailable) {
        this.fKey = fKey;
        this.foodName = foodName;
        this.foodPhoto = foodPhoto;
        this.foodDesc = foodDesc;
        this.foodType = foodType;
        this.foodPrice = foodPrice;
        this.foodAvailable = foodAvailable;
    }

    public Food(String fKey, String foodPhoto,  String foodName, String foodDesc, String foodType, int foodCount, int foodPrice) {
        this.fKey = fKey;
        this.foodName = foodName;
        this.foodPhoto = foodPhoto;
        this.foodDesc = foodDesc;
        this.foodType = foodType;
        this.foodPrice = foodPrice;
        this.foodCount = foodCount;
    }

    public int getFoodCount() {
        return foodCount;
    }

    public void setFoodCount(int foodCount) {
        this.foodCount = foodCount;
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

    public String getFoodPhoto() {
        return foodPhoto;
    }

    public void setFoodPhoto(String foodPhoto) {
        this.foodPhoto = foodPhoto;
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

    public boolean isAvailable(){
        if(foodAvailable.equals("t")) return true;

        return false;
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