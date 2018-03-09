package kr.ac.kaist.board.utils;

public class Argument {

    /**
     *
     */
    public static final String PREFS = "user_info";

    public static final String PREFS_BF_USERNAME = "bf_username";
    public static final String PREFS_BF_PASSWORD = "bf_password";
    public static final String PREFS_BF_COOKIE = "bf_Cookie";

    public static final String PREFS_ARA_LOGIN = "ara_login";
    public static final String PREFS_ARA_USERNAME = "ara_username";
    public static final String PREFS_ARA_PASSWORD = "ara_password";
    public static final String PREFS_ARA_COOKIE = "ara_Cookie";

    public static final String PREFS_PORTAL_LOGIN = "portal_login";
    public static final String PREFS_PORTAL_USERNAME = "portal_username";
    public static final String PREFS_PORTAL_PASSWORD = "portal_password";
    public static final String PREFS_PORTAL_COOKIE = "portal_Cookie";

    /**
     *
     */
    public static final int REQUEST_CODE_SUCCESS = 0;
    public static final int REQUEST_CODE_FAIL = 1;
    public static final int REQUEST_CODE_NOT_PARSED = 2;
    public static final int REQUEST_CODE_NOT_CONNECTED = 3;
    public static final int REQUEST_CODE_UNEXPECTED = 4;
    public static final int REQUEST_CODE_CAN_NOT_FIND_RESULT_OBJECT = 5;
    public static final int REQUEST_CODE_NOT_LOGIN = 6;
    public static final int REQUEST_CODE_NO_DATA = 7;


    /**
     *
     */
    public static final int ARG_BOARD_PORTAL_NOTICE = 0;
    public static final int ARG_BOARD_PORTAL_BOARD = 1;
    public static final int ARG_BOARD_ARA = 2;
    public static final int ARG_BOARD_BF = 3;


}
