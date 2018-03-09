package com.forasterisk.board.api;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by yearnning on 15. 7. 31..
 */
public class ComparisonApi extends ApiBase {

    private static final String API_NAME = "ComparisonApi";


    /**
     *
     */
    int comparison_id = -1;

    /**
     * @param application
     * @param params
     */
    public ComparisonApi(Application application, HashMap<String, String> params) {
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
        comparison_id = getComparisonIdFromResponse(response);
    }

    private int getComparisonIdFromResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int comparison_id = jsonObject.getJSONObject("payload").getInt("id");
            Log.d(API_NAME, "comparison_id -> " + comparison_id);
            return comparison_id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getResult() {
        return comparison_id;
    }

    /**
     * @return
     */
    protected String getResponseFromRemote() {
        return super.getResponseFromRemote("/comparison", "POST", "httpa");
    }


    public static HashMap<String, String> createParams() {
        HashMap<String, String> params = new HashMap<>();
        return params;
    }
}
