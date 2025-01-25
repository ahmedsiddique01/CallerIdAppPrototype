package com.caller.id.app.prototype.data.local.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import com.caller.id.app.prototype.domain.models.Contact

@Database(version = 1, entities = [Contact::class], exportSchema = false)
abstract class ContactsDatabase : RoomDatabase() {
    abstract fun contactsDao(): BlockedContactsDao
}
