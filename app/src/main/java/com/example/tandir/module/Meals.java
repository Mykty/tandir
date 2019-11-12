package com.example.tandir.module;

public class Meals {
    String titleOfMeal;
    String imageOfMeal;
    String descriptionOfMeal;
    int priceOfMeal;
    int amountOfMeal;
    boolean available;

    public Meals(String titleOfMeal, String imageOfMeal, String descriptionOfMeal, int priceOfMeal, int amount,boolean available) {
        this.titleOfMeal = titleOfMeal;
        this.imageOfMeal = imageOfMeal;
        this.descriptionOfMeal = descriptionOfMeal;
        this.priceOfMeal = priceOfMeal;
        this.amountOfMeal = amount;
        this.available = available;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getTitleOfMeal() {
        return titleOfMeal;
    }

    public String getImageOfMeal() {
        return imageOfMeal;
    }

    public String getDescriptionOfMeal() {
        return descriptionOfMeal;
    }

    public int getPriceOfMeal() {
        return priceOfMeal;
    }

    public int getAmount() {
        return amountOfMeal;
    }
}