package com.zolon.maxstore.emm.sdk;

import static com.zolon.maxstore.emm.sdk.CommonConstants.ERR_MSG_NULL_RETURNED;
import static com.zolon.maxstore.emm.sdk.CommonConstants.ERR_MSG_PAXSTORE_MAY_NOT_INSTALLED;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.zolon.maxstore.emm.aidl.IApiUrlService;

public final class BaseApiService {
    private static final String TAG = "BaseApiService";

    private static final String INIT_ACTION = "com.zolon.maxstore.emm.aidl.API_URL_SERVICE";
    private static final String[] EMM_PACKAGE_NAMES = {
            "com.zolon.maxstore.emm",
            "com.zolon.maxstore.emm.us",
            "com.zolon.maxstore.emm.sit",
            "com.zolon.maxstore.emm.global",
    };

    private static volatile BaseApiService instance;

    private BaseApiService() {}

    public static BaseApiService getInstance() {
        if (instance == null) {
            synchronized (BaseApiService.class) {
                if (instance == null) {
                    instance = new BaseApiService();
                }
            }
        }
        return instance;
    }

    public void init(Context context, InitCallback callback) {
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    final IApiUrlService apiUrlService = IApiUrlService.Stub.asInterface(service);
                    final String apiUrl = apiUrlService.getApiUrl();
                    final String sn = apiUrlService.getSn();
                    final long marketId = apiUrlService.getMarketId();

                    if (apiUrl == null || apiUrl.isEmpty() || sn == null || sn.isEmpty()) {
                        callback.onFailed(new RemoteException(ERR_MSG_NULL_RETURNED));
                        return;
                    }

                    Log.d(TAG, String.format("apiUrl: %s, sn: %s, marketId: %s", apiUrl, sn, marketId));

                    callback.onSuccess(apiUrl, sn, marketId);
                } catch (RemoteException e) {
                    callback.onFailed(e);
                }
                context.unbindService(this);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e(TAG, "onServiceDisconnected");
            }
        };

        boolean bindResult = false;
        for (String pkgName : EMM_PACKAGE_NAMES) {
            Intent intent = new Intent(INIT_ACTION);
            intent.setPackage(pkgName);
            if (context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)) {
                bindResult = true;
                break;
            }
        }
        if (!bindResult) {
            callback.onFailed(new RemoteException(ERR_MSG_PAXSTORE_MAY_NOT_INSTALLED));
            context.unbindService(serviceConnection);
        }
    }

    public interface InitCallback {
        void onSuccess(String apiUrl, String sn, long marketId);
        void onFailed(Throwable e);
    }
}
