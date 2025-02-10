package com.zolon.maxstore.emm.sdk

import android.content.Context

class EMMSDK private constructor() {
    companion object {
        @JvmStatic
        val instance by lazy { EMMSDK() }
    }

    fun init(context: Context, appKey: String, appSecret: String, callback: InitCallback) {
        BaseApiService.getInstance().init(context, appKey, appSecret, object : BaseApiService.InitCallback {
            override fun onSuccess(apiUrl: String?, sn: String?) {
                callback.onSuccess()
            }

            override fun onFailed(e: Throwable?) {
                callback.onFailed()
            }
        })
    }

    interface InitCallback {
        fun onSuccess()
        fun onFailed()
    }
}