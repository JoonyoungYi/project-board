package kr.ac.kaist.board.api;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.StringTokenizer;

import kr.ac.kaist.board.utils.Argument;

public class ApiBase {
    private final static String TAG = "Api Base";

    /**
     *
     */
    public Application application = null;
    public String response = null;

    /**
     * @param application
     * @param key
     * @param value
     */
    public static void setString2Prefs(Application application, String key, String value) {

        SharedPreferences prefs = application.getSharedPreferences(Argument.PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefs_editor;

        prefs_editor = prefs.edit();
        prefs_editor.putString(key, value);
        prefs_editor.commit();
    }

    /**
     * @param key
     * @param value_init
     * @return
     */
    public static String getStringInPrefs(Context context, String key, String value_init) {
        SharedPreferences prefs = context.getSharedPreferences(Argument.PREFS, Context.MODE_PRIVATE);
        return prefs.getString(key, value_init);
    }

    /**
     * @param application
     * @param key
     * @param value_init
     * @return
     */
    public static String getStringInPrefs(Application application, String key, String value_init) {
        SharedPreferences prefs = application.getSharedPreferences(Argument.PREFS, Context.MODE_PRIVATE);
        return prefs.getString(key, value_init);
    }

    /**
     * @return
     */
    public int getRequestCode() {

        /*
        //TODO: 인터넷 연결 확인부분 만들어야 함.
         */
        if (false) {
            return Argument.REQUEST_CODE_NOT_CONNECTED;
        }

        /*

         */
        JSONObject responseObj = null;
        try {
            responseObj = new JSONObject(response);

        } catch (JSONException e) {
            e.printStackTrace();
            return Argument.REQUEST_CODE_NOT_PARSED;
        }

        return Argument.REQUEST_CODE_SUCCESS;
    }

    /**
     *
     * @param board_id
     * @return
     */
    public String getBaseUrl(int board_id) {
        if (board_id == Argument.ARG_BOARD_PORTAL_BOARD || board_id == Argument.ARG_BOARD_PORTAL_NOTICE) {
            return ("portal.kaist.ac.kr");
        } else if (board_id == Argument.ARG_BOARD_ARA) {
            return ("ara.kaist.ac.kr");
        } else if (board_id == Argument.ARG_BOARD_BF){
            return ("bf.kaist.ac.kr");
        }

        return null;
    }

    /**
     *
     * @param board_id
     * @return
     */
    public String getCookieArgument(int board_id) {
        if (board_id == Argument.ARG_BOARD_PORTAL_BOARD || board_id == Argument.ARG_BOARD_PORTAL_NOTICE) {
            return Argument.PREFS_PORTAL_COOKIE;
        } else if (board_id == Argument.ARG_BOARD_ARA) {
            return Argument.PREFS_ARA_COOKIE;
        } else if (board_id == Argument.ARG_BOARD_BF){
            return Argument.PREFS_BF_COOKIE;
        }

        return null;
    }


    /**
     * @param request_code
     */
    public static void showToastMsg(Application application, int request_code) {

        if (application.getApplicationContext() == null)
            return;

        if (request_code == Argument.REQUEST_CODE_NOT_PARSED) {
            Toast.makeText(application.getApplicationContext(), "인터넷 연결이 불안정합니다. 잠시 후, 다시 시도해 주세요.", Toast.LENGTH_LONG).show();

        } else if (request_code == Argument.REQUEST_CODE_NOT_CONNECTED) {
            Toast.makeText(application.getApplicationContext(), "인터넷 연결이 불안정합니다. 잠시 후, 다시 시도해 주세요.", Toast.LENGTH_LONG).show();

        } else if (request_code == Argument.REQUEST_CODE_CAN_NOT_FIND_RESULT_OBJECT) {
            Toast.makeText(application.getBaseContext(), "서버와의 연결이 불안정합니다. 잠시 후, 다시 시도해 주세요.", Toast.LENGTH_LONG).show();

        } else if (request_code == Argument.REQUEST_CODE_UNEXPECTED) {
            Toast.makeText(application.getBaseContext(), "알 수 없는 오류가 발생했습니다. 잠시 후, 다시 시도해 주세요.", Toast.LENGTH_LONG).show();

        }
    }

    /**
     *
     * @param st
     * @return
     */
    public String getNextTokenTrimmed(StringTokenizer st) {
        String result = null;
        if (st.hasMoreElements()) {
            result = st.nextToken().trim();
        }
        return result;
    }


}
