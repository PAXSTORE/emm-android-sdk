package com.zolon.maxstore.emm.sdk.api;

import com.zolon.maxstore.emm.sdk.java.base.api.BaseApi;
import com.zolon.maxstore.emm.sdk.java.base.constant.Constants;
import com.zolon.maxstore.emm.sdk.java.base.request.SdkRequest;
import com.zolon.maxstore.emm.sdk.java.base.util.JsonUtils;

public final class ParamVariableApi extends BaseApi {
    private static final String DOWNLOAD_PARAM_URL = "/3rdApps/emm/variables";
    private static final String DOWNLOAD_SN_URL = "/3rdApps/emm/identifier";

    private static final String REQ_PARAM_MARKET_ID = "X-Device-Market";

    private final long marketId;

    public ParamVariableApi(String baseUrl, String appKey, String appSecret, String terminalSN, long marketId) {
        super(baseUrl, appKey, appSecret, terminalSN);
        this.marketId = marketId;
    }

    public ParamListObject getParamVariables() {
        SdkRequest request = new SdkRequest(DOWNLOAD_PARAM_URL);
        request.addHeader(Constants.REQ_HEADER_SN, getTerminalSN());
        request.addHeader(REQ_PARAM_MARKET_ID, Long.toString(marketId));
        return JsonUtils.fromJson(call(request), ParamListObject.class);
    }

    public IdentifierObject getIdentifier() {
        SdkRequest request = new SdkRequest(DOWNLOAD_SN_URL);
        request.addHeader(Constants.REQ_HEADER_SN, getTerminalSN());
        request.addHeader(REQ_PARAM_MARKET_ID, Long.toString(marketId));
        return JsonUtils.fromJson(call(request), IdentifierObject.class);
    }
}
