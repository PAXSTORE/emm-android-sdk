/*
 * *******************************************************************************
 * COPYRIGHT
 *               PAX TECHNOLOGY, Inc. PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or
 *   nondisclosure agreement with PAX  Technology, Inc. and may not be copied
 *   or disclosed except in accordance with the terms in that agreement.
 *
 *      Copyright (C) 2017 PAX Technology, Inc. All rights reserved.
 * *******************************************************************************
 */
package com.zolon.maxstore.emm.sdk.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.zolon.maxstore.emm.sdk.CommonConstants;
import com.zolon.maxstore.emm.sdk.api.dto.PolicyParamObject;
import com.zolon.maxstore.emm.sdk.api.dto.UpdateActionObject;
import com.zolon.maxstore.emm.sdk.java.base.api.BaseApi;
import com.zolon.maxstore.emm.sdk.java.base.constant.Constants;
import com.zolon.maxstore.emm.sdk.java.base.constant.ResultCode;
import com.zolon.maxstore.emm.sdk.java.base.dto.SdkObject;
import com.zolon.maxstore.emm.sdk.java.base.exception.ParseXMLException;
import com.zolon.maxstore.emm.sdk.java.base.request.SdkRequest;
import com.zolon.maxstore.emm.sdk.java.base.util.FileUtils;
import com.zolon.maxstore.emm.sdk.java.base.util.HMACSignatureGenerator;
import com.zolon.maxstore.emm.sdk.java.base.util.JsonUtils;
import com.zolon.maxstore.emm.sdk.java.base.util.SHA256Utils;
import com.zolon.maxstore.emm.sdk.java.base.util.ZipUtil;
import com.zolon.maxstore.emm.sdk.util.PreferencesUtils;
import com.zolon.maxstore.emm.sdk.util.ReplaceUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



/**
 * Created by fanjun on 2016/12/5.
 */
public class ParamApi extends BaseApi {
    public static final String REMARKS_PARAM_DOWNLOADING = "15206";         //Param is downloading
    /**
     * The constant ACT_STATUS_PENDING.
     */
    public static final int ACT_STATUS_PENDING = 1;
    /**
     * The constant ACT_STATUS_SUCCESS.
     */
    public static final int ACT_STATUS_SUCCESS = 2;
    /**
     * The constant ACT_STATUS_FAILED.
     */
    public static final int ACT_STATUS_FAILED = 3;
    /**
     * The constant CODE_NONE_ERROR.
     */
    public static final int CODE_NONE_ERROR = 0;
    /**
     * The constant CODE_DOWNLOAD_ERROR.
     */
    public static final int CODE_DOWNLOAD_ERROR = 1;

    public static final int ERROR_CODE_PARAM_APPLY_FAILED = 48;
    public static final String REMARKS_CODE_PARAM_DOWNLOADED = "15223";
    public static final String REMARKS_CODE_PARAM_APPLIED = "15224";


    /**
     * The constant RETRY_COUNT.
     */
    public static final int RETRY_COUNT = 20;


    /**
     * The constant RETRY_TIME_LIMIT.
     */
    public static final long RETRY_TIME_LIMIT = 10 * 24 * 3600_000L;

    /**
     * Get last success param limit
     */
    public static final long GET_SUCCESS_PARAM_LIMIT = 60_000L;
    public static final String ERROR_CELLULAR_NOT_ALLOWED = "Cellular download not allowed";
    private static final String REQ_PARAM_PACKAGE_NAME = "packageName";
    private static final String REQ_PARAM_VERSION_CODE = "versionCode";
    private static final String REQ_PARAM_SHA256 = "X-Param-Sha256";


