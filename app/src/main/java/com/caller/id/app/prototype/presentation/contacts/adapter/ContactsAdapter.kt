package com.caller.id.app.prototype.presentation.contacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adwardstark.mtextdrawable.MaterialTextDrawable
import com.caller.id.app.prototype.databinding.ItemContactBinding
import com.caller.id.app.prototype.domain.models.Contact

class ContactsAdapter(private val onBlockUnblockClickListener: (Contact) -> Unit) :
    ListAdapter<Contact, ContactsAdapter.ContactViewHolder>(ContactDiffCallback()) {

    class ContactViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)

        holder.binding.apply {

            textViewName.text = contact.name
            textViewNumber.text = contact.number

            MaterialTextDrawable.with(imageViewPhoto.context)
                .text(contact.name)
                .shape(MaterialTextDrawable.MaterialShape.CIRCLE)
                .into(imageViewPhoto)

            buttonAction.setOnClickListener {
                onBlockUnblockClickListener(contact)
            }

        }

    }

    class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.name == newItem.name // Assuming name is unique
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }
}