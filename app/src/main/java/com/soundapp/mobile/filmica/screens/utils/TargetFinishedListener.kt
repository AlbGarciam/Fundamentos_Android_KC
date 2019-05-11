package com.soundapp.mobile.filmica.screens.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class TargetFinishedListener(val callback: (Bitmap) -> Unit ): Target {
    /**
     * Callback invoked right before your request is submitted.
     *
     *
     * **Note:** The passed [Drawable] may be `null` if none has been
     * specified via [RequestCreator.placeholder]
     * or [RequestCreator.placeholder].
     */
    override fun onPrepareLoad(placeHolderDrawable: Drawable?) { }

    /**
     * Callback indicating the image could not be successfully loaded.
     *
     *
     * **Note:** The passed [Drawable] may be `null` if none has been
     * specified via [RequestCreator.error]
     * or [RequestCreator.error].
     */
    override fun onBitmapFailed(errorDrawable: Drawable?) { }

    /**
     * Callback when an image has been successfully loaded.
     *
     *
     * **Note:** You must not recycle the bitmap.
     */
    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
        callback.invoke(bitmap)
    }
}