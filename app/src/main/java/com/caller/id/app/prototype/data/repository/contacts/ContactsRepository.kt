package com.caller.id.app.prototype.data.repository.contacts

import com.caller.id.app.prototype.domain.models.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun getContacts(): Flow<List<Contact>>
    fun searchContacts(query: String): Flow<List<Contact>>
}