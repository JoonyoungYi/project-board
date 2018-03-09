package com.forasterisk.board.api;

import android.app.Application;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by yearnning on 15. 7. 31..
 */
public class ComparisonSelectionApi extends ApiBase {

    private static final String API_NAME = "ComparisonSelectionApi";

    /**
     *
     */

    private static final String PARAM_COMPARISON_ID = "comparison_id";
    /**
     *
     */
    int selection_id = -1;

    /**
     * @param application
     * @param params
     */
    public ComparisonSelectionApi(Application application, HashMap<String, String> params) {
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
        selection_id = getThresholdFromResponse(response);
    }

    private int getThresholdFromResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int selection_id = jsonObject.getJSONObject("payload").getInt("id");
            Log.d(API_NAME, "selection_id -> " + selection_id);
            return selection_id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getResult() {
        return selection_id;
    }

    /**
     * @return
     */
    protected String getResponseFromRemote() {

        /**
         *
         */
        String comparison_id_str = mParams.get(PARAM_COMPARISON_ID);
        int comparison_id = Integer.getInteger(comparison_id_str, 0);

        /**
         *
         */
        if (comparison_id > 0) {
            return super.getResponseFromRemote("/comparison/" + comparison_id + "/selection", "POST", "httpa");
        }
        return null;
    }


    public static HashMap<String, String> createParams(int comparison_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_COMPARISON_ID, "" + comparison_id);
        return params;
    }
}
