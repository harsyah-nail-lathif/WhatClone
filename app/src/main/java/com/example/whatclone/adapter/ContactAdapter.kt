package com.example.whatclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whatclone.R
import com.example.whatclone.listener.ContactClickListener
import com.example.whatclone.util.Contact
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_contact.*

class ContactAdapter(val contact: ArrayList<Contact>):
    RecyclerView.Adapter<ContactAdapter.ContactsViewHolder>() {
    private var clickListener: ContactClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ContactsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false))

    override fun getItemCount() = contact.size

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        holder.bindItem(contact[position], clickListener)
    }

    class ContactsViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bindItem(contact: Contact, listener: ContactClickListener?) {
            txt_contact_name.text = contact.name
            txt_contact_number.text = contact.phone
            itemView.setOnClickListener {
                listener?.onContactCliceked(contact.name, contact.phone)
            }
        }
    }

    fun setOnItemClickListener(listener: ContactClickListener) {
        clickListener = listener
        notifyDataSetChanged()
    }
}