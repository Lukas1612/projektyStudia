package com.example.readyreadingkotlin.database

import com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard.FlashcardBodyPost
import com.example.readyreadingkotlin.FullPassageMenu.ui.flashcard.FlashcardService
import com.example.readyreadingkotlin.described_passage.*
import com.example.readyreadingkotlin.learning_unit.*
import com.example.readyreadingkotlin.questions.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class DbHelper() {


    fun loadDescribedPassagesList(vlanguage: String, passageLoaderListener: PassageLoaderListener)
    {
        var  mutableDescribedPassagesList = mutableListOf<DescribedPassage>()

        val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val passageService: PassageService = retrofit.create(PassageService::class.java)

        val call: Call<List<DescribedPassage>> = passageService.getPassages()

        call.enqueue(object : Callback<List<DescribedPassage>?> {
            override fun onResponse(
                    call: Call<List<DescribedPassage>?>,
                    response: Response<List<DescribedPassage>?>,
            ) {
                if (!response.isSuccessful()) {
                    println(" response ERROR " + response.toString())
                    return
                }


                var describedPassagesList: List<DescribedPassage>? = response.body()

                for (passage in describedPassagesList!!) {
                    mutableDescribedPassagesList.add(passage)

                }

                passageLoaderListener.update(mutableDescribedPassagesList)


            }

            override fun onFailure(call: Call<List<DescribedPassage>?>, t: Throwable) {

                println(" ERROR " + t)
            }

        })
    }

    fun addNewFlashcard(
            word: String,
            translation: String,
            example_sentence: String,
            translated_sentence: String,
            name: String,
            password: String,
            id: Int,
    )
    {

        val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val flashcardService: FlashcardService = retrofit.create(FlashcardService::class.java)


        val call: Call<Long> = flashcardService.postFlashcard(
                FlashcardBodyPost(
                        word,
                        translation,
                        example_sentence,
                        translated_sentence,
                        name,
                        password,
                        id
                )
        )

        call.enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (!response.isSuccessful()) {
                    println(" response " + response.toString())
                    return
                }

            }

            override fun onFailure(call: Call<Long>, t: Throwable) {

                println(" ERROR " + t)
            }

        })
    }

    fun addUserTest(
            test_passage_id: Int,
            score: String,
            date: String,
            name: String,
            password: String,
    )
    {

        val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val userTestsService: UserTestsService = retrofit.create(UserTestsService::class.java)

        val call: Call<Long> = userTestsService.postUserTest(
                UserTestsBodyPost(
                        name,
                        password,
                        test_passage_id,
                        score,
                        date
                )
        )

        call.enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (!response.isSuccessful()) {
                    println(" response " + response.toString())
                    return
                }

            }

            override fun onFailure(call: Call<Long>, t: Throwable) {

                println(" ERROR " + t)
            }

        })
    }



    fun loadLearningUnitsList(
            vlanguage: String,
            learningUnitLoaderListener: LearningUnitLoaderListener,
    )
    {
        var  mutableLearningUnitsList = mutableListOf<LearningUnit>()

        val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val learningUnitService: LearningUnitService = retrofit.create(LearningUnitService::class.java)

        val call: Call<List<LearningUnit>> = learningUnitService.getLearningUnits()

        call.enqueue(object : Callback<List<LearningUnit>?> {
            override fun onResponse(
                    call: Call<List<LearningUnit>?>,
                    response: Response<List<LearningUnit>?>,
            ) {
                if (!response.isSuccessful()) {
                    println(" response ERROR " + response.toString())
                    return
                }


                var learningUnitList: List<LearningUnit>? = response.body()

                println("")
                println(" UNITS                ")
                learningUnitList!!.forEach() {
                    println(it.toString())
                }
                println(" UNITS                ")
                println("")

                for (passage in learningUnitList!!) {
                    mutableLearningUnitsList.add(passage)

                }

                learningUnitLoaderListener.update(mutableLearningUnitsList)


            }

            override fun onFailure(call: Call<List<LearningUnit>?>, t: Throwable) {

                println(" ERROR " + t)
            }

        })
    }

    fun deleteUserTest(
            test_passage_id: Int,
            score: String,
            date: String,
            name: String,
            password: String,
    )
    {

        val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val userTestsService: UserTestsService = retrofit.create(UserTestsService::class.java)

        val call: Call<Boolean> = userTestsService.delete(
                UserTestsBodyPost(
                        name,
                        password,
                        test_passage_id,
                        score,
                        date
                )
        )

        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (!response.isSuccessful()) {
                    println(" response " + response.toString())
                    return
                }

            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {

                println(" ERROR " + t)
            }

        })
    }



    fun loadQuestionnaireQuestions(questionnaireQuestionsLoaderListener: QuestionnaireQuestionsLoaderListener)
    {
        var  mutableQuestionnaireQuestionsList = mutableListOf<QuestionnaireQuestions>()

        val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val questionnaireQuestionsService: QuestionnaireQuestionsService = retrofit.create(
                QuestionnaireQuestionsService::class.java
        )

        val call: Call<List<QuestionnaireQuestions>> = questionnaireQuestionsService.getQuestionnaireQuestions()

        call.enqueue(object : Callback<List<QuestionnaireQuestions>?> {
            override fun onResponse(
                    call: Call<List<QuestionnaireQuestions>?>,
                    response: Response<List<QuestionnaireQuestions>?>,
            ) {
                if (!response.isSuccessful()) {
                    println(" response ERROR " + response.toString())
                    return
                }


                var questionnaireQuestionsList: List<QuestionnaireQuestions>? = response.body()



                for (question in questionnaireQuestionsList!!) {
                    mutableQuestionnaireQuestionsList.add(question)

                }

                questionnaireQuestionsLoaderListener.update(mutableQuestionnaireQuestionsList)


            }

            override fun onFailure(call: Call<List<QuestionnaireQuestions>?>, t: Throwable) {

                println(" ERROR " + t)
            }

        })
    }


    fun addNewPassagesAnswersTier(
            tier: Int,
            passage_id: Int,
            question_id: Int,
            name: String,
            password: String,
            responseHandler: IServerResponseHandler,
    )
    {

        println(" there ")

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val passagesAnswersTierService: PassagesAnswersTierService = retrofit.create(
                PassagesAnswersTierService::class.java
        )


        val call: Call<Long> = passagesAnswersTierService.savePassagesAnswersTier(
                PassagesAnswersTierBodyPost(
                        tier,
                        passage_id,
                        question_id,
                        name,
                        password
                )
        )

        call.enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (!response.isSuccessful()) {

                    println(" is no Successful ")

                    println(" response " + response.toString())
                    println("addNewPassagesAnswersTier " + " onResponse")
                   // mediator.notify("addNewPassagesAnswersTier","onResponse")
                    responseHandler.onResponse()
                    
                    return
                }

                println(" response " + response.toString())
                println("addNewPassagesAnswersTier " + " onResponse")
               // mediator.notify("addNewPassagesAnswersTier","onResponse")
                responseHandler.onResponse()
            }

            override fun onFailure(call: Call<Long>, t: Throwable) {

                println(" ERROR " + t)
            }

        })
    }




    fun addNewUserQuestionnaireQuestions(
            questionnaire_questions_id: Int,
            passage_id: Int,
            name: String,
            password: String,
            responseHandler: IServerResponseHandler
    )
    {
        println(" here ")

         val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val userQuestionnaireQuestionsService: UserQuestionnaireQuestionsService = retrofit.create(
            UserQuestionnaireQuestionsService::class.java
        )




        val call: Call<Long> = userQuestionnaireQuestionsService.postUserQuestionnaireQuestionsService(
            UserQuestionnaireQuestionsBodyPost(
                questionnaire_questions_id,
                passage_id,
                name,
                password
            )
        )

        call.enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (!response.isSuccessful()) {

                    responseHandler.onResponse()

                    return
                }

                responseHandler.onResponse()

            }

            override fun onFailure(call: Call<Long>, t: Throwable) {

                println(" ERROR " + t)
            }

        })
    }




    fun postUserTestQuestionChoices(
            test_question_id: Int,
            question_choice_id: Int,
            name: String,
            password: String,
            responseHandler: IServerResponseHandler
    )
    {

        val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val userTestQuestionChoicesService: UserTestQuestionChoicesService = retrofit.create(UserTestQuestionChoicesService::class.java)

        val call: Call<Long> = userTestQuestionChoicesService.postUserTestQuestionChoices(
                UserTestQuestionChoicesBodyPost(
                        test_question_id,
                        question_choice_id,
                        name,
                        password
                )
        )

        call.enqueue(object : Callback<Long> {
            override fun onResponse(call: Call<Long>, response: Response<Long>) {
                if (!response.isSuccessful()) {
                    println(" response " + response.toString())

                    responseHandler.onResponse()
                    return
                }

                responseHandler.onResponse()

            }

            override fun onFailure(call: Call<Long>, t: Throwable) {

                println(" ERROR " + t)
            }

        })
    }




}