    private static final String REQ_PARAM_STATUS = "status";
    private static final String REQ_PARAM_ERROR_CODE = "errorCode";
    private static final String REQ_PARAM_REMARKS = "remarks";
    private static final String REQ_PARAM_TEMPLATE_NAME = "paramTemplateName";
    private static final String ERROR_REMARKS_REPLACE_VARIABLES = "Replace paramVariables failed";
    private static final String ERROR_REMARKS_NOT_GOOD_JSON = "Bad json : ";
    private static final String ERROR_REMARKS_VARIFY_MD_FAILED = "MD5 Validation Error";
    private static final String ERROR_REMARKS_VARIFY_SHA256_OR_SIG_NOT_FOUND = "SHA256 or signature not found";
    private static final String ERROR_REMARKS_VARIFY_SHA256_FAILED = "SHA256 Validation Error";
    private static final String ERROR_REMARKS_VARIFY_SHA256_SIGNATURE_FAILED = "SHA256 signature Validation Error";
    private static final String ERROR_UNZIP_FAILED = "Unzip file failed";
    private static final String DOWNLOAD_SUCCESS = "Success";
    private static final String FILE_DOWNLOAD_SUCCESS = "Params downloaded";
    private static final String FILE_NO_UPDATE = "No update";
    private static final String FILE_NO_PARAMETERS = "No Parameters";
    private static final String SAVEPATH_CANNOT_BE_NULL = "Save path can not be empty";
    private static final String ERROR_NO_PARAMS = "No params to download";
    private static final String ERROR_NO_LAST_SUCCESS_PARAMS = "No last successful parameter download task is found";

    /**
     * The constant downloadParamUrl.
     */
    protected static String downloadParamUrl = "/v1/3rdApps/emm/param";

    protected static String url_policyid_path = "policyId";
    /**
     * The constant updateStatusUrl.
     */
    protected static String updateStatusUrl = "v1/3rdApps/actions/{actionId}/status";
    /**
     * The constant updateStatusBatchUrl.
     */
    protected static String updateStatusBatchUrl = "v1/3rdApps/actions";
    /**
     * Get last success param url
     */
    protected static String lastSuccessParamUrl = "v1/3rdApps/param/last/success";
    /**
     *  sync local param list
     */
    protected static String syncLocalParamUrl = "v1/3rdApps/sync/installed/param";
    private final Logger logger = LoggerFactory.getLogger(ParamApi.class);

    private long lastGetTime = -1;
    private Context context;
    private long marketId;
    private String policyId;

    public ParamApi(Context context, String baseUrl, String appKey, String appSecret, String terminalSN, long marketId, String policyId) {
        super(baseUrl, appKey, appSecret, terminalSN);
        this.context = context;
        this.marketId = marketId;
        this.policyId = policyId;
    }

