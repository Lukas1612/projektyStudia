package com.example.readyreadingkotlin.questions.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.readyreadingkotlin.questions.qdb.QuestionChoicesDao;
import com.example.readyreadingkotlin.questions.qdb.QuestionDao;
import com.example.readyreadingkotlin.questions.qdb.QuestionEntity;
import com.example.readyreadingkotlin.questions.qdb.QuestionWithChoicesEntity;
import com.example.readyreadingkotlin.questions.qdb.TestQuestionChoicesDao;
import com.example.readyreadingkotlin.questions.qdb.TestQuestionDao;
import com.example.readyreadingkotlin.questions.qdb.TestQuestionEntity;
import com.example.readyreadingkotlin.questions.qdb.TestQuestionWithChoicesEntity;


@Database(entities = {QuestionWithChoicesEntity.class, QuestionEntity.class, TestQuestionWithChoicesEntity.class, TestQuestionEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase
{
    private static final String DB_NAME = "question_db";

    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getAppDatabase(Context context)
    {
        if (INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)

                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public abstract QuestionChoicesDao getQuestionChoicesDao();
    public abstract QuestionDao getQuestionDao();
    public abstract TestQuestionChoicesDao getTestQuestionChoicesDao();
    public abstract TestQuestionDao getTestQuestionDao();
}
