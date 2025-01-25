package com.caller.id.app.prototype.domain.usecases.contacts

import com.caller.id.app.prototype.data.repository.contacts.ContactsRepository
import com.caller.id.app.prototype.domain.models.Contact
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactsUseCase @Inject constructor(
    private val contactRepository: ContactsRepository
):UseCase {
    override fun getContacts(): Flow<List<Contact>>  = contactRepository.getContacts()

    override fun searchContacts(query: String): Flow<List<Contact>>  = contactRepository.searchContacts(query)
}