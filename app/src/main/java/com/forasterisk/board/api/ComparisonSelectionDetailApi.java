package com.forasterisk.board.api;

import android.app.Application;
import android.util.Log;

import java.util.HashMap;

import com.forasterisk.board.model.Comparison;

/**
 * Created by yearnning on 15. 7. 31..
 */
public class ComparisonSelectionDetailApi extends ApiBase {

    private static final String API_NAME = "Com.Sel.DetailApi";

    /**
     *
     */
    private static final String PARAM_COMPARISON_ID = "comparison_id";
    private static final String PARAM_SELECTION_ID = "selection_id";

    /**
     *
     */
    Comparison comparison = null;

    /**
     * @param application
     * @param params
     */
    public ComparisonSelectionDetailApi(Application application, HashMap<String, String> params) {
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
        comparison = getComparisonFromResponse(response);

    }

    private Comparison getComparisonFromResponse(String response) {

        /*
        try {
            JSONObject jsonObject = new JSONObject(response);
            int selection_id = jsonObject.getJSONObject("payload").getInt("id");
            Log.d(API_NAME, "selection_id -> " + selection_id);
            return selection_id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; */
        return null;
    }

    public Comparison getResult() {
        return comparison;
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

        String selection_id_str = mParams.get(PARAM_SELECTION_ID);
        int selection_id = Integer.getInteger(selection_id_str, 0);

        /**
         *
         */
        if (comparison_id > 0 && selection_id > 0) {
            return super.getResponseFromRemote("/comparison/" + comparison_id + "/selection/" + selection_id, "POST", "httpa");
        }
        return null;
    }


    public static HashMap<String, String> createParams(int comparison_id, int selection_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_COMPARISON_ID, "" + comparison_id);
        params.put(PARAM_SELECTION_ID, "" + selection_id);
        return params;
    }
}
