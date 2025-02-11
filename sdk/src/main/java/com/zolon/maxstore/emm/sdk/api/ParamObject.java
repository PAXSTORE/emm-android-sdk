package com.zolon.maxstore.emm.sdk.api;

import com.google.gson.annotations.SerializedName;
import com.zolon.maxstore.emm.sdk.java.base.dto.SdkObject;

public class ParamObject extends SdkObject {
    @SerializedName("k")
    private String key;
    @SerializedName("v")
    private String value;
}
