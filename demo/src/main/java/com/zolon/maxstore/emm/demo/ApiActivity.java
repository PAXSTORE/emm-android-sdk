package com.zolon.maxstore.emm.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zolon.maxstore.emm.sdk.EMMSDK;
import com.zolon.maxstore.emm.sdk.api.dto.IdentifierObject;
import com.zolon.maxstore.emm.sdk.api.dto.ParamListObject;
import com.zolon.maxstore.emm.sdk.java.base.dto.SdkObject;
import com.zolon.maxstore.emm.sdk.java.base.exception.NotInitException;
import com.zolon.maxstore.emm.sdk.java.base.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class ApiActivity extends Activity {

    private static final String TAG = ApiActivity.class.getSimpleName();
    TextView txv;
    Button btnGetVariables, btnGetSn, btnDownloadParams;

    private static Handler handler = new Handler();

    public static Handler getHandler() {
        return handler;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txv = findViewById(R.id.txv);

        btnGetVariables = findViewById(R.id.btn1);

        btnGetVariables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ParamListObject paramVariables = EMMSDK.getInstance().getParamVariableApi().getParamVariables();
                            Log.d(TAG, "paramVariableStr: " + paramVariables);
                            showApiResultInTv("paramVariableStr: " + paramVariables);
                        } catch (NotInitException e) {
                            Log.e(TAG, "not Init");
                            showApiResultInTv("Init failed");
                        }
                    }
                });
                thread.start();
            }
        });


        btnGetSn = findViewById(R.id.btn2);
        btnGetSn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            IdentifierObject identifier = EMMSDK.getInstance().getParamVariableApi().getIdentifier();
                            Log.d(TAG, "identifier: " + identifier);
                            showApiResultInTv("identifier: " + identifier);
                        } catch (NotInitException e) {
                            Log.e(TAG, "not Init");
                            showApiResultInTv("Init failed");
                        }
                    }
                });
                thread.start();
            }
        });

        btnDownloadParams = findViewById(R.id.btn_get_param);
        btnDownloadParams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String saveFilePath = new File(getApplication().getExternalFilesDir(null), "YourPath").getAbsolutePath();
                            Log.e("ttt", saveFilePath);
                            SdkObject downloadResult = EMMSDK.getInstance().getParamApi().downloadParams(getPackageName(), saveFilePath);
                            String fileListStr = FolderUitls.listAllNames(saveFilePath);
                            String result = "download path:" +  saveFilePath + " \n\n Download param result: "  +downloadResult  + " \n\n fileList: \n" + fileListStr + " \n\n\n" +
                                    "  sys_cap.p  ：";
                            if (new File(saveFilePath, "sys_cap.p").exists() && downloadResult.getBusinessCode() == 0) {
                                String fileResult = FileUtils.readFileToString(new File(saveFilePath, "sys_cap.p"));
                                result = result + fileResult;
                            }

                            Log.d(TAG, "downloadResult: " + result);
                            showApiResultInTv("downloadResult: " + result);


                        } catch (NotInitException e) {
                            Log.e(TAG, "not Init");
                            showApiResultInTv("Init failed");
                        } catch (IOException e) {
                            showApiResultInTv("Read sys_cap.p failed");
                        }
                    }
                });
                thread.start();
            }
        });
    }

    private void showApiResultInTv(String result) {
        ApiActivity.getHandler().post(new Runnable() {
            @Override
            public void run() {
                txv.setText(result);
            }
        });
    }


}
