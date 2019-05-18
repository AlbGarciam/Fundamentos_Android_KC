package com.soundapp.mobile.filmica.repository.domain

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.security.ProviderInstaller
import org.json.JSONArray

object NetworkUtilities {
     fun updateAndroidSecurityProvider(context: Context) {
        try {
            ProviderInstaller.installIfNeeded(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun GET_LIST(context: Context, path: String, success: (JSONArray) -> Unit, failure: (Error?) -> Unit) {
        val request = JsonObjectRequest(Request.Method.GET, path, null, { response ->
            success.invoke(response.getJSONArray("results"))
        }, {
            failure.invoke(null)
        })
        makeRequest(context, request)
    }

    private fun makeRequest(context: Context, request: JsonObjectRequest) {
        /*** WORKAROUND FOR ANDROID KITKAT AND OLDER
         * https://stackoverflow.com/questions/42999914/com-android-volley-noconnectionerror-javax-net-ssl-sslexception-connection-clo
         ***/
        updateAndroidSecurityProvider(context)
        /*** END - WORKAROUND FOR ANDROID KITKAT AND OLDER ***/
        Volley.newRequestQueue(context).add(request)
    }
}