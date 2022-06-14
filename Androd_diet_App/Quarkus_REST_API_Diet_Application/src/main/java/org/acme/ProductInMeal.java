package org.acme;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

public class ProductInMeal extends Products{
    public String meal;
    public int portions;
    public int eaten_products_id;

    public ProductInMeal(Long id, String name, String brand, int one_portion, int fat, int carbohydrates, int protein, String meal, int portions,  int eaten_products_id) {
        super(id, name, brand, one_portion, fat, carbohydrates, protein);
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

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    static ProductInMeal from(Row row) {
        return new ProductInMeal(row.getLong("id"), row.getString("name"), row.getString("brand"), row.getInteger("one_portion"), row.getInteger("fat"), row.getInteger("carbohydrates"), row.getInteger("protein"), row.getString("meal"), row.getInteger("portions"), row.getInteger("eaten_products_id"));
    }

    public static Multi<ProductInMeal> findByNamePasswordAndDay(PgPool client, String u_name, String password, String day) {

        return client.preparedQuery("SELECT Products.id, Products.name, Products.brand, Products.one_portion, Products.fat, Products.carbohydrates, Products.protein, Eaten_products.meal, Eaten_products.portions, Eaten_products.id as eaten_products_id  FROM Products JOIN Users ON Users.name = $1 AND Users.password = $2 JOIN Eaten_products ON Eaten_products.product_id = Products.id and Eaten_products.user_id = Users.id and Eaten_products.date = \'" + day +"\'")
                .execute(Tuple.of(u_name, password)).onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(ProductInMeal::from);
    }

    public static Multi<ProductInMeal> findByNamePasswordAndPeriod(PgPool client, String u_name, String password, String firstDay, String lastDay) {

        return client.preparedQuery("SELECT Products.id, Products.name, Products.brand, Products.one_portion, Products.fat, Products.carbohydrates, Products.protein, Eaten_products.meal, Eaten_products.portions, Eaten_products.id as eaten_products_id FROM Products JOIN Users ON Users.name = $1 AND Users.password = $2 JOIN Eaten_products ON Eaten_products.product_id = Products.id and Eaten_products.user_id = Users.id and Eaten_products.date >= \'" + firstDay +"\' and Eaten_products.date <= \'" + lastDay +"\'")
                .execute(Tuple.of(u_name, password)).onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(ProductInMeal::from);
    }



}
