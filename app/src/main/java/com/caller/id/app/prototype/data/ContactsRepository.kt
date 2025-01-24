package com.caller.id.app.prototype.data

import com.caller.id.app.prototype.domain.models.Contact
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {
    fun getContacts(): Flow<List<Contact>>
    fun searchContacts(query: String): Flow<List<Contact>>
}