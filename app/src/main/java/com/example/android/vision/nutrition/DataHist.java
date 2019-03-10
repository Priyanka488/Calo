package com.example.android.vision.nutrition;

public class DataHist
{

    public String itemName;
    public double calories;

    public DataHist(){}

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getCalories() {
        return calories;
    }

    public String getItemName() {
        return itemName;
    }
}
