package org.acme;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

public class Products {

    Long id;
    String name;
    String  brand;
    int one_portion; //in grams
    int fat; //in grams
    int carbohydrates; //in grams
    int protein; // in grams

    public Products(Long id, String name, String brand, int one_portion, int fat, int carbohydrates, int protein) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.one_portion = one_portion;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.protein = protein;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setOne_portion(int one_portion) {
        this.one_portion = one_portion;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public int getOne_portion() {
        return one_portion;
    }

    public int getFat() {
        return fat;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public int getProtein() {
        return protein;
    }

    static Products from(Row row) {
        return new Products(row.getLong("id"), row.getString("name"), row.getString("brand"), row.getInteger("one_portion"), row.getInteger("fat"), row.getInteger("carbohydrates"), row.getInteger("protein"));
    }


    public static Multi<Products> findAll(PgPool client) {
        return client
                .query("SELECT id, name, brand, one_portion, fat, carbohydrates, protein FROM Products ORDER BY name DESC")
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(Products::from);
    }

    public static Uni<Boolean> delete(PgPool client, String pr_name, String pr_brand, String u_name, String password) {
        return client
                .preparedQuery("DELETE FROM Products USING Users WHERE Products.name = $1 AND Products.brand = $2 AND Users.name = $3 AND Users.password = $4 AND Users.id = Products.user_id")
                .execute(Tuple.of(pr_name, pr_brand, u_name, password))
                .onItem()
                .transform(m -> m.rowCount() == 1);
    }

    public static Uni<Long> save(PgPool client, String name, String brand, int one_portion, int fat, int carbohydrates, int protein, String u_name, String password) {

        String sql = "INSERT INTO Products (name, brand, one_portion, fat, carbohydrates, protein, user_id) VALUES (" +"\'" + name +"\', "  +"\'" + brand +"\', " + one_portion + ", " + fat +", " + carbohydrates +", "  + protein +", " + "(SELECT id from Users WHERE name = \'" + u_name +"\' AND password = \'" + password +"\')) RETURNING id";

        System.out.println(sql);
        return client
                .preparedQuery(sql)
                .execute()
                .onItem()
                .transform(m -> m.iterator().next().getLong("id"));
    }


    public static Multi<Products> findAllUserProducts(PgPool client, String u_name, String password) {

        return client.preparedQuery("SELECT id, name, brand, one_portion, fat, carbohydrates, protein FROM Products JOIN Users ON Users.name = $1 AND Users.password = $2")
                .execute(Tuple.of(u_name, password)).onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Products::from);
    }



}
