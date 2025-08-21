package com.zolon.maxstore.emm.demo

import android.app.Application
import android.util.Log
import com.zolon.maxstore.emm.sdk.EMMSDK

class BaseApplication: Application() {
    companion object {
        private const val TAG = "BaseApplication"
        private const val APP_KEY = "YNQ3MT30KSK9TS64J0EZ";
        private const val APP_SECRET = "60BCA6W5Y55KWLD8LZ5VKE6E7FV28Y65721W4DBB";
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