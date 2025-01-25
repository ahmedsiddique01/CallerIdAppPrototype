package com.caller.id.app.prototype.di

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import com.caller.id.app.prototype.data.local.LocalDataSource
import com.caller.id.app.prototype.data.local.LocalSource
import com.caller.id.app.prototype.data.local.persistance.BlockedContactsDao
import com.caller.id.app.prototype.data.local.persistance.ContactsDatabase
import com.caller.id.app.prototype.data.repository.blocked.BlockedRepository
import com.caller.id.app.prototype.data.repository.blocked.BlockedRepositoryImpl
import com.caller.id.app.prototype.data.repository.contacts.ContactsRepository
import com.caller.id.app.prototype.data.repository.contacts.ContactsRepositoryImpl
import com.caller.id.app.prototype.domain.usecases.contacts.ContactsUseCase
import com.caller.id.app.prototype.utils.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        ContactsDatabase::class.java,
        DB_NAME
    ).fallbackToDestructiveMigration()
        .allowMainThreadQueries().build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun provideDao(db: ContactsDatabase) =
        db.contactsDao()

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

    @Provides
    @Singleton
    fun provideLocalSource(contactsDao: BlockedContactsDao): LocalSource {
        return LocalDataSource(contactsDao)
    }

    @Provides
    @Singleton
    fun provideBlockedContactsRepository(localSource: LocalSource): BlockedRepository {
        return BlockedRepositoryImpl(localSource)
    }

}