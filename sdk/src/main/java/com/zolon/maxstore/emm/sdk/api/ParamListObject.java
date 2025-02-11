package com.zolon.maxstore.emm.sdk.api;

import com.google.gson.annotations.SerializedName;
import com.zolon.maxstore.emm.sdk.java.base.dto.SdkObject;

import java.util.List;

public class ParamListObject extends SdkObject {
    @SerializedName("businessCode")
    private long businessCode;

    @SerializedName("vinfos")
    private List<ParamObject> list;
}
