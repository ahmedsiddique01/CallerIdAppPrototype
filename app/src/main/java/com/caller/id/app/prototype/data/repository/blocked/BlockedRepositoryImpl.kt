package com.caller.id.app.prototype.data.repository.blocked

import com.caller.id.app.prototype.data.local.LocalSource
import com.caller.id.app.prototype.domain.models.Contact
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BlockedRepositoryImpl @Inject constructor(
    private val localSource: LocalSource
) : BlockedRepository {

    override suspend fun addBlockedContact(contact: Contact) {
        localSource.addBlockedContact(contact)
    }

    override suspend fun removeBlockedContact(contact: Contact) {
        localSource.removeBlockedContact(contact)
    }

    override fun getBlockedContacts(): Flow<List<Contact>> {
        return localSource.getBlockedContacts()
    }
}