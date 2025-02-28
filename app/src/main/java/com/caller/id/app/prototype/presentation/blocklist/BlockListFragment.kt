package com.caller.id.app.prototype.presentation.blocklist

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.caller.id.app.prototype.R
import com.caller.id.app.prototype.databinding.FragmentBlockListBinding
import com.caller.id.app.prototype.presentation.adapters.ContactsAdapter
import com.caller.id.app.prototype.presentation.base.BaseFragment
import com.caller.id.app.prototype.utils.RecyclerViewItemDecoration
import com.caller.id.app.prototype.utils.hide
import com.caller.id.app.prototype.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BlockListFragment : BaseFragment<FragmentBlockListBinding>() {

    private val viewModel: BlockListViewModel by viewModels<BlockListViewModel>()

    private val contactsAdapter: ContactsAdapter = ContactsAdapter(false) {
        viewModel.removeBlockedContact(it)
    }

    override fun setupView() {

        setUpRecyclerView()

        lifecycleScope.launch {
            viewModel.blockedContacts.collect { contacts ->
                contacts?.let {
                    if (it.isEmpty()) {
                        binding.layoutNoResult.show()
                    }else{
                        binding.layoutNoResult.hide()
                    }
                    contactsAdapter.submitList(contacts.toMutableList())
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