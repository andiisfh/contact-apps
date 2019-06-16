package com.andiinsanudin.jenius.testapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andiinsanudin.jenius.testapp.model.ContactModel
import com.andiinsanudin.jenius.testapp.model.ContactRequestModel
import com.andiinsanudin.jenius.testapp.model.base.BaseResponse
import com.andiinsanudin.jenius.testapp.repository.ContactRepository
import com.andiinsanudin.jenius.testapp.retorfit.RetrofitFactory
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Created by Andi Insanudin on 2019-06-12.
 */
class ContactViewModel : ViewModel() {
    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : ContactRepository = ContactRepository(RetrofitFactory.service)

    val contactLiveData = MutableLiveData<BaseResponse<List<ContactModel>>>()

    fun fetchAllContact(){
        scope.launch {
            val allContact = repository.getAllContact()
            contactLiveData.postValue(allContact)
        }
    }

    val contactByIdLiveData = MutableLiveData<BaseResponse<ContactModel>>()

    fun fetchContactById(id: String){
        scope.launch {
            val contactById = repository.getContactById(id)
            contactByIdLiveData.postValue(contactById)
        }
    }

    val addContactLiveData = MutableLiveData<BaseResponse<ContactModel>>()

    fun postAddContact(contactRequestModel: ContactRequestModel){
        scope.launch {
            val addContact = repository.addContact(contactRequestModel)
            addContactLiveData.postValue(addContact)
        }
    }

    val updateContactLiveData = MutableLiveData<BaseResponse<ContactModel>>()

    fun postUpdateContact(id: String, contactRequestModel: ContactRequestModel){
        scope.launch {
            val updateContact = repository.updateContact(id, contactRequestModel)
            updateContactLiveData.postValue(updateContact)
        }
    }

    val deleteContactByIdLiveData = MutableLiveData<BaseResponse<ContactModel>>()

    fun deleteContactById(id: String){
        scope.launch {
            val deleteContactById = repository.deleteContactById(id)
            deleteContactByIdLiveData.postValue(deleteContactById)
        }
    }

    fun cancelAllRequests() = coroutineContext.cancel()
}