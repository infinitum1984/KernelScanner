package com.kernel.scanner

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig

class KernelApplication : Application(), CameraXConfig.Provider{
    companion object{
        private var application:Application?=null

        fun getContext()= application?.applicationContext
    }
    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }

    override fun onCreate() {
        super.onCreate()
        application=this
    }
}