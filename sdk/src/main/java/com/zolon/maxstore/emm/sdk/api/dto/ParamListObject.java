package com.zolon.maxstore.emm.sdk.api.dto;

import com.google.gson.annotations.SerializedName;
import com.zolon.maxstore.emm.sdk.java.base.dto.SdkObject;

import java.util.List;

public class ParamListObject extends SdkObject {
    @SerializedName("changed")
    private boolean changed;
    @SerializedName("vinfos")
    private List<ParamObject> list;

    public boolean isChanged() {
        return changed;
    }

    public List<ParamObject> getList() {
        return list;
    }
}
