package com.zolon.maxstore.emm.sdk;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.zolon.maxstore.emm.sdk.api.ParamVariableApi;
import com.zolon.maxstore.emm.sdk.java.base.exception.NotInitException;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangchenyang on 2018/5/23.
 */

public class EMMSDK {
    private static final String TAG = "EMMSDK";
    private static final String INIT_PERMISSION = "com.zolon.maxstore.emm.SN_HOST";

    private static volatile EMMSDK instance;
    private ParamVariableApi paramVariableApi;

    private Semaphore semaphore;

    private Context context;
    private String appKey;
    private String appSecret;
    private String url;
    private String terminalSn;
    private long marketId;

    public EMMSDK() {
        semaphore = new Semaphore(2);
    }

    public static EMMSDK getInstance() {
        if (instance == null) {
            synchronized (EMMSDK.class) {
                if (instance == null) {
                    instance = new EMMSDK();
                }
            }
        }
        return instance;
    }

    public void init(final Context context, final String appKey, final String appSecret, InitCallback callback) {
        int permissionStatus = context.checkSelfPermission(INIT_PERMISSION);
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            // 没有权限
            callback.onFailed(new SecurityException("Init permission not granted"));
            return;
        }
        if (paramVariableApi == null && semaphore.availablePermits() != 1) {
            validParams(context, appKey, appSecret);
            this.context = context;
            this.appKey = appKey;
            this.appSecret = appSecret;
            try {
                Log.d(TAG, "init acquire 1");
                semaphore.acquire(1);
            } catch (InterruptedException e) {
                Log.e(TAG, "InterruptedException", e);
            }
            BaseApiService.getInstance().init(context, new BaseApiService.InitCallback() {
                @Override
                public void onSuccess(String apiUrl, String sn, long marketId) {
                    EMMSDK.this.url = apiUrl;
                    EMMSDK.this.terminalSn = sn;
                    EMMSDK.this.marketId = marketId;

                    paramVariableApi = new ParamVariableApi(context, apiUrl, appKey, appSecret, sn, marketId);

                    semaphore.release(1);
                    Log.d(TAG, "initSuccess >> release acquire 1");
                    callback.onSuccess();
                }

                @Override
                public void onFailed(Throwable e) {
                    semaphore.release(1);
                    Log.e(TAG, "initFailed >> release acquire 1", e);
                    callback.onFailed(e);
                }
            });
        } else {
            Log.d(TAG, "Initialization is on process or has been done");
        }
    }

    public ParamVariableApi getParamVariableApi() throws NotInitException {
        if (paramVariableApi == null) {
            acquireSemaphore();
            if (paramVariableApi == null) {
                throw new NotInitException("Not initialized");
            }
        }
        return paramVariableApi;
    }

    public boolean checkInitialization() {
        if (paramVariableApi != null) {
            return true;
        }
        return false;
    }

    /**
     * Make sure StoreSdk is not initailizing now.
     * <p>
     * Since developer will call {@link #getParamVariableApi()}
     * when doing {@link #init}(which will take 1 to 2 seconds to finish),
     * at these period, any StoreSdk api call will fail.
     * So we add these method to hold the api call, until {@link #init} get
     * a result or timeout after 5 seconds.
     */
    public void acquireSemaphore() {
        try {
            Log.d(TAG, "acquireSemaphore api try acquire 2");
            long startTime = System.currentTimeMillis();
            semaphore.tryAcquire(2, 5, TimeUnit.SECONDS);
            Log.d(TAG, "tryAcquire cost Time:" + (System.currentTimeMillis() - startTime));
        } catch (InterruptedException e) {
            Log.e(TAG, "InterruptedException", e);
        }
        if (semaphore.availablePermits() == 0) {
            semaphore.release(2);
            Log.d(TAG, "acquireSemaphore api release acquire 2");
        }
    }

    /**
     * Context, appKey， appSecret， terminalSerialNo will be validated,
     * NullPointerException will be throw when any of this is null.
     *
     * @param context
     * @param appKey
     * @param appSecret
     * @throws NullPointerException
     */
    private void validParams(Context context, String appKey, String appSecret) throws NullPointerException {
        if (context == null) {
            throw new NullPointerException("Context needed");
        }
        if (appKey == null || appKey.isEmpty()) {
            throw new NullPointerException("AppKey needed");
        }
        if (appSecret == null || appSecret.isEmpty()) {
            throw new NullPointerException("AppSecret needed");
        }
    }

    public interface InitCallback {
        void onSuccess();
        void onFailed(Throwable t);
    }
}
