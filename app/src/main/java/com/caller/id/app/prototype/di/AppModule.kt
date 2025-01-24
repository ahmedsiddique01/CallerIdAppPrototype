package com.caller.id.app.prototype.di

import android.content.ContentResolver
import android.content.Context
import com.caller.id.app.prototype.data.ContactsRepository
import com.caller.id.app.prototype.data.ContactsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @Singleton
    fun provideContactRepository(contentResolver: ContentResolver): ContactsRepository {
        return ContactsRepositoryImpl(contentResolver)
    }
}