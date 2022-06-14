package com.example.gymstat;

public class ProductInMeal extends Product{
    String meal;
    int portions;
    int eaten_products_id;

    public ProductInMeal(String name, String brand,  int kcal, int carbohydrates, int protein, int fat, int id, int one_portion, String meal, int portions, int eaten_products_id) {
        super(name, brand, kcal, carbohydrates, protein, fat, id, one_portion);
        this.meal = meal;
        this.portions = portions;
        this.eaten_products_id = eaten_products_id;
    }

    public int getEaten_products_id() {
        return eaten_products_id;
    }

    public void setEaten_products_id(int eaten_products_id) {
        this.eaten_products_id = eaten_products_id;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    @Override
    public String toString() {

        super.toString();
        return "ProductInMeal{" +  super.toString() +
                "meal='" + meal + '\'' +
                ", portions=" + portions +
                ", eaten_products_id" + eaten_products_id +
                '}';
    }
}



