package com.forasterisk.board.api;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

import com.forasterisk.board.utils.Argument;
import com.forasterisk.board.utils.PreferenceManager;

/**
 * Created by yearnning on 15. 7. 31..
 */
public class UserSessionApi extends ApiBase {

    private static final String API_NAME = "AuthLoginFacebookApi";

    /**
     *
     */
    private static final String PARAM_TOKEN = "fb_token";

    /**
     * @param application
     * @param params
     */
    public UserSessionApi(Application application, HashMap<String, String> params) {
        super(application, params);
        Log.d(API_NAME, "api_started!");

        /**
         * Handle Response
         */
        String response = getResponseFromRemote();
        if (response == null) {
            return;
        }

        Log.d(API_NAME, "response -> " + response);
        getSessionKeyFromResponse(response);
    }

    private void getSessionKeyFromResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String session_key = jsonObject.getJSONObject("payload").getString("token");
            Log.d(API_NAME, "session_key -> " + session_key);
            PreferenceManager.put(mContext, Argument.PREFS_SESSION_KEY, session_key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getResult() {

    }

    /**
     * @return
     */
    protected String getResponseFromRemote() {
        return super.getResponseFromRemote("/user/session", "POST", "http");
    }


    public static HashMap<String, String> createParams(String token) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_TOKEN, token);
        return params;
    }
}
