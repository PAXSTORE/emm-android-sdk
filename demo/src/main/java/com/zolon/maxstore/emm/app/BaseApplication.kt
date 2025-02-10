package com.zolon.maxstore.emm.app

import android.app.Application
import android.util.Log
import com.zolon.maxstore.emm.sdk.EMMSDK

class BaseApplication: Application() {
    companion object {
        private const val TAG = "BaseApplication"
    }

    override fun onCreate() {
        super.onCreate()
        EMMSDK.instance.init(this, "", "", object : EMMSDK.InitCallback {
            override fun onSuccess() {
                Log.d(TAG, "initSuccess")
            }

            override fun onFailed() {
                Log.d(TAG, "initFailed")
            }
        })
    }
}