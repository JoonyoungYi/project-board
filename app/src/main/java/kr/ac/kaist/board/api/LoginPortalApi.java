package kr.ac.kaist.board.api;

import android.app.Application;
import android.util.Log;

import org.apache.http.cookie.Cookie;

import java.util.List;

import kr.ac.kaist.board.model.Board;
import kr.ac.kaist.board.model.Wall;
import kr.ac.kaist.board.utils.Argument;
import kr.ac.kaist.board.utils.RequestManager;


public class LoginPortalApi extends ApiBase {
    private final static String TAG = "LoginPortalApi";

    private List<Cookie> cookies = null;
    private String portal_username = "";
    private String portal_password = "";

    /**
     * @param application
     */
    public LoginPortalApi(Application application, String portal_username, String portal_password) {
        this.application = application;

        if (portal_password.equals("") || portal_username.equals("")) {
            portal_username = getStringInPrefs(application, Argument.PREFS_PORTAL_USERNAME, "");
            portal_password = getStringInPrefs(application, Argument.PREFS_PORTAL_PASSWORD, "");
        }

        this.portal_username = portal_username;
        this.portal_password = portal_password;

        if (portal_password.equals("") || portal_username.equals("")) {
            Log.e(TAG, "ERROR! No password username");
            return;
        }

        //
        RequestManager rm = new RequestManager("/accounts/process", "POST", "http");
        rm.addBodyValue("username", portal_username);
        rm.addBodyValue("password", portal_password);
        rm.doRequest();

        //
        response = rm.getResponse_body();
        cookies = rm.getResponse_cookie();
        Log.d(TAG, response);

    }


    /**
     * @return
     */
    public int getRequestCode() {
        if (cookies == null) {
            Log.e(TAG, "ERROR! No password username");
            return Argument.REQUEST_CODE_FAIL;
        }

        String ObSSOCookie = null;
        String evSSOCookie = null;
        String EnviewSessionID = null;
        String JSESSIONID = null;

        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("ObSSOCookie")) {
                ObSSOCookie = cookie.getValue();
                Log.d(TAG, cookie.getName() + "=" + cookie.getValue() + ";");

            } else if (cookie.getName().equals("evSSOCookie")) {
                evSSOCookie = cookie.getValue();
                Log.d(TAG, cookie.getName() + "=" + cookie.getValue() + ";");

            } else if (cookie.getName().equals("EnviewSessionID")) {
                EnviewSessionID = cookie.getValue();
                Log.d(TAG, cookie.getName() + "=" + cookie.getValue() + ";");

            } else if (cookie.getName().equals("JSESSIONID")) {
                JSESSIONID = cookie.getValue();
                Log.d(TAG, cookie.getName() + "=" + cookie.getValue() + ";");
            }

        }

        if (ObSSOCookie != null && evSSOCookie != null && EnviewSessionID != null && JSESSIONID != null) {
            String cookie = "ObSSOCookie=" + ObSSOCookie + "; evSSOCookie=" + evSSOCookie + "; EnviewSessionID=" + EnviewSessionID + "; JSESSIONID=" + JSESSIONID + ";";
            setString2Prefs(application, Argument.PREFS_PORTAL_COOKIE, cookie);

            setString2Prefs(application, Argument.PREFS_PORTAL_USERNAME, portal_username);
            setString2Prefs(application, Argument.PREFS_PORTAL_PASSWORD, portal_password);
            setString2Prefs(application, Argument.PREFS_PORTAL_LOGIN, "true");

            return Argument.REQUEST_CODE_SUCCESS;

        } else {
            setString2Prefs(application, Argument.PREFS_PORTAL_LOGIN, "false");

            return Argument.REQUEST_CODE_FAIL;
        }
    }

    /**
     * 결과를 반환합니다.
     */

    public void getResult() {


    }

}
