package com.caller.id.app.prototype.data.repository.contacts

import android.content.ContentResolver
import android.provider.ContactsContract
import com.caller.id.app.prototype.domain.models.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(private val contentResolver: ContentResolver) :
    ContactsRepository {

    override fun getContacts(): Flow<List<Contact>> = flow {
        emit(fetchContacts(null))
    }

    override fun searchContacts(query: String): Flow<List<Contact>> = flow {
        emit(fetchContacts(query))
    }

    private fun fetchContacts(query: String?): List<Contact> {
        val contacts = mutableListOf<Contact>()

        val selection = if (query.isNullOrEmpty()) null else {
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ? OR ${ContactsContract.CommonDataKinds.Phone.NUMBER} LIKE ?"
        }

        val selectionArgs = if (query.isNullOrEmpty()) null else {
            arrayOf("%$query%", "%$query%")
        }

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val id = it.getString(idIndex)
                val name = it.getString(nameIndex)
                val phone = it.getString(phoneIndex)

                contacts.add(Contact(id, name, phone))
            }
        }
        return contacts.sortedBy { it.name }.distinctBy { it.id }
    }
}