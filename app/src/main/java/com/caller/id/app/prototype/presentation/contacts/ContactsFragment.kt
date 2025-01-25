package com.caller.id.app.prototype.presentation.contacts

import android.annotation.SuppressLint
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.caller.id.app.prototype.R
import com.caller.id.app.prototype.databinding.FragmentContactsBinding
import com.caller.id.app.prototype.presentation.adapters.ContactsAdapter
import com.caller.id.app.prototype.presentation.base.BaseFragment
import com.caller.id.app.prototype.presentation.contacts.viewmodel.ContactsViewModel
import com.caller.id.app.prototype.utils.RecyclerViewItemDecoration
import com.caller.id.app.prototype.utils.hide
import com.caller.id.app.prototype.utils.hideKeyboard
import com.caller.id.app.prototype.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ContactsFragment : BaseFragment<FragmentContactsBinding>() {

    private val viewModel by viewModels<ContactsViewModel>()

    private var contactsAdapter = ContactsAdapter { contact ->
        if (contact.isBlocked) {
            viewModel.addBlockedContact(contact)
        } else {
            viewModel.removeBlockedContact(contact)
        }
    }

    override fun setupView() {
        setData()
        setUpRecyclerView()
        setUpSearchListener()
        setUpButtonsListener()
        lifecycleScope.launch {
            viewModel.contacts.collect { contacts ->
                contacts?.let {
                    if (contacts.isEmpty()) {
                        binding.layoutNoResult.show()
                    } else {
                        binding.layoutNoResult.hide()
                    }
                    contactsAdapter.submitList(contacts.toMutableList()) {
                        resetScrollAndClearFocus()
                    }
                }
            }
        }
    }

    private fun setData() {
        binding.editText.setText(viewModel.query)
    }

    private fun setUpButtonsListener() {
        binding.apply {
            buttonReceiveCall.setOnClickListener {
                findNavController().navigate(
                    ContactsFragmentDirections.actionContactsFragmentToIncomingFragment(
                        viewModel.getRandomUnblockedNumber()
                    )
                )
            }
            scrollUp.setOnClickListener { recyclerViewContacts.smoothScrollToPosition(0) }
        }
    }

    private fun resetScrollAndClearFocus() {
        binding.apply {
            if (editText.text.isNullOrEmpty()) {
                clearFocusAndHideKeyboard()
            }
            recyclerViewContacts.apply {
                invalidateItemDecorations()
                adapter?.let { adapter ->
                    if (adapter.itemCount > 0) {
                        scrollToPosition(0)
                    }
                }
            }
        }
    }

    private fun clearFocusAndHideKeyboard() {
        binding.editText.apply {
            clearFocus()
            hideKeyboard()
        }
    }

    private fun setUpSearchListener() {
        binding.apply {
            editText.doAfterTextChanged { editable ->
                viewModel.apply {
                    editable?.toString()?.trim()?.let { query ->
                        searchContacts(query)
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setUpRecyclerView() {
        binding.apply {
            recyclerViewContacts.apply {
                adapter = contactsAdapter
                addItemDecoration(RecyclerViewItemDecoration(context, R.drawable.ic_divider))
                setOnTouchListener { _, _ ->
                    clearFocusAndHideKeyboard()
                    false
                }
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        if (dy > 0) {
                            scrollUp.show()
                            buttonReceiveCall.hide()
                        }

                        if (!canScrollVertically(-1)) {
                            scrollUp.hide()
                            buttonReceiveCall.show()
                        }

                    }
                })
            }
        }
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()
    }
}