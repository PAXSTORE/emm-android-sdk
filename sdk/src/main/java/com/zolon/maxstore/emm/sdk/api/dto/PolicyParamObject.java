package com.zolon.maxstore.emm.sdk.api.dto;

import com.google.gson.annotations.SerializedName;
import com.zolon.maxstore.emm.sdk.java.base.dto.SdkObject;

public class PolicyParamObject extends SdkObject {


    @SerializedName("downloadUrl")
    private String downloadUrl;
    @SerializedName("paramVariables")
    private String paramVariables;
    @SerializedName("downloadUrlDetail")
    private CookieObject cookieDto; //下载cookies
    @SerializedName("sha256")
    private String sha256;
    @SerializedName("signature")
    private String signature;


    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getParamVariables() {
        return paramVariables;
    }

    public void setParamVariables(String paramVariables) {
        this.paramVariables = paramVariables;
    }

    public CookieObject getCookieDto() {
        return cookieDto;
    }

    public void setCookieDto(CookieObject cookieDto) {
        this.cookieDto = cookieDto;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getCookieSignature() {
        if (cookieDto != null) {
            return cookieDto.getCookieSignature();
        }
        return null;
    }

    public String getCookieExpires() {
        if (cookieDto != null) {
            return cookieDto.getCookieExpires();
        }
        return null;
    }

    public String getCookieKeyPairId() {
        if (cookieDto != null) {
            return cookieDto.getCookieKeyPairId();
        }
        return null;
    }


    @Override
    public String toString() {
        return "PolicyParamObject{" +
                "downloadUrl='" + downloadUrl + '\'' +
                ", paramVariables='" + paramVariables + '\'' +
                ", cookieDto=" + cookieDto +
                ", sha256='" + sha256 + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
