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
        EMMSDK.getInstance().init(this, "", "", object : EMMSDK.InitCallback {
            override fun onSuccess() {
                Log.d(TAG, "initSuccess")
            }

            override fun onFailed(t: Throwable?) {
                Log.e(TAG, "initFailed", t)
            }
        })
    }
}