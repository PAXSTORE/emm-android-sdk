package com.zolon.maxstore.emm.sdk.api.dto;

import com.google.gson.annotations.SerializedName;
import com.zolon.maxstore.emm.sdk.java.base.dto.SdkObject;

public class IdentifierObject extends SdkObject {
    @SerializedName("serialNo")
    private String serialNo;
    @SerializedName("imei")
    private String imei;

    public String getImei() {
        return imei;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }


    @Override
    public String toString() {
        return "IdentifierObject{" +
                super.toString()  + '\'' +
                "serialNo='" + serialNo + '\'' +
                ", imei='" + imei + '\'' +
                '}';
    }
}
