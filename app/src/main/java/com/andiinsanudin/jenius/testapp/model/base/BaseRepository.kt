package com.andiinsanudin.jenius.testapp.model.base

import android.util.Log
import com.andiinsanudin.jenius.testapp.model.ErrorResponse
import retrofit2.Response



/**
 * Created by Andi Insanudin on 2019-06-12.
 */
open class BaseRepository{

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): T? {

        val result : Result<T> = safeApiResult(call,errorMessage)
        var data : T? = null

        when(result) {
            is Result.Success ->
                data = result.data
            is Result.Error -> {
                Log.d("1.DataRepository", "$errorMessage & Exception - ${result.exception}")
            }
        }

        return data
    }

    private suspend fun <T: Any> safeApiResult(call: suspend ()-> Response<T>, errorMessage: String) : Result<T> {
        val response = call.invoke()
        if(response.isSuccessful) return Result.Success(response.body()!!)

        val errorResponse = ErrorResponse(response.errorBody()!!.string())

        val error = BaseResponse<T>(
            response.code(),
            errorResponse.error!!,
            errorResponse.message,
            null,
            null
        ) as T

        return Result.Success(error)

//       return Result.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
    }
}