package com.caller.id.app.prototype.domain.usecases.contacts

import com.caller.id.app.prototype.domain.models.Contact
import kotlinx.coroutines.flow.Flow

interface UseCase {
    fun getContacts(): Flow<List<Contact>>
    fun searchContacts(query: String): Flow<List<Contact>>
}