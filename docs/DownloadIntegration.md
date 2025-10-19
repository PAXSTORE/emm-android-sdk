# EMM Downloading Parameter Integration

This document describes the APIs for downloading  parameters from the backend. The system is designed to optimize network usage by minimizing unnecessary downloads while ensuring parameter freshness when needed

### 1: Below two steps can save you a lot of time( Do not skip this!!!)
1. Check the appKey and appSecret that are the same with the web, and check again, and again, three times should be fine.
2. We recommend saving the parameter files to a temporary directory first. Then, copy them to a different directory before use.

Now you can go through below contents.

### 2：Initialization of Sdk
Refer to the [SetUp](../README.md)

### 3：Download Parameters APIs
##### API 1:

    public SdkObject downloadParams(String packageName, String saveFilePath) {
        return downloadParams(packageName, saveFilePath, true);
    }

#### Method Behavior
downloadParams- **Intelligent Update Check** 

This interface sends local hashes to the backend for comparison and only downloads parameters when changes are detected.

**Workflow:**

* Client saves and sends local parameter and variable hashes(null for first time) to backend

* Backend compares hashes with current parameter versions

* If hashes differ, backend triggers parameter download

* Client receives and downloads new parameters

* Both parameter content and variable hashes are updated locally after successful download

**Recommended Use:** Daily update checks to conserve bandwidth while ensuring parameter freshness

##### API 2:

    public SdkObject downloadParamsWithoutCache(String packageName, String saveFilePath) {
        return downloadParams(packageName, saveFilePath, false);
    }

**Workflow:**

downloadParamsWithoutCache- **Forced Update**

This interface bypasses the SHA256 comparison process entirely and always downloads the latest parameters from the backend, regardless of local hash values

**Use Case:** Critical situations requiring immediate parameter synchronization, such as after security incidents or user-initiated refresh operations.


### 4：Download Parameters return codes and messages
| Code | Message                  | Description        |
|------|--------------------------| ------------------ |
| 0    | Parameters downloaded    | New parameters successfully downloaded       |
| 0    | Parameters no update     | Backend parameters identical to local version (both SHA256 hashes match)   |
| 0    | Parameters not available | Backend has disabled parameter configuration            |



⚠️Important: Prevent Concurrent Download Operations
To ensure the integrity and correct application of parameter files, it is **critical to avoid initiating two or more concurrent threads** to perform the downloadParams or downloadParamsWithoutCache operation simultaneously

## Template
See [Template](../demo/src/main/assets/param_template.xml)



