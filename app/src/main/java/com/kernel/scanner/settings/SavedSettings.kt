package com.kernel.scanner.settings

import android.content.Context

object SavedSettings {
    fun getIsLowerBrightness(context: Context): Boolean{
        return context.getSharedPreferences("SETTINGS",Context.MODE_PRIVATE).getBoolean("LowerBrightness", true)
    }
    fun saveIsLowerBrightness(context: Context, lowerBrightness:Boolean): Boolean{
        return context.getSharedPreferences("SETTINGS",Context.MODE_PRIVATE)
            .edit().putBoolean("LowerBrightness", lowerBrightness).commit()
    }

}