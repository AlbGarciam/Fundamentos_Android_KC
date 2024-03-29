package com.soundapp.mobile.filmica.repository.films

import android.content.Context

interface DataSourceRepository<T> {

    fun get(params: HashMap<String, String>, context: Context, callback: ((List<T>) -> Unit)? = null, error: ((Error?) -> Unit)? = null)
}