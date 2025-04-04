package com.zolon.maxstore.emm.demo

import android.app.Application
import android.util.Log
import com.zolon.maxstore.emm.sdk.EMMSDK

class BaseApplication: Application() {
    companion object {
        private const val TAG = "BaseApplication"
        private const val APP_KEY = "Your key";
        private const val APP_SECRET = "Your secret";
    }

    override fun onCreate() {
        super.onCreate()
        EMMSDK.getInstance().init(this, APP_KEY, APP_SECRET, object : EMMSDK.InitCallback {
            override fun onSuccess() {
                Log.d(TAG, "initSuccess")
            }

            override fun onFailed(t: Throwable?) {
                Log.e(TAG, "initFailed", t)
            }
        })
    }
}