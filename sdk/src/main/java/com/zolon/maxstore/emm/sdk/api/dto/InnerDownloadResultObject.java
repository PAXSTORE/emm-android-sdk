package com.zolon.maxstore.emm.sdk.api.dto;

import com.google.gson.annotations.SerializedName;
import com.zolon.maxstore.emm.sdk.java.base.dto.SdkObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * This is for aar use, not for users.
 */
public class InnerDownloadResultObject extends SdkObject {

    /**
     * path that param files saved
     */
    @SerializedName("paramSavePath")
    String paramSavePath;

    @SerializedName("actionList")
    ArrayList<Long> actionList;

    LinkedHashMap<String, String> idPathMap;


    public String getParamSavePath() {
        return paramSavePath;
    }

    public void setParamSavePath(String paramSavePath) {
        this.paramSavePath = paramSavePath;
    }

    public void setActionList(ArrayList<Long> actionList) {
        this.actionList = actionList;
    }

    public ArrayList<Long> getActionList() {
        return actionList;
    }


    public LinkedHashMap<String, String> getIdPathMap() {
        return idPathMap;
    }

    public void setIdPathMap(LinkedHashMap<String, String> idPathMap) {
        this.idPathMap = idPathMap;
    }


    @Override
    public String toString() {
        return "InnerDownloadResultObject{" +
                "paramSavePath='" + paramSavePath + '\'' +
                ", actionList=" + actionList +
                ", idPathMap=" + idPathMap +
                '}';
    }
}
