package com.zolon.maxstore.emm.sdk.api;

import com.google.gson.annotations.SerializedName;

public class ParamObject {
    @SerializedName("k")
    private String key;
    @SerializedName("v")
    private String value;
    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
