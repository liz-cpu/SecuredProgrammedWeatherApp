package com.example.nhlmobileapp.helpers

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.nhlmobileapp.MainActivity
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.net.URLEncoder

class CustomInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val newRequest: Request = if (request.body != null) {
            val bodyStr: String = urlStringToJson(bodyToString(request.body!!))
            val newBody = "body=" + URLEncoder.encode(RSAEncryption.encrypt(bodyStr), "utf-8")
            request.newBuilder()
                .addHeader("Authorization", CryptoManager("authToken").decrypt(MainActivity.fileDirectory))
                .post(newBody.toRequestBody()).build()
        } else {
            request.newBuilder()
                .addHeader("Authorization", CryptoManager("authToken").decrypt(MainActivity.fileDirectory))
                .build()
        }
        return chain.proceed(newRequest)
    }

    private fun bodyToString(request: RequestBody): String {
        var result: String
        try {
            val copy: RequestBody = request
            val buffer = okio.Buffer()
            copy.writeTo(buffer)
            result = buffer.readUtf8()
            buffer.close()

        } catch (e: Exception) {
            result = ""
        }

        return result
    }

    private fun urlStringToJson(str: String): String {
        val json = JSONObject()
        str.split("&").forEach(fun(item: String) {
            val kv = item.split("=")
            json.put(kv.get(0), kv.get(1))
        })
        return json.toString()
    }

}