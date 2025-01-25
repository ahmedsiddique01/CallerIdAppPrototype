package com.caller.id.app.prototype.domain.usecases.blocked

import com.caller.id.app.prototype.data.repository.blocked.BlockedRepository
import com.caller.id.app.prototype.domain.models.Contact
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class BlockedContactsUseCase @Inject constructor(private val blockedRepository: BlockedRepository) :
    UseCase {
    override suspend fun addBlockedContact(contact: Contact) {
        blockedRepository.addBlockedContact(contact)
    }

    override suspend fun removeBlockedContact(contact: Contact) {
        blockedRepository.removeBlockedContact(contact)
    }

    override fun getBlockedContacts(): Flow<List<Contact>> {
        return blockedRepository.getBlockedContacts()
    }
}