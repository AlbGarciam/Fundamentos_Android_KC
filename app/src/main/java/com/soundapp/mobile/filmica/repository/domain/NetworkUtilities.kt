package com.soundapp.mobile.filmica.repository.domain

import android.content.Context
import com.google.android.gms.security.ProviderInstaller

object NetworkUtilities {
     fun updateAndroidSecurityProvider(context: Context) {
        try {
            ProviderInstaller.installIfNeeded(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}