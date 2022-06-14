package com.example.gymstat;

import java.util.ArrayList;

public class Product {

    private String name;
    private String brand;
    private int kcal;
    private int carbohydrates;
    private int protein;
    private int fat;
    private int id;
    private int one_portion;



    public Product(String name,  String  brand, int kcal, int carbohydrates, int protein, int fat, int id, int one_portion) {
        this.name = name;
        this.brand = brand;
        this.kcal = kcal;
        this.carbohydrates = carbohydrates;
        this.protein = protein;
        this.fat = fat;
        this.id = id;
        this.one_portion = one_portion;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", kcal=" + kcal +
                ", carbohydrates=" + carbohydrates +
                ", protein=" + protein +
                ", fat=" + fat +
                ", id=" + id +
                ", one_portion=" + one_portion +
                '}';
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getOne_portion() {
        return one_portion;
    }

    public void setOne_portion(int one_portion) {
        this.one_portion = one_portion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
