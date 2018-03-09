package com.forasterisk.board.utils;

/**
 * Created by yearnning on 15. 8. 1..
 */
public class Argument {

    /**
     *
     */
    public static final int REQUEST_CODE_RECOVERD_FROM_FAIL_LOGIN = -3;
    public static final int REQUEST_CODE_RECOVERD = -2;
    public static final int REQUEST_CODE_SUCCESS = -1;
    public static final int REQUEST_CODE_UNEXPECTED = 0;
    public static final int REQUEST_CODE_FAIL_NETWORK = 1;
    public static final int REQUEST_CODE_FAIL_SERVER = 2;
    public static final int REQUEST_CODE_FAIL_SERVER_WITH_SERVER_STATUS_OK = 3;
    public static final int REQUEST_CODE_FAIL_SERVER_WITH_SERVER_STATUS_FAIL = 4;
    public static final int REQUEST_CODE_FAIL_SERVER_WITH_SERVER_STATUS_UNKNWON = 5;
    public static final int REQUEST_CODE_FAIL_COMMUNICATION = 6;
    public static final int REQUEST_CODE_FAIL_LOGIN = 7;
    public static final int REQUEST_CODE_FAIL_PARAMS = 8;
    public static final int REQUEST_CODE_FAIL_OLD_VERSION = 9;

    /**
     *
     */
    public static final String PREFS = "user_info";
    public static final String PREFS_USER_FIRST_NAME = "prefs_user_first_name";
    public static final String PREFS_LOG_ON = "prefs_log_on";
    public static final String PREFS_SESSION_KEY = "prefs_session_key";
}
