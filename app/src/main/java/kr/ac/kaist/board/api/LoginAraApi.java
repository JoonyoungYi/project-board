package kr.ac.kaist.board.api;

import android.app.Application;
import android.util.Log;

import org.apache.http.cookie.Cookie;

import java.util.List;

import kr.ac.kaist.board.utils.Argument;
import kr.ac.kaist.board.utils.RequestManager;


public class LoginAraApi extends ApiBase {
    private final static String TAG = "LoginAraApi";

    private List<Cookie> cookies = null;
    private String username = "";
    private String password = "";

    /**
     * @param application
     */
    public LoginAraApi(Application application, String username, String password) {
        this.application = application;

        if (password.equals("") || username.equals("")) {
            username = getStringInPrefs(application, Argument.PREFS_PORTAL_USERNAME, "");
            password = getStringInPrefs(application, Argument.PREFS_PORTAL_PASSWORD, "");
        }

        this.username = username;
        this.password = password;

        if (password.equals("") || username.equals("")) {
            Log.e(TAG, "ERROR! No password username");
            return;
        }

        //
        RequestManager rm = new RequestManager("/account/login/", "POST", "http");
        rm.setBaseUrl("ara.kaist.ac.kr");

        rm.addBodyValue("username", username);
        rm.addBodyValue("password", password);
        rm.doRequest();

        response = rm.getResponse_body();
        cookies = rm.getResponse_cookie();

    }


    /**
     * @return
     */
    public int getRequestCode() {
        if (cookies == null) {
            Log.e(TAG, "ERROR! No password username");
            return Argument.REQUEST_CODE_FAIL;
        }

        String sessionid = null;
        String arara_checksum = null;

        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("sessionid")) {
                sessionid = cookie.getValue();
                Log.d(TAG, cookie.getName() + "=" + cookie.getValue() + ";");

            } else if (cookie.getName().equals("arara_checksum")) {
                arara_checksum = cookie.getValue();
                Log.d(TAG, cookie.getName() + "=" + cookie.getValue() + ";");

            }

        }

        if (sessionid != null && arara_checksum != null) {
            String cookie = "sessionid=" + sessionid + "; arara_checksum=" + arara_checksum + ";";
            setString2Prefs(application, Argument.PREFS_ARA_COOKIE, cookie);

            setString2Prefs(application, Argument.PREFS_ARA_USERNAME, username);
            setString2Prefs(application, Argument.PREFS_ARA_PASSWORD, password);
            setString2Prefs(application, Argument.PREFS_ARA_LOGIN, "true");

            return Argument.REQUEST_CODE_SUCCESS;
        } else {

            setString2Prefs(application, Argument.PREFS_ARA_LOGIN, "false");

            return Argument.REQUEST_CODE_FAIL;
        }

    }

    /**
     * 결과를 반환합니다.
     */

    public void getResult() {


    }

}
