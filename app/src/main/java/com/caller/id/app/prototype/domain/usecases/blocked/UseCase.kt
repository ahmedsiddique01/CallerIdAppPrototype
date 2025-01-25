package com.caller.id.app.prototype.domain.usecases.blocked

import com.caller.id.app.prototype.domain.models.Contact
import kotlinx.coroutines.flow.Flow

interface UseCase {
    suspend fun addBlockedContact(contact: Contact)
    suspend fun removeBlockedContact(contact: Contact)
    fun getBlockedContacts(): Flow<List<Contact>>
}