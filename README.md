
# EMM Service Android SDK

EMM Service Android SDK provides simple and easy-to-use service interfaces for third party developers to develop android apps on PAXSTORE 
1. Get parameter variables
2. Get Serial Number

Please take care of your AppKey and AppSecret that generated by PAXSTORE system when you create an app.
<br>Refer to the following steps for integration.

## Requirements
**Android SDK version**
>SDK 29 or higher, depending on the terminal's paydroid version.

## Download

 Add the dependency

```
    implementation 'com.whatspos.sdk:emm-android-sdk:1.0.0'
```


## Permissions
EMM Service Android SDK need the following permissions, please add them in AndroidManifest.xml.

`<uses-permission android:name="android.permission.INTERNET" />`<br>

## Set Up

### Step 1: Get Application Key and Secret
Create a new app in PAXSTORE, and get **AppKey** and **AppSecret** from app detail page in developer center.

### Step 2: Initialization
Configuring the application element, edit AndroidManifest.xml, it will have an application element. You need to configure the android:name attribute to point to your Application class (put the full name with package if the application class package is not the same as manifest root element declared package)

    <application
        android:name=".BaseApplication"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme">

Initializing AppKey,AppSecret and SN
>Please note, make sure you have put your own app's AppKey and AppSecret correctly

    public class BaseApplication extends Application {
    
        private static final String TAG = BaseApplication.class.getSimpleName();
        
        //todo make sure to replace with your own app's appKey and appSecret
        private String appkey = "Your APPKEY";
        private String appSecret = "Your APPSECRET";
        
        @Override
        public void onCreate() {
            super.onCreate();
            initEMMSdk();
        }
        
        private void initEMMSdk() {
           //todo Init AppKey，AppSecret, make sure the appKey and appSecret is corret.
            StoreSdk.getInstance().init(getApplicationContext(), appkey, appSecret, new BaseApiService.Callback() {
                @Override
                public void initSuccess() {
                   //TODO Do your business here
                }
    
                @Override
                public void initFailed(Throwable e) {
                   //TODO Do failed logic here
                    Toast.makeText(getApplicationContext(), "Cannot get API URL from STORE client, Please install STORE client first.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

### Step 3: Usage
Get parameter variables

    new Thread(() -> {
        try {
            // this method need to be executed in IO Thread
            ParamListObject paramVariables = EMMSDK.getInstance().getParamVariableApi().getParamVariables();
            if (paramVariables.getBusinessCode() == ResultCode.SUCCESS.getCode()) {
                Log.d(TAG, "get success. " + JsonUtils.toJson(paramVariables));
            } else {
                Log.d(TAG, "get failed. " + paramVariables);
            }
        } catch (Throwable t) {
            
        }
    }).start();

Get Serial Number

    new Thread(() -> {
        try {
            // this method need to be executed in IO Thread
            IdentifierObject identifier = EMMSDK.getInstance().getParamVariableApi().getIdentifier();
            if (identifier.getBusinessCode() == ResultCode.SUCCESS.getCode()) {
                Log.d(TAG, "get success. " + JsonUtils.toJson(identifier));
            } else {
                Log.d(TAG, "get failed. " + identifier);
            }
        } catch (Throwable t) {
            
        }
    }).start();

ErrorCode

    for all interfaces
    113: Your request is invalid, please try again or contact marketplace administrator
    129: Authentication failed
    1016: App Authentication failed
    1800: Terminal not found
    90011: EMM device is uncertified

    only for getIdentifier()
    131: Insufficient access right

## License

See the [Apache 2.0 license](https://github.com/PAXSTORE/paxstore-3rd-app-android-sdk/blob/master/LICENSE) file for details.

    Copyright © 2019 Shenzhen Zolon Technology Co., Ltd. All Rights Reserved.
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at following link.
    
         http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
