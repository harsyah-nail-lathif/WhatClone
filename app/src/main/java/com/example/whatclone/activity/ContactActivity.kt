package com.example.whatclone.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatclone.R
import com.example.whatclone.adapter.ContactAdapter
import com.example.whatclone.listener.ContactClickListener
import com.example.whatclone.util.Contact
import kotlinx.android.synthetic.main.activity_contact.*
import java.util.ArrayList

class ContactActivity : AppCompatActivity(), ContactClickListener {

    private val contactsList = ArrayList<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        setupList()
        getContacts()
    }

    private fun getContacts() {
        progress_layout_contact.visibility = View.VISIBLE
        contactsList.clear()
        val newList = ArrayList<Contact>()
        val phone = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, null, null,null
        )
        while (phone!!.moveToNext()){
            val name = phone.getString(
                phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            )
            val phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            newList.add(Contact(name, phoneNumber))
        }
        progress_layout_contact.visibility = View.GONE

        contactsList.addAll(newList)
        phone.close()
    }

    private fun setupList() {
        progress_layout_contact.visibility = View.GONE
        val contactsAdapter = ContactAdapter(contactsList)
        contactsAdapter.setOnItemClickListener(this)
        rv_contact.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = contactsAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onContactCliceked(name: String?, phone: String?) {
        val intent = Intent()
        intent.putExtra(MainActivity.PARAM_NAME, name)
        intent.putExtra(MainActivity.PARAM_PHONE, phone)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}