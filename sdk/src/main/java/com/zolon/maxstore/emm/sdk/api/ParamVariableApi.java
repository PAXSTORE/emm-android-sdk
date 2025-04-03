package com.zolon.maxstore.emm.sdk.api;

import static com.zolon.maxstore.emm.sdk.java.base.util.SHA256Utils.byte2hex;
import static com.zolon.maxstore.emm.sdk.java.base.util.SHA256Utils.encryptHMAC;

import android.content.Context;
import android.util.Log;

import com.zolon.maxstore.emm.sdk.api.dto.IdentifierObject;
import com.zolon.maxstore.emm.sdk.api.dto.ParamListObject;
import com.zolon.maxstore.emm.sdk.api.dto.ParamObject;
import com.zolon.maxstore.emm.sdk.java.base.api.BaseApi;
import com.zolon.maxstore.emm.sdk.java.base.request.SdkRequest;
import com.zolon.maxstore.emm.sdk.java.base.util.CryptoUtils;
import com.zolon.maxstore.emm.sdk.java.base.util.JsonUtils;
import com.zolon.maxstore.emm.sdk.util.PreferencesUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public final class ParamVariableApi extends BaseApi {
    private static final String TAG = "ParamVariableApi";

    private static final String DOWNLOAD_PARAM_URL = "/v1/3rdApps/emm/variables";
    private static final String DOWNLOAD_SN_URL = "/v1/3rdApps/emm/identifier";

    private static final String REQ_PARAM_MARKET_ID = "X-Device-Market";
    private static final String REQ_PARAM_SN = "X-Device-SN";
    private static final String REQ_PARAM_VARIABLE_SIGN = "variablesignature";

    private static final String SP_KEY_VARIABLE_SIGN = "variable_sign";

    private final Context context;
    private final long marketId;

    public ParamVariableApi(Context context, String baseUrl, String appKey, String appSecret, String terminalSN, long marketId) {
        super(baseUrl, appKey, appSecret, terminalSN);
        this.context = context;
        this.marketId = marketId;
    }

    public ParamListObject getParamVariables() {
        SdkRequest request = new SdkRequest(DOWNLOAD_PARAM_URL);
        request.addHeader(REQ_PARAM_SN, getTerminalSN());
        request.addHeader(REQ_PARAM_MARKET_ID, Long.toString(marketId));

        String variableSign = PreferencesUtils.getString(context, SP_KEY_VARIABLE_SIGN, "");
        Log.d(TAG, "variableSign: " + variableSign);
        request.addHeader(REQ_PARAM_VARIABLE_SIGN, variableSign);

        ParamListObject result = JsonUtils.fromJson(call(request), ParamListObject.class);
        saveVariableSignature(result.getList());

        return result;
    }

    public IdentifierObject getIdentifier() {
        SdkRequest request = new SdkRequest(DOWNLOAD_SN_URL);
        request.addHeader(REQ_PARAM_SN, getTerminalSN());
        request.addHeader(REQ_PARAM_MARKET_ID, Long.toString(marketId));
        return JsonUtils.fromJson(call(request), IdentifierObject.class);
    }

    private void saveVariableSignature(List<ParamObject> vinfos) {
        if (vinfos == null) return;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < vinfos.size(); i++) {
            String key = vinfos.get(i).getKey();
            String value = vinfos.get(i).getValue();
            builder.append(String.format("%s=%s", key != null ? key : "", value != null ? value : ""));
            if (i != vinfos.size() - 1) {
                builder.append("&");
            }
        }

        byte[] encrypt = new byte[0];
        try {
            encrypt = encryptHMAC(builder.toString(), getAppSecret(), CryptoUtils.getM());
        } catch (GeneralSecurityException | IOException ignored) {
        }

        String str = byte2hex(encrypt);
        PreferencesUtils.putString(context, SP_KEY_VARIABLE_SIGN, str);
    }
}
