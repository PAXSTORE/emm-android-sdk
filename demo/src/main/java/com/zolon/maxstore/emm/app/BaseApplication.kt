package com.zolon.maxstore.emm.app

import android.app.Application
import android.util.Log
import com.zolon.maxstore.emm.sdk.EMMSDK

class BaseApplication: Application() {
    companion object {
        private const val TAG = "BaseApplication"
        private const val APP_KEY = "3WR44AC9GALK3HYE5R8Y";
        private const val APP_SECRET = "0P5C5A182Y9DQ6YDJ62VGYC1C65DMTIFQE78LH6A";
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