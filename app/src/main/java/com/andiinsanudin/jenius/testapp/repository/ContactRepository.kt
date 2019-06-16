package com.andiinsanudin.jenius.testapp.repository

import com.andiinsanudin.jenius.testapp.model.ContactAPI
import com.andiinsanudin.jenius.testapp.model.ContactModel
import com.andiinsanudin.jenius.testapp.model.ContactRequestModel
import com.andiinsanudin.jenius.testapp.model.base.BaseRepository
import com.andiinsanudin.jenius.testapp.model.base.BaseResponse

/**
 * Created by Andi Insanudin on 2019-06-12.
 */
class ContactRepository (private val api: ContactAPI) : BaseRepository() {

    suspend fun getAllContact(): BaseResponse<List<ContactModel>>? {
        return safeApiCall(
            call = { api.getAllContactAsync().await() },
            errorMessage = "Error Fetching Get All Contact"
        )
    }

    suspend fun getContactById(id: String): BaseResponse<ContactModel>? {
        return safeApiCall(
            call = { api.getContactByIdAsync(id).await() },
            errorMessage = "Error Fetching Contact By ID"
        )
    }

    suspend fun addContact(contactRequestModel: ContactRequestModel): BaseResponse<ContactModel>? {
        return safeApiCall(
            call = { api.addContactAsync(contactRequestModel).await() },
            errorMessage = "Error Post Add Contact"
        )
    }

    suspend fun updateContact(id: String, contactRequestModel: ContactRequestModel): BaseResponse<ContactModel>? {
        return safeApiCall(
            call = { api.updateContactAsync(id, contactRequestModel).await() },
            errorMessage = "Error Post Update Contact"
        )
    }

    suspend fun deleteContactById(id: String): BaseResponse<ContactModel>? {
        return safeApiCall(
            call = { api.deleteContactByIdAsync(id).await() },
            errorMessage = "Error Delete Contact By ID"
        )
    }
}