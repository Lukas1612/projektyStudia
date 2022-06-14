package com.example.gymstat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {


    private final MutableLiveData<String> selectedItemMeal = new MutableLiveData<String>();
    public void selectItemMeal(String item) {
        selectedItemMeal.setValue(item);
    }
    public LiveData<String> getSelectedItemMeal() {
        return selectedItemMeal;
    }
}
