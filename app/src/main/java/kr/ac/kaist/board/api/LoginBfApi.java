package kr.ac.kaist.board.api;

import android.app.Application;
import android.util.Log;

import org.apache.http.cookie.Cookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

import kr.ac.kaist.board.utils.Argument;
import kr.ac.kaist.board.utils.RequestManager;


public class LoginBfApi extends ApiBase {
    private final static String TAG = "LoginBfApi";

    private List<Cookie> cookies = null;
    private String username = "";
    private String password = "";

    /**
     * @param application
     */
    public LoginBfApi(Application application, String username, String password) {
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
        RequestManager rm = new RequestManager("/users/sign_in", "GET", "https");
        rm.setBaseUrl("bamboofo.rest");
        rm.doRequest();

        response = rm.getResponse_body();
        cookies = rm.getResponse_cookie();

        //
        rm = new RequestManager("/users/sign_in", "POST", "http");
        rm.setBaseUrl("bamboofo.rest");

        String cookie_str = "";
        for (Cookie cookie : cookies) {
            Log.d(TAG, cookie.getName() + " : " + cookie.getValue());
            cookie_str += cookie.getName() + "=" + cookie.getValue();
        }
        rm.addHeader("Cookie", cookie_str);

        try {
            Document doc = Jsoup.parse(response);

            Elements inputElements = doc.getElementsByTag("input");
            for (Element element : inputElements) {

                if (element.attr("type").equals("submit")) {
                    continue;
                }

                String key = element.attr("name");
                String value = element.attr("value").trim();

                if (!value.equals("")) {
                    rm.addBodyValue(key, value);
                    //Log.d(TAG, key + " : " + value);

                } else if (key.contains("email")) {
                    rm.addBodyValue(key, this.username);

                } else if (key.contains("password")) {
                    rm.addBodyValue(key, this.password);

                } else {
                    Log.d(TAG, element.outerHtml());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        rm.doRequest();
        response = rm.getResponse_body();
        cookies = rm.getResponse_cookie();
    }


    /**
     * @return
     */
    public int getRequestCode() {
        if (response == null) {
            return Argument.REQUEST_CODE_FAIL;
        }

        /**
         *
         */
        if (!response.contains("로그인")) {

            return Argument.REQUEST_CODE_SUCCESS;

        } else {
            Log.d(TAG, "Bamboo Login Fail");
            return Argument.REQUEST_CODE_FAIL;
        }

    }

    /**
     * 결과를 반환합니다.
     */

    public void getResult() {

        String cookie_str = "";
        for (Cookie cookie : cookies) {
            Log.d(TAG, cookie.getName() + " : " + cookie.getValue());
            cookie_str += ";" + cookie.getName() + "=" + cookie.getValue();
        }
        cookie_str = cookie_str.substring(1);

        setString2Prefs(application, Argument.PREFS_BF_COOKIE, cookie_str);
        setString2Prefs(application, Argument.PREFS_BF_USERNAME, username);
        setString2Prefs(application, Argument.PREFS_BF_PASSWORD, password);
    }

}
