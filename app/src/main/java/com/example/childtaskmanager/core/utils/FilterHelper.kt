package com.example.childtaskmanager.core.utils

import android.os.Build
import com.example.childtaskmanager.core.utils.FilterHelper
import android.graphics.BlendMode
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.BlendModeColorFilter
import androidx.annotation.RequiresApi

object FilterHelper {
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun getBlendMode(mode: Mode): BlendMode {
        return when (mode) {
            Mode.CLEAR -> BlendMode.CLEAR
            Mode.SRC -> BlendMode.SRC
            Mode.DST -> BlendMode.DST
            Mode.SRC_OVER -> BlendMode.SRC_OVER
            Mode.DST_OVER -> BlendMode.DST_OVER
            Mode.SRC_IN -> BlendMode.SRC_IN
            Mode.DST_IN -> BlendMode.DST_IN
            Mode.SRC_OUT -> BlendMode.SRC_OUT
            Mode.DST_OUT -> BlendMode.DST_OUT
            Mode.SRC_ATOP -> BlendMode.SRC_ATOP
            Mode.DST_ATOP -> BlendMode.DST_ATOP
            Mode.XOR -> BlendMode.XOR
            Mode.DARKEN -> BlendMode.DARKEN
            Mode.LIGHTEN -> BlendMode.LIGHTEN
            Mode.MULTIPLY -> BlendMode.MULTIPLY
            Mode.SCREEN -> BlendMode.SCREEN
            Mode.ADD -> BlendMode.PLUS
            Mode.OVERLAY -> BlendMode.OVERLAY
        }
        return BlendMode.SCREEN
    }

    private fun getPorterDuffMode(mode: Mode): PorterDuff.Mode {
        return when (mode) {
            Mode.CLEAR -> PorterDuff.Mode.CLEAR
            Mode.SRC -> PorterDuff.Mode.SRC
            Mode.DST -> PorterDuff.Mode.DST
            Mode.SRC_OVER -> PorterDuff.Mode.SRC_OVER
            Mode.DST_OVER -> PorterDuff.Mode.DST_OVER
            Mode.SRC_IN -> PorterDuff.Mode.SRC_IN
            Mode.DST_IN -> PorterDuff.Mode.DST_IN
            Mode.SRC_OUT -> PorterDuff.Mode.SRC_OUT
            Mode.DST_OUT -> PorterDuff.Mode.DST_OUT
            Mode.SRC_ATOP -> PorterDuff.Mode.SRC_ATOP
            Mode.DST_ATOP -> PorterDuff.Mode.DST_ATOP
            Mode.XOR -> PorterDuff.Mode.XOR
            Mode.DARKEN -> PorterDuff.Mode.DARKEN
            Mode.LIGHTEN -> PorterDuff.Mode.LIGHTEN
            Mode.MULTIPLY -> PorterDuff.Mode.MULTIPLY
            Mode.SCREEN -> PorterDuff.Mode.SCREEN
            Mode.ADD -> PorterDuff.Mode.ADD
            Mode.OVERLAY -> PorterDuff.Mode.OVERLAY
        }
        return PorterDuff.Mode.SCREEN
    }

    fun setColorFilter(background: Drawable, color: Int, mode: Mode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            background.colorFilter = BlendModeColorFilter(color, getBlendMode(mode))
        } else {
            background.setColorFilter(color, getPorterDuffMode(mode))
        }
    }

    enum class Mode {
        CLEAR, SRC, DST, SRC_OVER, DST_OVER, SRC_IN, DST_IN, SRC_OUT, DST_OUT, SRC_ATOP, DST_ATOP, XOR, DARKEN, LIGHTEN, MULTIPLY, SCREEN, ADD, OVERLAY
    }
}