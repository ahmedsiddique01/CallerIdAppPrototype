package com.caller.id.app.prototype.presentation.contacts

import android.util.Log
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.caller.id.app.prototype.R
import com.caller.id.app.prototype.databinding.FragmentContactsBinding
import com.caller.id.app.prototype.presentation.base.BaseFragment
import com.caller.id.app.prototype.presentation.adapters.ContactsAdapter
import com.caller.id.app.prototype.presentation.contacts.viewmodel.ContactsViewModel
import com.caller.id.app.prototype.utils.RecyclerViewItemDecoration
import com.caller.id.app.prototype.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactsFragment : BaseFragment<FragmentContactsBinding>() {

    private val viewModel by viewModels<ContactsViewModel>()

    private var contactsAdapter = ContactsAdapter { contact ->
        if (contact.isBlocked) {
            viewModel.removeBlockedContact(contact)
        } else {
            viewModel.addBlockedContact(contact)
        }
    }

    override fun setupView() {
        setUpRecyclerView()
        setUpSearchListener()
        createNotification()
        lifecycleScope.launch {
            viewModel.contacts.collect { contacts ->
                contacts?.let {
                    contactsAdapter.submitList(contacts.toMutableList())
                }
            }
        }
    }

    private fun resetScrollAndClearFocus() {
        binding.apply {
            recyclerViewContacts.scrollToPosition(0)
            searchTextInputLayout.editText?.apply {
                if (text.isNullOrEmpty()) {
                    clearFocus()
                    hideKeyboard()
                }
            }
        }
    }

    private fun setUpSearchListener() {
        binding.apply {
            searchTextInputLayout.editText?.doAfterTextChanged { editable ->
                viewModel.apply {
                    editable?.toString()?.trim()?.let { query ->
                        searchContacts(query)
                    }

                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.apply {
            recyclerViewContacts.apply {
                adapter = contactsAdapter
                addItemDecoration(RecyclerViewItemDecoration(context, R.drawable.ic_divider))
            }
        }
    }

    private fun createNotification() {
        viewModel.viewModelScope.launch {
            delay(3000)
            Log.d("Something", "createNotification: ${viewModel.getRandomUnblockedNumber().number}")
            createNotification()
        }
    }

}