package com.caller.id.app.prototype.data.local

import com.caller.id.app.prototype.domain.models.Contact
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    fun getBlockedContacts(): Flow<List<Contact>>
    suspend fun addBlockedContact(contact: Contact)
    suspend fun removeBlockedContact(contact: Contact)
}
