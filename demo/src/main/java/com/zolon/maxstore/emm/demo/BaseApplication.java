package com.zolon.maxstore.emm.demo;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.zolon.maxstore.emm.sdk.EMMSDK;

/**
 * Created by fojut on 2017/8/24.
 */

public class BaseApplication extends Application {

    private static final String TAG = BaseApplication.class.getSimpleName();

    private boolean isReadyToUpdate = true;

    //todo make sure to replace with your own app's appkey and appsecret
    private static String appkey;
    private static String appSecret;



    //todo please make sure get the correct SN here, for pax device you can integrate NeptuneLite SDK to get the correct SN


    @Override
    public void onCreate() {
        super.onCreate();
        //initial the SDK
        initKeyAndSecret();
        initStoreSdk();

    }

    private void initKeyAndSecret() {
        ApplicationInfo appInfo;
        try {
            appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            appkey = String.valueOf(appInfo.metaData.get("Access_Key"));
            appSecret = String.valueOf(appInfo.metaData.get("SECRET"));
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "initKeyAndSecret : " + e);
        }
        Log.w(TAG, "int key: " + appkey + " Secret: " + appSecret);
    }

    private void initStoreSdk() {
        //todo 1. Init AppKey，AppSecret and SN, make sure the appkey and appSecret is corret.
        EMMSDK.getInstance().init(getApplicationContext(), appkey, appSecret, new  EMMSDK.InitCallback() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "initSuccess.");
            }

            @Override
            public void onFailed(Throwable e) {
                Log.i(TAG, "initFailed: "+e.getMessage());
            }
        });
    }

}
