package com.caller.id.app.prototype.data.local.persistance

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.caller.id.app.prototype.domain.models.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockedContactsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBlockedContact(contact: Contact)

    @Delete
    suspend fun removeBlockedContact(contact: Contact)

    @Query("SELECT * FROM contact")
    fun getBlockedContacts(): Flow<List<Contact>>
}