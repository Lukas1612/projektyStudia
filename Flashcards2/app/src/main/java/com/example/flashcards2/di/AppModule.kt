package com.example.flashcards2.di

import android.app.Application
import androidx.room.Room
import com.example.flashcards2.data.data_source.FlashcardDatabase
import com.example.flashcards2.data.repository.ColRepositoryImpl
import com.example.flashcards2.data.repository.FlashcardGroupRepositoryImpl
import com.example.flashcards2.data.repository.FlashcardRepositoryImpl
import com.example.flashcards2.domain.repository.ColRepository
import com.example.flashcards2.domain.repository.FlashcardGroupRepository
import com.example.flashcards2.domain.repository.FlashcardRepository
import com.example.flashcards2.domain.use_case.col.ColUseCases
import com.example.flashcards2.domain.use_case.col.DeleteCol
import com.example.flashcards2.domain.use_case.col.GetCol
import com.example.flashcards2.domain.use_case.col.SaveCol
import com.example.flashcards2.domain.use_case.flashcard.AddFlashcard
import com.example.flashcards2.domain.use_case.flashcard.DeleteFlashcard
import com.example.flashcards2.domain.use_case.flashcard.DeleteFlashcardById
import com.example.flashcards2.domain.use_case.flashcard.DeleteFlashcardsByGroupId
import com.example.flashcards2.domain.use_case.flashcard.FlashcardUseCases
import com.example.flashcards2.domain.use_case.flashcard.GetDueLearnFlashcards
import com.example.flashcards2.domain.use_case.flashcard.GetDueReviewFlashcards
import com.example.flashcards2.domain.use_case.flashcard.GetFlashcardById
import com.example.flashcards2.domain.use_case.flashcard.GetFlashcards
import com.example.flashcards2.domain.use_case.flashcard.GetFlashcardsByGroupId
import com.example.flashcards2.domain.use_case.flashcard.GetFlashcardsWhereRepetitionTimeIsSmallerThan
import com.example.flashcards2.domain.use_case.flashcard.GetIntradayLearningFlashcards
import com.example.flashcards2.domain.use_case.flashcard.GetNewFlashcards
import com.example.flashcards2.domain.use_case.groups.AddFlashcardGroup
import com.example.flashcards2.domain.use_case.groups.DeleteFlashcardGroup
import com.example.flashcards2.domain.use_case.groups.DeleteFlashcardGroupById
import com.example.flashcards2.domain.use_case.groups.FlashcardGroupUseCases
import com.example.flashcards2.domain.use_case.groups.GetFlashcardGroupById
import com.example.flashcards2.domain.use_case.groups.GetFlashcardGroupByName
import com.example.flashcards2.domain.use_case.groups.GetFlashcardGroups
import com.example.flashcards2.presentation.feature_flashcards_list.navigation_mediator.Navigator
import com.example.flashcards2.presentation.feature_flashcards_list.navigation_mediator.NavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFlashcardDatabase(app: Application): FlashcardDatabase {
        return Room.databaseBuilder(
            app,
            FlashcardDatabase::class.java,
            FlashcardDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideFlashcardRepository(db: FlashcardDatabase): FlashcardRepository {
        return FlashcardRepositoryImpl(db.flashcardDao)
    }

    @Provides
    @Singleton
    fun provideFlashcardGroupRepository(db: FlashcardDatabase): FlashcardGroupRepository {
        return FlashcardGroupRepositoryImpl(db.flashcardGroupDao)
    }

    @Provides
    @Singleton
    fun provideColRepository(db: FlashcardDatabase): ColRepository {
        return ColRepositoryImpl(db.colDao)
    }

    @Provides
    @Singleton
    fun provideFlashcardUseCases(repository: FlashcardRepository): FlashcardUseCases {
        return FlashcardUseCases(
            addFlashcard = AddFlashcard(repository),
            deleteFlashcard = DeleteFlashcard(repository),
            getFlashcards = GetFlashcards(repository),
            getFlashcardById = GetFlashcardById(repository),
            getFlashcardsByGroupId = GetFlashcardsByGroupId(repository),
            deleteFlashcardsByGroupId = DeleteFlashcardsByGroupId(repository),
            deleteFlashcardById = DeleteFlashcardById(repository),
            getFlashcardsWhereRepetitionTimeIsSmallerThan = GetFlashcardsWhereRepetitionTimeIsSmallerThan(repository),
            getIntradayLearningFlashcards = GetIntradayLearningFlashcards(repository),
            getDueLearnFlashcards = GetDueLearnFlashcards(repository),
            getDueReviewFlashcards = GetDueReviewFlashcards(repository),
            getNewFlashcards = GetNewFlashcards(repository)
        )
    }

    @Provides
    @Singleton
    fun provideFlashcardGroupUseCases(repository: FlashcardGroupRepository): FlashcardGroupUseCases {
        return FlashcardGroupUseCases(
            addFlashcardGroup = AddFlashcardGroup(repository),
            deleteFlashcardGroup = DeleteFlashcardGroup(repository),
            getFlashcardGroupById = GetFlashcardGroupById(repository),
            getFlashcardGroupByName = GetFlashcardGroupByName(repository),
            getFlashcardGroups = GetFlashcardGroups(repository),
            deleteFlashcardGroupById = DeleteFlashcardGroupById(repository)
        )
    }

    @Provides
    @Singleton
    fun provideColUseCases(repository: ColRepository): ColUseCases {
        return ColUseCases(
            deleteCol = DeleteCol(repository),
            getCol = GetCol(repository),
            saveCol = SaveCol(repository)
        )
    }

    @Provides
    @Singleton
    fun provideNavigator(): Navigator{
        return NavigatorImpl()
    }
}