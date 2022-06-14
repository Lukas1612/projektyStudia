package org.acme;

import io.smallrye.jwt.algorithm.SignatureAlgorithm;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import io.smallrye.jwt.util.KeyUtils;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import org.eclipse.microprofile.jwt.Claims;


import javax.management.relation.Role;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

public class Users {

    private Long id;
    private String gender;
    int weight;
    int height;
    int age;
    String name;
    String password;

    public Users(Long id, String gender, int weight, int height, int age, String name, String password) {
        this.id = id;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.age = age;
        this.name = name;
        this.password = password;
    }

    public Users()
    {

    }



    public Long getId() {
        return id;
    }

    public String getGender() {
        return gender;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static Multi<Users> findAll(PgPool client) {
        return client
                .query("SELECT * FROM Users ORDER BY name DESC")
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(Users::from);
    }

    public static Multi<Users> findByNameAndPassword(PgPool client, String name, String password) {
        return client.preparedQuery("SELECT * FROM Users WHERE name = $1 AND password = $2")
                .execute(Tuple.of(name, password)).onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Users::from);
    }



   /* public static Multi<Movie> findById(PgPool client, Long id) {
        return client
                .preparedQuery("SELECT id, title FROM movies WHERE id = $1")
                .execute(Tuple.of(id))
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(Movie::from);
    }*/

    public static Uni<Long> save(PgPool client, String gender, Integer weight, Integer height, Integer age, String name, String password) {

        String sql = "INSERT INTO Users (gender, weight, height, age, name, password) VALUES (" +"\'" + gender+"\', "  +"\'" + weight +"\', " +"\'" + height +"\', " +"\'" + age +"\', " +"\'" + name +"\', "  +"\'" + password +"\'" + ") RETURNING id";
        return client
                .preparedQuery(sql)
                .execute()
                .onItem()
                .transform(m -> m.iterator().next().getLong("id"));
    }
    
    //*****************************************************************
    //example of INSERT withe value selected from diffrent table, not for use as actual endpoint
    public static Uni<Long> save2(PgPool client, String gender, Integer weight, Integer height, Integer age, String name, String password) {

        String sql = "INSERT INTO Users (gender, weight, height, age, name, password) VALUES (" + "(SELECT first_meal_name from Meals_schedule WHERE id = 1)" +", "  +"\'" + weight +"\', " +"\'" + height +"\', \'" + age +"\', " +"\'" + name +"\', "  +"\'" + password +"\'" + ") RETURNING id";
        return client
                .preparedQuery(sql)
                .execute()
                .onItem()
                .transform(m -> m.iterator().next().getLong("id"));
    }
    //*****************************************************************
    //*****************************************************************

    public static Uni<Boolean> delete(PgPool client, String name, String password) {
        return client
                .preparedQuery("DELETE FROM Users WHERE name = $1 AND password = $2")
                .execute(Tuple.of(name, password))
                .onItem()
                .transform(m -> m.rowCount() == 1);
    }

    public static Uni<Boolean> chechIfNameExist(PgPool client, String name)
    {
        return client
                .preparedQuery("SELECT name FROM Users WHERE name = $1")
                .execute(Tuple.of(name))
                .onItem()
                .transform(m -> m.rowCount() == 1);
    }

    static Users from(Row row) {
        return new Users(row.getLong("id"), row.getString("gender"), row.getInteger("weight"), row.getInteger("height"), row.getInteger("age"), row.getString("name"), row.getString("password"));
    }


    public static Uni<String> generateJWT(PgPool client, String name, String password) {
        return client.preparedQuery("SELECT * FROM Users WHERE name = $1 AND password = $2")
                .execute(Tuple.of(name, password)).onItem().transform(m -> generateToken(m.iterator().next().getString("name"), m.iterator().next().getString("password")));
    }

    public static String generateToken(String name, String password) {

        try {
            return Jwt.issuer("http://localhost:8080/issuer")
                    .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                    .claim("name", name).claim("password", password)
                    .sign(KeyUtils.readPrivateKey("privateKey.pem"));

        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

}
