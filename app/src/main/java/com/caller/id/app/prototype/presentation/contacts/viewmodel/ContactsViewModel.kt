package com.caller.id.app.prototype.presentation.contacts.viewmodel

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caller.id.app.prototype.domain.models.Contact
import com.caller.id.app.prototype.domain.usecases.blocked.BlockedContactsUseCase
import com.caller.id.app.prototype.domain.usecases.contacts.ContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsUseCase: ContactsUseCase,
    private val blockedContactsUseCase: BlockedContactsUseCase
) : ViewModel() {
    private val _contacts = MutableStateFlow<List<Contact>?>(null)
    val contacts: StateFlow<List<Contact>?> get() = _contacts
    var query: String = ""

    private val _blockedContacts = MutableStateFlow<List<Contact>?>(null)

    init {
        fetchData()
    }

    fun fetchContacts() {
        viewModelScope.launch {
            contactsUseCase.searchContacts(query)
                .catch { e ->
                    e.printStackTrace()
                }
                .collect { contactList ->
                    val blockedIds = _blockedContacts.value?.map { it.id } ?: emptyList()

                    val updatedContacts = contactList.map { contact ->
                        contact.copy(isBlocked = blockedIds.contains(contact.id))
                    }
                    _contacts.value = updatedContacts
                }
        }
    }

    private fun fetchBlockedContacts() {
        viewModelScope.launch {
            blockedContactsUseCase.getBlockedContacts()
                .catch { e ->
                    e.printStackTrace()
                }
                .collect { blockedContacts ->
                    _blockedContacts.value = blockedContacts
                  //  fetchContacts()
                }
        }
    }

    fun searchContacts(query: String) {
        viewModelScope.launch {
            this@ContactsViewModel.query = query
            contactsUseCase.searchContacts(query)
                .catch { e ->
                    // Handle errors
                    e.printStackTrace()
                }
                .collect { contactList ->
                    val blockedIds = _blockedContacts.value?.map { it.id } ?: emptyList()

                    val updatedContacts = contactList.map { contact ->
                        contact.copy(isBlocked = blockedIds.contains(contact.id))
                    }

                    _contacts.value = updatedContacts
                }
        }
    }

    fun addBlockedContact(contact: Contact) {
        viewModelScope.launch {
            try {
                blockedContactsUseCase.addBlockedContact(contact)
                fetchBlockedContacts()
                updateContactsListAfterBlocking(contact, true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeBlockedContact(contact: Contact) {
        viewModelScope.launch {
            try {
                blockedContactsUseCase.removeBlockedContact(contact)
                fetchBlockedContacts()
                updateContactsListAfterBlocking(contact, false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateContactsListAfterBlocking(contact: Contact, isBlocked: Boolean) {
        _contacts.value = _contacts.value?.map { currentContact ->
            if (currentContact.id == contact.id) {
                currentContact.copy(isBlocked = isBlocked)
            } else {
                currentContact
            }
        }
    }

    fun getRandomUnblockedNumber() =
        _contacts.value?.filter { !it.isBlocked }?.let {
            if (it.isNotEmpty()) {
                it.random()
            } else {
                defaultContact()
            }
        } ?: defaultContact()

    fun fetchData() {
        viewModelScope.launch {
            fetchBlockedContacts()
            delay(300)
            fetchContacts()
        }
    }
}

fun defaultContact() = Contact(
    "11",
    "Johnson",
    "+92 333 3333333"
)
