package com.soundapp.mobile.filmica.screens.utils

import android.text.Editable
import android.text.TextWatcher

class FilmicaTextWatcher(val callback: (CharSequence) -> Unit) : TextWatcher {
    override fun afterTextChanged(s: Editable?) { }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        callback.invoke(s ?: "")
    }

}