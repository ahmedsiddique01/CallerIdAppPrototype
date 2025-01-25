package com.caller.id.app.prototype.presentation.blocklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caller.id.app.prototype.domain.models.Contact
import com.caller.id.app.prototype.domain.usecases.blocked.BlockedContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockListViewModel @Inject constructor(private val blockedContactsUseCase: BlockedContactsUseCase) :
    ViewModel() {
    private val _blockedContacts = MutableStateFlow<List<Contact>?>(null)
    val blockedContacts: StateFlow<List<Contact>?> get() = _blockedContacts

    init {
        fetchBlockedContacts()
    }

    fun fetchBlockedContacts() {
        viewModelScope.launch {
            blockedContactsUseCase.getBlockedContacts()
                .catch { e ->
                    e.printStackTrace()
                }
                .collect { blockedContacts ->
                    blockedContacts.forEach{
                        it.isBlocked = true
                    }
                    _blockedContacts.value = blockedContacts
                }
        }
    }

    fun removeBlockedContact(contact: Contact) {
        viewModelScope.launch {
            blockedContactsUseCase.removeBlockedContact(contact)
            fetchBlockedContacts()
        }
    }
}