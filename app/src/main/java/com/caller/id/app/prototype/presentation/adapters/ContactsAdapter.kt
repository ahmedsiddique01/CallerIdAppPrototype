package com.caller.id.app.prototype.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.adwardstark.mtextdrawable.MaterialTextDrawable
import com.caller.id.app.prototype.R
import com.caller.id.app.prototype.databinding.ItemContactBinding
import com.caller.id.app.prototype.domain.models.Contact
import com.caller.id.app.prototype.utils.load

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

            imageViewPhoto.load(
                MaterialTextDrawable.with(imageViewPhoto.context)
                    .text(contact.name)
                    .shape(MaterialTextDrawable.MaterialShape.CIRCLE)
                    .get()
            )

            if (contact.isBlocked) {
                buttonAction.text = buttonAction.context.getString(R.string.unblock)
                buttonAction.backgroundTintList =
                    ContextCompat.getColorStateList(
                        buttonAction.context,
                        R.color.unblock_button_color_green
                    )
            } else {
                buttonAction.text = buttonAction.context.getString(R.string.block)
                buttonAction.backgroundTintList =
                    ContextCompat.getColorStateList(
                        buttonAction.context,
                        R.color.block_button_color_red
                    )
            }

            buttonAction.setOnClickListener {
                onBlockUnblockClickListener.invoke(contact)
            }

        }

    }

    class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id.equals(newItem.id,false)
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.isBlocked == newItem.isBlocked
        }
    }
}