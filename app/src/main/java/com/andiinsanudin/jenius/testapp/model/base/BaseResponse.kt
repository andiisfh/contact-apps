package com.andiinsanudin.jenius.testapp.model.base

/**
 * Created by Andi Insanudin on 2019-06-15.
 */
data class BaseResponse<T> (
    val statusCode: Int?,
    var error: String,
    val message: String,
    val validation: Validation?,
    val data: T?
)

data class Validation(
    val source: String,
    val keys: List<String>
)