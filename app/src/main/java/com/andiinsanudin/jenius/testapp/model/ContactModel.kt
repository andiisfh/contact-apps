package com.andiinsanudin.jenius.testapp.model

import com.andiinsanudin.jenius.testapp.model.base.BaseResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Andi Insanudin on 2019-06-12.
 */
data class ContactModel (
    val id: String,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val photo: String
)

data class ContactRequestModel (
    val firstName: String,
    val lastName: String,
    val age: Int,
    val photo: String
)

interface ContactAPI{
    @GET("/contact")
    fun getAllContactAsync(): Deferred<Response<BaseResponse<List<ContactModel>>>>

    @GET("/contact/{id}")
    fun getContactByIdAsync(@Path("id") id: String):Deferred<Response<BaseResponse<ContactModel>>>

    @POST("/contact")
    fun addContactAsync(@Body contactRequestModel: ContactRequestModel):Deferred<Response<BaseResponse<ContactModel>>>

    @PUT("/contact/{id}")
    fun updateContactAsync(@Path("id") id: String, @Body contactRequestModel: ContactRequestModel):Deferred<Response<BaseResponse<ContactModel>>>

    @DELETE("/contact/{id}")
    fun deleteContactByIdAsync(@Path("id") id: String):Deferred<Response<BaseResponse<ContactModel>>>
}