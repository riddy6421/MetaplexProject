package com.example.metaplexproject.di

import android.app.Application
import androidx.room.Room
import com.example.metaplexproject.MetaplexApp
import com.example.metaplexproject.data.ApiDataRepo
import com.example.metaplexproject.data.db.DataDaoImpl
import com.example.metaplexproject.data.db.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object AppModule {

    @Provides
    fun ioCoroutineDispatch(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideApiDataRepo(dataDaoImpl: DataDaoImpl): ApiDataRepo {
        return ApiDataRepo(dataDaoImpl)
    }

    @Provides
    fun provideNftDatabase(metaplexApp: Application): Database {
        return Room.databaseBuilder(
            metaplexApp,
            Database::class.java,
            "nfts_db"
        ).build()
    }

    @Provides
    fun provideDatabaseImpl(database: Database): DataDaoImpl {
        return DataDaoImpl(database.dataDao)
    }
}