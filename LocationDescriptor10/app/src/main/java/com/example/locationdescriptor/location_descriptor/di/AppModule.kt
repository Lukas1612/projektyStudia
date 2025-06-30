package com.example.locationdescriptor.location_descriptor.di

import android.app.Application
import androidx.room.Room
import androidx.work.WorkManager
import com.example.locationdescriptor.location_descriptor.data.data_source.DescriptionDatabase
import com.example.locationdescriptor.location_descriptor.data.repository.DescriptionRepositoryImpl
import com.example.locationdescriptor.location_descriptor.data.repository.FolderDescriptorRepositoryImpl
import com.example.locationdescriptor.location_descriptor.data.repository.InternalStorageFacadeImpl
import com.example.locationdescriptor.location_descriptor.domain.repository.DescriptionRepository
import com.example.locationdescriptor.location_descriptor.domain.repository.FolderDescriptorRepository
import com.example.locationdescriptor.location_descriptor.domain.repository.InternalStorageFacade
import com.example.locationdescriptor.location_descriptor.domain.use_case.description.*
import com.example.locationdescriptor.location_descriptor.domain.use_case.folder.*
import com.example.locationdescriptor.location_descriptor.domain.use_case.photo.*
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
    fun provideDescriptionDatabase(app: Application): DescriptionDatabase{
        return Room.databaseBuilder(
            app,
            DescriptionDatabase::class.java,
            DescriptionDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideDescriptionRepository(db: DescriptionDatabase): DescriptionRepository {
        return DescriptionRepositoryImpl(db.descriptionDao)
    }

    @Provides
    @Singleton
    fun provideFolderDescriptorRepository(db: DescriptionDatabase): FolderDescriptorRepository {
        return FolderDescriptorRepositoryImpl(db.folderDescriptorDao)
    }

    @Provides
    @Singleton
    fun provideInternalStorageFacade(app: Application): InternalStorageFacade{
        return InternalStorageFacadeImpl(app)
    }

    @Provides
    @Singleton
    fun provideDescriptionUseCases(repository: DescriptionRepository): PhotoDescriptionUseCases {
        return PhotoDescriptionUseCases(
            addPhotoDescription = AddPhotoDescription(repository),
            deletePhotoDescription = DeletePhotoDescription(repository),
            getPhotoDescription = GetPhotoDescription(repository),
            getPhotoDescriptions = GetPhotoDescriptions(repository)
        )
    }

    @Provides
    @Singleton
    fun providePhotoUseCases(storage: InternalStorageFacade): PhotoUseCases {
        return PhotoUseCases(
            savePhoto = SavePhoto(storage),
            deletePhoto = DeletePhoto(storage),
            getPhoto = GetPhoto(storage),
            getPhotoPath = GetPhotoDirectoryPath(storage),
            doesFileExist = DoesFileExist(storage),
            savePdfToFile = SavePdfToFile(storage)
        )
    }

    @Provides
    @Singleton
    fun provideFolderDescriptorUseCases(repository: FolderDescriptorRepository): FolderDescriptorUseCases {
        return FolderDescriptorUseCases(
            addFolderDescriptor = AddFolderDescriptor(repository),
            deleteFolderDescriptor = DeleteFolderDescriptor(repository),
            getFolderDescriptors = GetFolderDescriptors(repository),
            checkIfFolderNameIsAlreadyUsed = CheckIfFolderNameIsAlreadyUsed(repository),
            deleteFolderDescriptorByName = DeleteFolderDescriptorByName(repository)
        )
    }

    @Provides
    @Singleton
    fun provideWorkManager(
        app: Application
    ) = WorkManager.getInstance(app)

}