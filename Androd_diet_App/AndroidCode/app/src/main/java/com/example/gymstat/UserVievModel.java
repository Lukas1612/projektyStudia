package com.example.gymstat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserVievModel extends ViewModel {

    private final MutableLiveData<JSONObject> user = new MutableLiveData<JSONObject>();
    private final MutableLiveData<JSONArray> productsInMeals = new MutableLiveData<JSONArray>();
    private final MutableLiveData<String> currentDate = new MutableLiveData<String>();

    public MutableLiveData<JSONObject> getSelectedItemUser() {
        return user;
    }

    public MutableLiveData<JSONArray> getSelectedItemProductsInMeals() {
        return productsInMeals;
    }

    public MutableLiveData<String> getSelectedItemCurrentDate() {
        return currentDate;
    }

    public void selectItemUser(JSONObject item) {
        user.setValue(item);
    }

    public void selectItemProductsInMeals(JSONArray item) {
        productsInMeals.setValue(item);
    }

    public void selectItemcurrentDate(String item) {
        currentDate.setValue(item);
    }

}

