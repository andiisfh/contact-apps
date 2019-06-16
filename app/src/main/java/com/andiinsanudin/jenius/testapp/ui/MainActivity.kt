package com.andiinsanudin.jenius.testapp.ui

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.andiinsanudin.jenius.testapp.R
import com.andiinsanudin.jenius.testapp.model.ContactModel
import com.andiinsanudin.jenius.testapp.model.ContactRequestModel
import com.andiinsanudin.jenius.testapp.utils.Utils
import com.andiinsanudin.jenius.testapp.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var contactViewModel: ContactViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)

        initUI()
        getAllContact()
    }

    /**
     *
     * Function for initialize UI
     *
     * **/
    private fun initUI(){
        srl_refresh.post {
            srl_refresh.isRefreshing = true
            contactViewModel.fetchAllContact()
        }

        srl_refresh.setOnRefreshListener {
            tv_message.visibility = View.GONE
            rv_list.visibility = View.VISIBLE

            contactViewModel.fetchAllContact()
        }

        rv_list.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
        }

        btn_search.setOnClickListener {
            tv_message.visibility = View.GONE
            rv_list.visibility = View.VISIBLE

            if (et_search.text!!.isEmpty()) {
                srl_refresh.isRefreshing = true
                contactViewModel.fetchAllContact()
            } else {
                searchContactByID()
            }
        }

        et_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchContactByID()
                    return true
                }
                return false
            }
        })
    }

    /**
     *
     * Function for get all contact
     *
     * **/
    private fun getAllContact(){
        contactViewModel.contactLiveData.observe(this, Observer {
            rv_list.apply {
                val list: MutableList<ContactModel> = mutableListOf()
                list.addAll(it.data!!)

                val contactModel = ContactModel("", "", "", 0, "")
                list.add(contactModel)

                adapter = ContactAdapter(this@MainActivity, list, {
                    showDialogAddOrUpdateContact(false, it)
                }, {
                    showDialogAddOrUpdateContact(true, it)
                }, {
                    Log.d("copy", "copy")
                    Utils().showSnackBar(this@MainActivity, container, Utils().copyToClipBoard(this@MainActivity, it), android.R.color.holo_green_light, android.R.color.black)
                }, {
                    showDialogDeleteContact(it.id, it.firstName.plus(" ").plus(it.lastName))
                })
                srl_refresh.isRefreshing = false
            }
        })
    }

    /**
     *
     * Function for search contact by ID
     *
     * **/
    private fun searchContactByID() {
        srl_refresh.isRefreshing = true

        contactViewModel.fetchContactById(et_search.text.toString())

        contactViewModel.contactByIdLiveData.observe(this, Observer { it ->
            val list: MutableList<ContactModel> = mutableListOf()

            rv_list.apply {
                if (it.data != null) {
                    list.add(it.data)

                    val contactModel = ContactModel("", "", "", 0, "")
                    list.add(contactModel)
                }else{
                    tv_message.visibility = View.VISIBLE
                    tv_message.text = it!!.message
                    list.add(ContactModel("", "", "", 0, ""))
                }

                adapter = ContactAdapter(this@MainActivity, list, {
                    showDialogAddOrUpdateContact(false, it)
                }, {
                    showDialogAddOrUpdateContact(true, it)
                }, {
                    Utils().showSnackBar(this@MainActivity, container, Utils().copyToClipBoard(this@MainActivity, it), android.R.color.holo_green_light, android.R.color.black)
                }, {
                    showDialogDeleteContact(it.id, it.firstName.plus(" ").plus(it.lastName))
                })
            }

            srl_refresh.isRefreshing = false
        })
    }

    /**
     *
     * Function for show dialog add new or update contact form
     *
     * **/
    private fun showDialogAddOrUpdateContact(isAddNew: Boolean, contactModel: ContactModel) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(if (isAddNew) "Add New Contact" else "Edit")

        val dialogView = layoutInflater.inflate(R.layout.dialog_add_or_update_contact, null)

        val mETFirstName = dialogView.findViewById<EditText>(R.id.et_first_name)
        val mETLastName = dialogView.findViewById<EditText>(R.id.et_last_name)
        val mETAge = dialogView.findViewById<EditText>(R.id.et_age)

        mETFirstName.setText(if (isAddNew) "" else contactModel.firstName)
        mETLastName.setText(if (isAddNew) "" else contactModel.lastName)
        mETAge.setText(if (isAddNew) "" else contactModel.age.toString())

        builder.setView(dialogView)

        builder.setPositiveButton(if (isAddNew) "Add" else "Edit") { dialog, _ ->

        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            addOrUpdateContact(
                contactModel.id,
                mETFirstName!!.text.toString(),
                mETLastName!!.text.toString(),
                if(mETAge!!.text.toString().isEmpty()) "0" else mETAge!!.text.toString(),
                if (isAddNew) "N/A" else contactModel.photo,
                dialog,
                dialogView.findViewById(R.id.container),
                isAddNew
            )
        })
    }

    /**
     *
     * Function for add new or update contact
     *
     * **/
    private fun addOrUpdateContact(id: String, firstName: String, lastName: String, age: String, photo: String, dialog: AlertDialog, dialogView: View, isAddNew: Boolean) {
        val contactRequestModel = ContactRequestModel(
            firstName,
            lastName,
            age.toInt(),
            photo
        )

        if(isAddNew){
            contactViewModel.postAddContact(contactRequestModel)

            contactViewModel.addContactLiveData.observe(this, Observer {
                if(it.statusCode != null){
                    Utils().showSnackBar(this@MainActivity, dialogView, it.message, android.R.color.holo_red_light, android.R.color.white)
                }else{
                    srl_refresh.isRefreshing = true
                    contactViewModel.fetchAllContact()
                    dialog.dismiss()
                    Utils().showSnackBar(this@MainActivity, container, it.message, android.R.color.holo_green_light, android.R.color.black)
                }
            })
        }else{
            contactViewModel.postUpdateContact(id, contactRequestModel)

            contactViewModel.updateContactLiveData.observe(this, Observer {
                if(it.statusCode != null){
                    Utils().showSnackBar(this@MainActivity, dialogView, it.message, android.R.color.holo_red_light, android.R.color.white)
                }else{
                    srl_refresh.isRefreshing = true
                    contactViewModel.fetchAllContact()
                    dialog.dismiss()
                    Utils().showSnackBar(this@MainActivity, container, it.message, android.R.color.holo_green_light, android.R.color.black)
                }
            })

        }
    }

    /**
     *
     * Function for show dialog delete contact
     *
     * **/
    private fun showDialogDeleteContact(id: String, name: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Contact")

        val dialogView = layoutInflater.inflate(R.layout.dialog_delete, null)

        val mTVMessage = dialogView.findViewById<TextView>(R.id.tv_message)

        mTVMessage.text = "Are you sure want to delete $name?"

        builder.setView(dialogView)

        builder.setPositiveButton("Delete") { dialog, _ ->

        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            deleteContact(id, dialog, dialogView)
        })
    }

    /**
     *
     * Function for delete contact
     *
     * **/
    private fun deleteContact(id: String, dialog: AlertDialog, dialogView: View){
        contactViewModel.deleteContactById(id)

        contactViewModel.deleteContactByIdLiveData.observe(this, Observer {
            if(it.statusCode != null){
                Utils().showSnackBar(this@MainActivity, dialogView, it.message, android.R.color.holo_red_light, android.R.color.white)
            }else{
                srl_refresh.isRefreshing = true
                contactViewModel.fetchAllContact()
                dialog.dismiss()
                Utils().showSnackBar(this@MainActivity, container, it.message, android.R.color.holo_green_light, android.R.color.black)
            }
        })
    }
}
