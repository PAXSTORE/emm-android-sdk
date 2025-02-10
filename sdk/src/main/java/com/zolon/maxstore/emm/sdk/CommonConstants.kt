package com.zolon.maxstore.emm.sdk

object CommonConstants {
    const val ERR_MSG_BIND_PAXSTORE_SERVICE_FAILED =
        "Bind service failed, STORE client may not running or STORE client version is below 6.1. Please check"
    const val ERR_MSG_BIND_PAXSTORE_SERVICE_TOO_FAST =
        "Bind service failed, get terminal infomation too fast"
    const val ERR_MSG_NULL_RETURNED =
        "Null value returned, STORE client may not activated or running. Please check"
    const val ERR_MSG_PAXSTORE_MAY_NOT_INSTALLED =
        "Bind service failed, STORE client may not installed"
    const val SP_LAST_GET_TERMINAL_INFO_TIME = "sp_last_get_terminal_time"
    const val SP_LAST_GET_ONLINE_STATUS_TIME = "sp_last_get_online_time"
    const val SP_LAST_GET_LOCATION_TIME = "sp_last_get_location_time"
    const val SP_LAST_GET_MERCHANT_TIME = "sp_last_get_merchant_time"
    const val SP_LAST_GET_DCURL_TIME = "sp_last_get_dcurl_time"
    const val SP_SMALL_LOGO_ICON = "sp_small_logo_icon"
    const val ONE_HOUR_INTERVAL = 3600000L
    const val ACTION_START_CUSTOMER_SERVICE = "com.sdk.service.ACTION_TO_DOWNLOAD_PARAMS"
}