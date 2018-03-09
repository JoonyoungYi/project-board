package com.forasterisk.board.api;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by yearnning on 15. 7. 31..
 */
public class ComparisonThresholdApi extends ApiBase {

    private static final String API_NAME = "ComparisonThresholdApi";

    /**
     *
     */
    int threshold = -1;

    /**
     * @param application
     * @param params
     */
    public ComparisonThresholdApi(Application application, HashMap<String, String> params) {
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
        threshold = getThresholdFromResponse(response);
    }

    private int getThresholdFromResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int threshold = jsonObject.getJSONObject("payload").getInt("threshold");
            Log.d(API_NAME, "threshold -> " + threshold);
            return threshold;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getResult() {
        return threshold;
    }

    /**
     * @return
     */
    protected String getResponseFromRemote() {
        return super.getResponseFromRemote("/comparison/threshold", "GET", "httpa");
    }


    public static HashMap<String, String> createParams() {
        HashMap<String, String> params = new HashMap<>();
        return params;
    }
}
