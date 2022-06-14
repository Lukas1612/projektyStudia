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
//********  Passages endpoints  *******************

    @GET
    @PermitAll
    @Path("passages/")
    public Multi<Passages> getPassages() {

        System.out.println(" -->> ");
        return Passages.findAll(client);
    }

//********  Passages endpoints  *******************
//*************************************************

//*************************************************
//********  Tips endpoints  *******************

    @GET
    @PermitAll
    @Path("tips/id={id}")
    public Multi<PassageTips> getTips(@PathParam("id") Integer id) {

        System.out.println(" -->> ");
        return PassageTips.findTipsById(client, id);
    }

//********  Tips endpoints  ***********************
//*************************************************

//*************************************************
//********  Described Passage endpoints  ***********


    @GET
    @PermitAll
    @Path("describedPassage/language={name}")
    public Multi<DescribedPassage> getAllDescribedPassages(@PathParam("name") String name) {

        return DescribedPassage.findAll(client, name);
    }

//********  DescribedPassage endpoints  ***********
//*************************************************

//*************************************************
//********  Flashcards Passage endpoints  ***********

    @POST
    @Path("flashcard")
    public Uni<Long> saveFlashcard(JsonObject productData) {

        return Flashcard.save(client, productData.getString("name"), productData.getString("password"), productData.getString("word"), productData.getString("translation"), productData.getString("example_sentence"), productData.getString("translated_sentence"), productData.getInteger("group_id"));
    }

    @GET
    @RolesAllowed({"User", "Admin"})
    @Path("flashcard")
    public Multi<Flashcard> getFlashcard() {


        return Flashcard.findByNameAndPassword(client, jwt.getClaim("name").toString(), jwt.getClaim("password").toString());
    }

    @DELETE
    @RolesAllowed({"User", "Admin"})
    @Path("flashcard/id={id}")
    public Uni<Boolean> deleteFlashcard(@PathParam("id") Integer id) {

        System.out.println(jwt.getClaim("name").toString() + jwt.getClaim("password").toString());
        return Flashcard.delete(client, jwt.getClaim("name").toString(), jwt.getClaim("password").toString(), id);
    }

    @GET
    @RolesAllowed({"User", "Admin"})
    @Path("flashcards_in_groups")
    public Multi<Flashcards_in_groups> geyFlashcardInGroups() {
        return  Flashcards_in_groups.findAll(client, jwt.getClaim("name").toString(), jwt.getClaim("password").toString());
    }

//********  Flashcards endpoints  *****************
//*************************************************



//*************************************************
//********  LearningUnits endpoints  ***********


    @GET
    @RolesAllowed({"User", "Admin"})
    @Path("LearningUnits/language={name}")
    public Multi<LearningUnit> getFlashcard(@PathParam("name") String name) {


        return LearningUnit.findAll(client, name, jwt.getClaim("name").toString(), jwt.getClaim("password").toString());
    }



//********  LearningUnits endpoints  ***********
//*************************************************


// *************************************************
//********  user tests *****************************


    @DELETE
    @Path("user_tests/id={id}")
    public Uni<Boolean> deleteScore(@PathParam("id") Integer id) {

        System.out.println(" delete flashcard: " + jwt.getClaim("name").toString() + " " + jwt.getClaim("password").toString() + " " + id);
        return UserTests.delete(client, jwt.getClaim("name").toString(), jwt.getClaim("password").toString(), id);
    }

    @POST
    @Path("user_tests")
    public Uni<Long> saveUserTest(JsonObject productData) {

        System.out.println(productData.toString());
        return  UserTests.save(client, productData.getInteger("test_passage_id"), productData.getString("score"), productData.getString("date"),  productData.getString("name"),  productData.getString("password"));
    }


    @GET
    @RolesAllowed({"User", "Admin"})
    @Path("user_tests")
    public Multi<UserTests> geyUserTest() {
        return  UserTests.findAll(client, jwt.getClaim("name").toString(), jwt.getClaim("password").toString());
    }
//********  user tests ****************************
//*************************************************


//*************************************************
//********  questionnaire questions ***************

    @GET
    @Path("questionnaire_questions")
    public Multi<QuestionnaireQuestions> getQuestionnaireQuestions() {
        return  QuestionnaireQuestions.findAll(client);
    }

//********  questionnaire questions ***************
//*************************************************



//*************************************************
//********  Passages Answers Tier *****************


    @POST
    @Path("Passages_answers_tier")
    public Uni<Long> saveUserAnswersTier(JsonObject productData) {

        System.out.println("tier " + productData.toString());
        return PassagesAnswersTier.save(client, productData.getInteger("tier"), productData.getInteger("passage_id"), productData.getInteger("question_id"), productData.getString("name"), productData.getString("password"));
    }


//********  Passages Answers Tier *****************
//*************************************************

//*************************************************
//******** User Questionnaire Questions ***********


    @POST
    @Path("user_questionnaire_questions")
    public Uni<Long> saveUserQuestionnaireQuestions(JsonObject productData) {
        return UserQuestionnaireQuestions.save(client, productData.getInteger("questionnaire_questions_id"), productData.getInteger("passage_id"),  productData.getString("name"), productData.getString("password"));
    }

    @GET
    @RolesAllowed({"User", "Admin"})
    @Path("user_questionnaire_questions")
    public Multi<UserQuestionnaireQuestions> getUserQuestionnaireQuestions() {

        System.out.println(" getUserQuestionnaireQuestions " + jwt.getClaim("name").toString() + "  " +jwt.getClaim("name").toString());
        return UserQuestionnaireQuestions.findByNameAndPassword(client, jwt.getClaim("name").toString(), jwt.getClaim("password").toString());
    }



//********  User Questionnaire Questions **********
//*************************************************


//*************************************************
//******** User Test Question Choices ***********
    @POST
    @Path("user_test_question_choices")
    public Uni<Long> postUserTestQuestionChoices(JsonObject productData) {

        return UserTestQuestionChoices.save(client, productData.getInteger("test_question_id"), productData.getInteger("question_choice_id"), productData.getString("name"), productData.getString("password"));
    }
//********  User Test Question Choices **********
//*************************************************
}
