package org.acme;


import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.pgclient.PgPool;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.*;

//RSA encryption decryption

@Path("app")
@RequestScoped
public class UsersResource {

    @Inject
    PgPool client;

    @Inject
    JsonWebToken jwt;

    @PostConstruct
    void config() {
    }

    


    @POST
    @Path("authenticate")
    public Uni<String> getJwt(JsonObject body) {
        System.out.println("body " + body.toString());
        return Users.generateJWT(client, body.getString("name"), body.getString("password"));
    }
//**********************************************
//********  Users endpoints  *******************

    //add user
    @POST
    @Path("users")
    public Uni<Long> add(JsonObject body) {

        return Users.save(client, body.getString("gender"), body.getInteger("weight"), body.getInteger("height"), body.getInteger("age"), body.getString("name"), body.getString("password"));
    }


    //get user by password and name
    @GET
    @RolesAllowed({"User", "Admin"})
    @Path("users")
    public Multi<Users> getUNP() {

        return Users.findByNameAndPassword(client, jwt.getClaim("name").toString(), jwt.getClaim("password").toString());

    }


    //delate user by password and name
    @DELETE
    @Path("users")
    public Uni<Boolean> delate(JsonObject body) {


        return Users.delete(client, body.getString("name"), body.getString("password"));
    }

    //check if name already exist
    @GET
    @PermitAll
    @Path("username/name={name}")
    public Uni<Boolean> checkName(@PathParam("name") String name) {
        return Users.chechIfNameExist(client, name);
    }

//********  Users endpoints  *******************
//**********************************************


//*************************************************
//********  Products endpoints  *******************


    //save product
    @POST
    @PermitAll
    @Path("products")
    public Uni<Long> addProduct(JsonObject product) {
        System.out.println(product);
        return Products.save(client, product.getString("product_name"), product.getString("brand"), product.getInteger("one_portion"), product.getInteger("fat"), product.getInteger("carbohydrates"), product.getInteger("protein"), product.getString("user_name"), product.getString("password"));
    }

    //get all products from database
    @GET
    @PermitAll
    @Path("products")
    public Multi<Products> getProduct() {
        return Products.findAll(client);
    }


    //get al products that was added by given User
    @GET
    @RolesAllowed({"User", "Admin"})
    @Path("userProducts")
    public Multi<Products> getUserProduct() {

        return Products.findAllUserProducts(client, jwt.getClaim("name").toString(), jwt.getClaim("password").toString());
    }


    @DELETE
    @Path("products")
    public Uni<Boolean> deleteProduct(JsonObject productData) {
        return Products.delete(client, productData.getString("product_name"), productData.getString("brand"), productData.getString("user_name"), productData.getString("password"));
    }
//********  Products endpoints  *******************
//*************************************************


//*************************************************
//********  Eaten_products endpoints  *************

    @POST
    @Path("eaten_products")
    public Uni<Long> saveEatenProduct(JsonObject productData) {
        System.out.println(productData.getString("meal"));
        return Eaten_products.save(client, productData.getLong("product_id"), productData.getString("day"), productData.getString("name"), productData.getString("password"), productData.getString("meal"), productData.getInteger("portions"));
    }

/*
    @GET
    @RolesAllowed({"User", "Admin"})
    @Path("eaten_products/day={day}")
    public Multi<Eaten_products> getEatenProduct(@PathParam("day") String day) {
        return Eaten_products.findByNamePasswordAndDay(client, jwt.getClaim("name").toString(), jwt.getClaim("password").toString(), day);
    }*/

    @GET
   @RolesAllowed({"User", "Admin"})
    @Path("eaten_products/day={day}")
    public Multi<ProductInMeal> getEatenProduct(@PathParam("day") String day) {

        return ProductInMeal.findByNamePasswordAndDay(client, jwt.getClaim("name").toString(), jwt.getClaim("password").toString(), day);

    }

    @GET
    @RolesAllowed({"User", "Admin"})
    @Path("eaten_products/firstDay={firstDay}&lastDay={lastDay}")
    public Multi<ProductInMeal> getEatenProduct(@PathParam("firstDay") String firstDay, @PathParam("lastDay") String lastDay) {

        return ProductInMeal.findByNamePasswordAndPeriod(client, jwt.getClaim("name").toString(), jwt.getClaim("password").toString(), firstDay, lastDay);
    }


    @GET
    @RolesAllowed({"User", "Admin"})
    @Path("eaten_products")
    public Multi<Eaten_products> getEatenProducts() {

       return Eaten_products.findByNameAndPassword(client, jwt.getClaim("name").toString(), jwt.getClaim("password").toString());

    }


    @POST
    @Path("eaten_products/delete")
    public Uni<Boolean> eatenProductsDelete(JsonObject productData) {
        System.out.println(" productData " + productData.toString() +"  <<");
        return Eaten_products.delete(client, productData.getString("name"), productData.getString("password"), productData.getInteger("eaten_products_id"));
    }

//********  Eaten_products endpoints **************
//*************************************************

}
