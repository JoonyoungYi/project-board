package com.forasterisk.board.api;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import com.forasterisk.board.model.Food;
import com.forasterisk.board.model.Store;

/**
 * Created by yearnning on 15. 7. 30..
 */
public class ComparisonRecommendationsApi extends ApiBase {

    private static final String API_NAME = "Com.Recom.Api";

    /**
     *
     */

    private static final String PARAM_COMPARISON_ID = "comparison_id";

    /**
     * @param application
     * @param params
     */
    public ComparisonRecommendationsApi(Application application, HashMap<String, String> params) {
        super(application, params);
        Log.d(API_NAME, "api_started!");

        /**
         * Handle Response
         */
        String response = getResponseFromRemote();
        if (response == null) {
            return;
        }

        Log.d(API_NAME, response);
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
            return super.getResponseFromRemote("/comparison/" + comparison_id + "/recommendations", "GET", "httpa");
        } else {
            return super.getResponseFromRemote("/comparison/recommendations", "GET", "httpa");
        }
    }


    public ArrayList<Food> getResult() {

        ArrayList<Food> foods = new ArrayList<>();

        foods.add(Food.newInstance("부대전골",
                "http://file.smartbaedal.com/usr/menuitm/2013/3/26/m000835_286.jpg",
                17000,
                Store.newInstance("별밤야식", "050-6381-9607")));

        /*
        foods.add(Food.newInstance("치즈돈까스",
                "http://file.smartbaedal.com/usr/menuitm/2012/07/m000135_a01_286.jpg",
                Store.newInstance("명동돈까스 양푼이비빔밥")));
        */

        return foods;
    }

    public static HashMap<String, String> createParams(int comparison_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_COMPARISON_ID, "" + comparison_id);
        return params;
    }
}
