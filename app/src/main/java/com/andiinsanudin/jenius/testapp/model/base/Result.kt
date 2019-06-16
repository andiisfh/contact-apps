package com.andiinsanudin.jenius.testapp.model.base

/**
 * Created by Andi Insanudin on 2019-06-12.
 */
sealed class Result<out T: Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}