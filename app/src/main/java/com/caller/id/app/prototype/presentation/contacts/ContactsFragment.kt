package com.caller.id.app.prototype.presentation.contacts

import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.caller.id.app.prototype.R
import com.caller.id.app.prototype.databinding.FragmentContactsBinding
import com.caller.id.app.prototype.presentation.base.BaseFragment
import com.caller.id.app.prototype.presentation.contacts.adapter.ContactsAdapter
import com.caller.id.app.prototype.presentation.contacts.viewmodel.ContactsViewModel
import com.caller.id.app.prototype.utils.RecyclerViewItemDecoration
import com.caller.id.app.prototype.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactsFragment : BaseFragment<FragmentContactsBinding>() {

    private val viewModel by viewModels<ContactsViewModel>()

    private var contactsAdapter = ContactsAdapter {

    }

    override fun setupView() {
        lifecycleScope.launch {
            viewModel.contacts.collect { contacts ->
                contacts?.let {
                    contactsAdapter.submitList(contacts) {
                        resetScrollAndClearFocus()
                    }

                }
            }
        }
        setUpRecyclerView()
        setUpSearchListener()
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

}