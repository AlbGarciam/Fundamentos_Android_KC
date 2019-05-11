package com.soundapp.mobile.filmica.screens.utils

import android.animation.Animator

class AnimatorEndListener(val endCallback: (Animator) -> Unit): Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator?) {}

    override fun onAnimationEnd(animation: Animator) {
        endCallback.invoke(animation)
    }

    override fun onAnimationCancel(animation: Animator?) { }

    override fun onAnimationStart(animation: Animator?) { }
}