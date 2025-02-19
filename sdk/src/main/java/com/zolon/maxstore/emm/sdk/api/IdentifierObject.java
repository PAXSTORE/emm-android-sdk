package com.zolon.maxstore.emm.sdk.api;

import com.google.gson.annotations.SerializedName;

public class IdentifierObject {
    @SerializedName("businessCode")
    private int businessCode;
    @SerializedName("serialNo")
    private String serialNo;
    @SerializedName("imei")
    private String imei;
}