    public String calculateSHA256(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                return  SHA256Utils.sha256Hex(inputStream);
            } catch (IOException e) {
                logger.error("calculateSHA256 error:" + e);
            }
        }
        return null;
    }

    public void setBaseUrl(String baseUrl) {
        super.getDefaultClient().setBaseUrl(baseUrl);
    }


    /**
     * Get terminal params to download
     *
     * @param packageName the packageName
     * @return the paramList
     */
    public PolicyParamObject getParamDownloadTask(String packageName, String paramSha256, String policyId) {
        SdkRequest request = new SdkRequest(downloadParamUrl);
        request.addHeader(CommonConstants.REQ_PARAM_SN, getTerminalSN());
        request.addHeader(CommonConstants.REQ_PARAM_MARKET_ID, Long.toString(marketId));
        request.addRequestParam(url_policyid_path, policyId);
        request.addRequestParam(REQ_PARAM_PACKAGE_NAME, packageName);
        if (paramSha256 != null) {
            request.addHeader(REQ_PARAM_SHA256, paramSha256);
        }
        return JsonUtils.fromJson(call(request), PolicyParamObject.class);
    }


    private String cookieHeader(String signature, String expires, String keyPairId) {
        StringBuilder cookieHeader = new StringBuilder();
        if (expires == null && keyPairId == null) {
            cookieHeader.append("CloudFront-Signature").append('=').append(signature);
        } else {
            cookieHeader.append("CloudFront-Signature").append('=').append(signature);
            cookieHeader.append("; ");
            cookieHeader.append("CloudFront-Expires").append('=').append(expires);
            cookieHeader.append("; ");
            cookieHeader.append("CloudFront-Key-Pair-Id").append('=').append(keyPairId);
        }
        return cookieHeader.toString();
    }

    /**
     * Download param files
     *
     * @param paramObject  You can get ParamObject from getParamDownloadList();
     * @param saveFilePath Path that param files will be saved.
     * @return the download result
     */
    public SdkObject downloadParamFileOnly(PolicyParamObject paramObject, String saveFilePath) {

        // 0. prepare request
        SdkRequest request = new SdkRequest(paramObject.getDownloadUrl());
        request.setSaveFilePath(saveFilePath);
        if (paramObject.getCookieSignature() != null) { // there exist cookies
            String cookies = cookieHeader(paramObject.getCookieSignature(), paramObject.getCookieExpires(), paramObject.getCookieKeyPairId());
            request.addHeader("Cookie", cookies);
        }

        // 1. download
        String execute = download(request);
        SdkObject downloadResult = JsonUtils.fromJson(execute, SdkObject.class);
        String parPath = downloadResult.getMessage();
        if (downloadResult.getBusinessCode() != ResultCode.SUCCESS.getCode()) {
            return downloadResult;
        }

        // 2. verify
        SdkObject verifyResult = verifySHA256OrM(paramObject, parPath);
        if (verifyResult != null) {
            return verifyResult;
        }

        // 3. unzip and replace param variables
        SdkObject unzipFileAndDeletePar = unzipFileAndDeletePar(paramObject, saveFilePath, parPath);
        if (unzipFileAndDeletePar.getBusinessCode() != 0) {
            return unzipFileAndDeletePar;
        }

        // all success
        return downloadResult;
    }

    private SdkObject verifySHA256OrM(PolicyParamObject paramObject, String parPath) {
        //compare sha256. if sha256 is null, fail
        SdkObject verifyShaResult = verifySha256(paramObject, parPath);
        if (verifyShaResult.getBusinessCode() != 0) {
            return verifyShaResult;
        }
        return null;
    }

    /**
     * verify the sha256 of the file
     *
     * @param paramObject  You can get ParamObject from getParamDownloadList();
     * @param parPath Path that param files will be saved.
     * @return SdkObject    the final result
     */
    private SdkObject verifySha256(PolicyParamObject paramObject, String parPath) {
        SdkObject sdkObject = new SdkObject();
        if (paramObject.getSha256() == null || paramObject.getSignature() == null) {
            logger.warn("sha256 or signature not found in task");
            sdkObject.setBusinessCode(ResultCode.SDK_SHA256_OR_SIGNATURE_NOT_FOUND.getCode());
            sdkObject.setMessage(ERROR_REMARKS_VARIFY_SHA256_OR_SIG_NOT_FOUND);
            return sdkObject;
        }

        String localCalSHA256 = calculateSHA256(parPath);
        if (!paramObject.getSha256().equals(localCalSHA256)) {
            logger.warn("sha256 verify failed");
            sdkObject.setBusinessCode(ResultCode.SDK_SHA256_FAILED.getCode());
            sdkObject.setMessage(ERROR_REMARKS_VARIFY_SHA256_FAILED);
            return sdkObject;
        }

        String localSignature = HMACSignatureGenerator.generateHmacSha256(localCalSHA256, getAppSecret());
        if (!paramObject.getSignature().equals(localSignature)) {
            logger.warn("signature verify failed");
            sdkObject.setBusinessCode(ResultCode.SDK_SHA256_SIGNATURE_FAILED.getCode());
            sdkObject.setMessage(ERROR_REMARKS_VARIFY_SHA256_SIGNATURE_FAILED);
            return sdkObject;
        }

        // pass all verify, then success
        sdkObject.setBusinessCode(ResultCode.SUCCESS.getCode());
        return sdkObject;
    }



    /**
     * unzip file to folder and delete par
     *
     * @param paramObject  You can get ParamObject from getParamDownloadList();
     * @param saveFilePath Path that param files will be saved.
     * @param parPath    the par path
     * @return SdkObject the result
     */
    private SdkObject unzipFileAndDeletePar(PolicyParamObject paramObject, String saveFilePath, String parPath) {
        //Unzip zipfile and delete it
        SdkObject sdkObject = new SdkObject();
        boolean unzipResult = ZipUtil.unzip(parPath);
        boolean deleteResult = FileUtils.deleteFile(parPath);
        if (!unzipResult || !deleteResult) {
            sdkObject.setBusinessCode(ResultCode.SDK_UNZIP_FAILED.getCode());
            sdkObject.setMessage(ERROR_UNZIP_FAILED);
            return sdkObject;
        }

        //replace file
        if (!ReplaceUtils.isHashMapJson(paramObject.getParamVariables())) {
            sdkObject.setBusinessCode(ResultCode.SDK_REPLACE_VARIABLES_FAILED.getCode());
            sdkObject.setMessage(ERROR_REMARKS_NOT_GOOD_JSON + paramObject.getParamVariables());
            return sdkObject;
        }
        boolean ifReplaceSuccess = ReplaceUtils.replaceParams(saveFilePath, paramObject.getParamVariables());
        if (!ifReplaceSuccess) {
            logger.warn("replace paramVariables failed");
            sdkObject.setBusinessCode(ResultCode.SDK_REPLACE_VARIABLES_FAILED.getCode());
            sdkObject.setMessage(ERROR_REMARKS_REPLACE_VARIABLES);
            return sdkObject;
        }

        // unzip, delete and replace success
        sdkObject.setBusinessCode(ResultCode.SUCCESS.getCode());
        return sdkObject;
    }


    /**
     * update push task status
     *
     * @param actionId  Id of push task.
     * @param remarks   the remarks
     * @param status    result of push task：{ pending:1, success:2, fail:3 }
     * @param errorCode error code { None error code:0 }
     * @return the update result
     */
    public SdkObject updateDownloadStatus(String actionId, int status, int errorCode, String remarks) {
        String requestUrl = updateStatusUrl.replace("{actionId}", actionId);
        SdkRequest request = new SdkRequest(requestUrl);
        request.setRequestBody("");
        request.setRequestMethod(SdkRequest.RequestMethod.PUT);
        request.addHeader(Constants.REQ_HEADER_SN, getTerminalSN());
        request.addRequestParam(REQ_PARAM_STATUS, Integer.toString(status));
        request.addRequestParam(REQ_PARAM_ERROR_CODE, Integer.toString(errorCode));
        request.addRequestParam(REQ_PARAM_REMARKS, remarks);
        return JsonUtils.fromJson(call(request), SdkObject.class);
    }

    /**
     * Update push task result in a batch.
     *
     * @param updateActionObjectList the update action list
     * @return the update result
     */
    public SdkObject updateDownloadStatusBatch(List<UpdateActionObject> updateActionObjectList) {
        String requestBody = JsonUtils.toJson(updateActionObjectList);
        SdkRequest request = new SdkRequest(updateStatusBatchUrl);
        request.setRequestMethod(SdkRequest.RequestMethod.POST);
        request.addHeader(Constants.CONTENT_TYPE, Constants.CONTENT_TYPE_JSON);
        request.addHeader(Constants.REQ_HEADER_SN, getTerminalSN());
        request.setRequestBody(requestBody);
        return JsonUtils.fromJson(call(request), SdkObject.class);
    }

    public SdkObject downloadParamsWithoutCache(String packageName, String saveFilePath) {
        return downloadParams(packageName, saveFilePath, false);
    }

    /**
     * download params and if this api will check params already downloaded as the server configured,
     * but if saveFilePath changed, params will be downloaded again.
     *
     * @param packageName
     * @param saveFilePath
     * @return
     */
    public SdkObject downloadParams(String packageName, String saveFilePath) {
        return downloadParams(packageName, saveFilePath, true);
    }

        /**
         * Download param files to specific folder
         *
         * @param packageName        the packageName
         * @param saveFilePath       the saveFilePath
         * @return the result
         */
    private SdkObject downloadParams(String packageName, String saveFilePath, boolean useCache) {

        String lastSaveFolder = PreferencesUtils.getString(context, CommonConstants.SP_PARAM_FOLDER);
        String paramSha256 =  Objects.equals(saveFilePath, lastSaveFolder)? PreferencesUtils.getString(context, CommonConstants.SP_PARAM_SHA256) : null;
        if (!useCache) {
            paramSha256 = null;
        }

        logger.debug("downloadParamToPath: start");
        SdkObject result = new SdkObject();
        if (saveFilePath == null || "".equals(saveFilePath.trim())) {
            result.setBusinessCode(ResultCode.SDK_FILE_NOT_FOUND.getCode());
            result.setMessage(JsonUtils.getSdkJson(ResultCode.SDK_FILE_NOT_FOUND.getCode(), SAVEPATH_CANNOT_BE_NULL));
            return result;
        }
        //get paramList
        Log.e("ttt", "policyId> " + policyId);
        PolicyParamObject policyParamObject = getParamDownloadTask(packageName, paramSha256, policyId);
        Log.e("ttt", "getSha256> " + policyParamObject.getSha256());
        if (policyParamObject.getBusinessCode() != 0) {
            SdkObject sdkObject = new SdkObject();
            sdkObject.setBusinessCode(policyParamObject.getBusinessCode());
            sdkObject.setMessage(policyParamObject.getMessage());
            return sdkObject;
        }
        if(paramSha256 != null && Objects.equals(paramSha256, policyParamObject.getSha256())) {
            SdkObject sdkObject = new SdkObject();
            sdkObject.setBusinessCode(policyParamObject.getBusinessCode());
            sdkObject.setMessage(FILE_NO_UPDATE);
            return sdkObject;
        }
        if (policyParamObject.getSha256() == null ) {
            SdkObject sdkObject = new SdkObject();
            sdkObject.setBusinessCode(-10);
            sdkObject.setMessage(FILE_NO_PARAMETERS);
            return sdkObject;
        }


        saveFilePath = saveFilePath + File.separator + "temp"; // use first actionId as temp folder name
        String remarks = null;


        SdkObject sdkObject = downloadParamFileOnly(policyParamObject, saveFilePath);
        if (sdkObject.getBusinessCode() != ResultCode.SUCCESS.getCode()) {
            result.setBusinessCode(sdkObject.getBusinessCode());
            result.setMessage(sdkObject.getMessage());
            remarks = sdkObject.getMessage();
            logger.error("download error remarks: " + remarks);
        }

        if (remarks != null) {
            Log.e("ttt", "remarks != null NO SHA256");
            // Since download failed, result of updating action is not concerned, just return the result of download failed reason
            FileUtils.delFolder(saveFilePath);
        } else {
            Log.e("ttt", "SAVE SHA256");
            PreferencesUtils.putString(context, CommonConstants.SP_PARAM_SHA256, policyParamObject.getSha256());
            PreferencesUtils.putString(context, CommonConstants.SP_PARAM_FOLDER, saveFilePath.substring(0, saveFilePath.lastIndexOf("/temp")));
            // 当下载成功之后， 就把文件解压到上级目录
            FileUtils.moveToFatherFolder(saveFilePath);
            // 这里就是把所有的任务不要立即更新为成功， 而是等待更新
            // 这里更新为参数下载成功， 等待apply
            result.setBusinessCode(ResultCode.SUCCESS.getCode());
            result.setMessage(FILE_DOWNLOAD_SUCCESS);
        }
        logger.debug("downloadParamToPath: end");
        return result;
    }


    /**
     * @param file the file
     * @return the list
     * @throws JsonParseException the exception
     */
    public LinkedHashMap<String, String> parseDownloadParamJsonWithOrder(File file) throws JsonParseException {
        if (file != null) {
            String fileString = null;
            try {
                fileString = FileUtils.readFileToString(file);

                if (fileString != null) {
                    Gson gson = new Gson();
                    java.lang.reflect.Type type = new TypeToken<LinkedHashMap<String, String>>() {
                    }.getType();
                    return gson.fromJson(fileString, type);
                }
            } catch (Exception e) {
                logger.error("Read file error" + e);
                throw new JsonParseException(e.getMessage());
            }
        }
        return null;
    }

    public HashMap<String, String> parseDownloadParamXml(File xmlFile) throws ParseXMLException {
        HashMap<String, String> xmlData = new HashMap<>();
        if (!xmlFile.exists() || !xmlFile.isFile()) {
            System.out.println("parseDownloadParamXml error, File not exists or not a valid xml");
            return xmlData;
        }
        InputStreamReader isr = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            isr = new InputStreamReader(new FileInputStream(xmlFile), StandardCharsets.UTF_8);
            Document doc = dBuilder.parse(new InputSource(isr));

            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    xmlData.put(element.getNodeName(), element.getTextContent());
                }
            }
        } catch (Exception e) {
            throw new ParseXMLException(e);
        } finally {
            if (isr != null) {
                try {
                    isr.close(); // 确保关闭
                } catch (IOException e) {
                    logger.error("close inputStream failed：" + e);
                }
            }
        }
        return xmlData;
    }

    public LinkedHashMap<String, String> parseDownloadParamXmlWithOrder(File xmlFile) throws ParseXMLException{
        LinkedHashMap<String, String> xmlData = new LinkedHashMap<>();
        if (!xmlFile.exists() || !xmlFile.isFile()) {
            System.out.println("parseDownloadParamXmlWithOrder error, File not exists or not a valid xml");
            return xmlData;
        }
        InputStreamReader isr = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            isr = new InputStreamReader(new FileInputStream(xmlFile), StandardCharsets.UTF_8);
            Document doc = dBuilder.parse(new InputSource(isr));

            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    xmlData.put(element.getNodeName(), element.getTextContent());
                }
            }
        } catch (Exception e) {
            throw new ParseXMLException(e);
        } finally {
            if (isr != null) {
                try {
                    isr.close(); // 显式关闭
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return xmlData;
    }

}
