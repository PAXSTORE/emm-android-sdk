package com.zolon.maxstore.emm.sdk.api.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ParamObject implements Serializable {
    private static final long serialVersionUID = 1L;

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
