package com.andiinsanudin.jenius.testapp.model

import org.json.JSONObject

/**
 * Created by Andi Insanudin on 2019-06-16.
 */
class ErrorResponse (json: String) : JSONObject(json) {
    val statusCode: Int? = this.optInt("statusCode")
    var error: String? = this.optString("error")
    val message: String = this.optString("message")
 //   val validation = Validation(this.optString("validation"))
}


/*
class Validation(json: String) : JSONObject(json) {
    val source: String = this.optString("source")
    val keys= this.optJSONArray("keys")
        ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } }
        ?.map { it.toString() }
}*/
