package com.caller.id.app.prototype.presentation.contacts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caller.id.app.prototype.domain.models.Contact
import com.caller.id.app.prototype.domain.usecases.contacts.ContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(private val contactsUseCase: ContactsUseCase) : ViewModel() {
    private val _contacts = MutableStateFlow<List<Contact>?>(null)
    val contacts: StateFlow<List<Contact>?> get() = _contacts

    init {
        fetchContacts()
    }

    fun fetchContacts() {
        viewModelScope.launch {
            contactsUseCase.getContacts()
                .catch { e ->
                    e.printStackTrace()
                }
                .collect { contactList ->
                    _contacts.value = contactList
                }
        }
    }

    fun searchContacts(query: String) {
        viewModelScope.launch {
            contactsUseCase.searchContacts(query)
                .catch { e ->
                    // Handle errors
                    e.printStackTrace()
                }
                .collect { contactList ->
                    _contacts.value = contactList
                }
        }
    }
}