package com.zolon.maxstore.emm.sdk.api;

import com.zolon.maxstore.emm.sdk.java.base.api.BaseApi;
import com.zolon.maxstore.emm.sdk.java.base.constant.Constants;
import com.zolon.maxstore.emm.sdk.java.base.request.SdkRequest;
import com.zolon.maxstore.emm.sdk.java.base.util.JsonUtils;

public final class ParamVariableApi extends BaseApi {
    private static final String DOWNLOAD_PARAM_URL = "/3rdApps/emm/variables";
    private static final String REQ_PARAM_MARKET_ID = "X-Device-Market";

    public ParamVariableApi(String baseUrl, String appKey, String appSecret, String terminalSN) {
        super(baseUrl, appKey, appSecret, terminalSN);
    }

    public ParamListObject getParamVariables(long marketId) {
        SdkRequest request = new SdkRequest(DOWNLOAD_PARAM_URL);
        request.addHeader(Constants.REQ_HEADER_SN, getTerminalSN());
        request.addHeader(REQ_PARAM_MARKET_ID, Long.toString(marketId));
        return JsonUtils.fromJson(call(request), ParamListObject.class);
    }
}
