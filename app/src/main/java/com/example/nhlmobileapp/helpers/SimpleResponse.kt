package com.example.nhlmobileapp.helpers

import org.json.JSONObject
import retrofit2.Response

data class SimpleResponse<T>(
    val status: Status,
    val data: Response<T>?,
    val message: String?,
    val statusCode: Int,
    val exception: Exception?
) {

    companion object {
        fun <T> success(data: Response<T>): SimpleResponse<T> {
            return SimpleResponse(
                status = Status.Success,
                data = data,
                message = null,
                statusCode = 200,
                exception = null
            )
        }

        fun <T> error(data: Response<T>): SimpleResponse<T> {
            return SimpleResponse(
                status = Status.Error,
                data = null,
                message = JSONObject(data.errorBody()!!.string()).get("Message").toString(),
                statusCode = data.code(),
                exception = null
            )
        }

        fun <T> failure(exception: Exception): SimpleResponse<T> {
            return SimpleResponse(
                status = Status.Failure,
                data = null,
                message = null,
                statusCode = 500,
                exception = exception
            )
        }
    }

    sealed class Status {
        object Success: Status()
        object Error: Status()
        object Failure: Status()
    }


    val failed: Boolean
        get() = this.status == Status.Failure


    val isError: Boolean
        get() = this.status == Status.Error

    val isSuccessful: Boolean
        get() = !this.failed && this.data?.isSuccessful == true

    val body: T
        get() = this.data!!.body()!!
}
