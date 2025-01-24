package com.caller.id.app.prototype.domain.usecases

import com.caller.id.app.prototype.data.ContactsRepository
import com.caller.id.app.prototype.domain.models.Contact
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetContactsUseCase @Inject constructor(
    private val contactRepository: ContactsRepository
) {
    suspend fun execute(): Flow<List<Contact>> = contactRepository.getContacts()
}

@Singleton
class SearchContactsUseCase @Inject constructor(
    private val contactRepository: ContactsRepository
) {
    suspend fun execute(query: String): Flow<List<Contact>> = contactRepository.searchContacts(query)
}