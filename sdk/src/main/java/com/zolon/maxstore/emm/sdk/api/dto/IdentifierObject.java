package com.zolon.maxstore.emm.sdk.api.dto;

import com.google.gson.annotations.SerializedName;
import com.zolon.maxstore.emm.sdk.java.base.dto.SdkObject;

public class IdentifierObject extends SdkObject {
    @SerializedName("serialNo")
    private String serialNo;
    @SerializedName("imei")
    private String imei;
}
