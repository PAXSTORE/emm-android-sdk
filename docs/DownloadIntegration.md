# EMM Downloading Parameter Integration

By integrating with this function, applications can download parameters.

### 1: Below two steps can save you a lot of time( Do not skip this!!!)
1. Check the appKey and appSecret that are the same with the web, and check again, and again, three times should be fine.
2. We recommend saving the parameter files to a temporary directory first. Then, copy them to a different directory before use.

Now you can go through below contents.

### 2：Initialization of Sdk
Refer to the [SetUp](../README.md)

### 3：Download Parameters API
It is important that you do not download 

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

    public SdkObject downloadParamsWithoutCache(String packageName, String saveFilePath) {
        return downloadParams(packageName, saveFilePath, false);
    }





推荐是用downloadParams， 该接口会检查上一次下载的参数，变量和后台当前参数，变量是否一致， 如果一致则不会再次下载， 
返回code=0, message="no update";

当你需要强制去后台获取参数文件的时候， 可以调用downloadParamsWithoutCache， 这将不比较上一次的参数下载， 
直接去后台获取最新的参数文件。

建议应用在第一次打开的时候进行参数下载， 之后每天可以请求一次是否有参数更新， 建议只在必要的时候去后台检查是否有新的参数需要下载， 否则可能造成大量的流量浪费。 


code 0  message :  Params downloaded , >> 代表下载最新的参数成功
code 0  message ： Params No Update , >> 后台当前参数与上一次下载的参数相同， 无需更新
code 0  message ： Parameters not available , >> 后台关闭参数配置， 无可用参数下载。


请不要同时启动两个或以上的线程去同时做参数下载的操作， 否则可能会导致参数文件损坏，使应用无法正确使用参数文件。 


## Template
See [Template](../demo/src/main/assets/param_template.xml)



