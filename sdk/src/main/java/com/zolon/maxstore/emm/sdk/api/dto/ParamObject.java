package com.zolon.maxstore.emm.sdk.api.dto;

import com.google.gson.annotations.SerializedName;
import com.zolon.maxstore.emm.sdk.java.base.dto.SdkObject;

public class ParamObject extends SdkObject {
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
