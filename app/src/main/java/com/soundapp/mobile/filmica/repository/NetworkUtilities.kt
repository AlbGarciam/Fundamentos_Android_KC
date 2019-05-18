package com.soundapp.mobile.filmica.repository

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.security.ProviderInstaller
import org.json.JSONArray
import org.json.JSONObject

object NetworkUtilities {
    private val cache = HashMap<String, JSONArray>()

    private fun updateAndroidSecurityProvider(context: Context) {
        try {
            ProviderInstaller.installIfNeeded(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun GET_LIST(context: Context, path: String, success: (JSONArray) -> Unit, failure: (Error?) -> Unit) {
        val cached = cache[path]
        if (cached != null) {
            success.invoke(cached)
            return
        }

        val request = JsonObjectRequest(Request.Method.GET, path, null, { response ->
            val array = response.getJSONArray("results")
            if (array != null)
                cache[path] = array
            success.invoke(array)
        }, {
            failure.invoke(null)
        })
        makeRequest(context, request)
    }

    private fun makeRequest(context: Context, request: JsonObjectRequest) {
        Log.d("NetworkUtilities","Making request ${request.url}")
        /*** WORKAROUND FOR ANDROID KITKAT AND OLDER
         * https://stackoverflow.com/questions/42999914/com-android-volley-noconnectionerror-javax-net-ssl-sslexception-connection-clo
         ***/
        updateAndroidSecurityProvider(context)
        /*** END - WORKAROUND FOR ANDROID KITKAT AND OLDER ***/
        Volley.newRequestQueue(context).add(request)
    }
}