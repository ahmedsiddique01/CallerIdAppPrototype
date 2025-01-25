package com.caller.id.app.prototype.data.local

import com.caller.id.app.prototype.data.local.persistance.BlockedContactsDao
import com.caller.id.app.prototype.domain.models.Contact
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val contactsDao: BlockedContactsDao
) : LocalSource {

    override suspend fun addBlockedContact(contact: Contact) {
        contactsDao.addBlockedContact(contact)
    }

    override suspend fun removeBlockedContact(contact: Contact) {
        contactsDao.removeBlockedContact(contact)
    }

    override fun getBlockedContacts(): Flow<List<Contact>> {
        return contactsDao.getBlockedContacts()
    }
}