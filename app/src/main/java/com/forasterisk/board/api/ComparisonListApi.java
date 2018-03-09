package com.forasterisk.board.api;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import com.forasterisk.board.model.Comparison;
import com.forasterisk.board.model.Food;
import com.forasterisk.board.model.Store;

/**
 * Created by yearnning on 15. 7. 30..
 */
public class ComparisonListApi extends ApiBase {

    private static final String API_NAME = "ComparisonListApi";

    public ComparisonListApi(Application application, HashMap<String, String> params) {
        super(application, params);
        Log.d(API_NAME, "api_started!");

        /**
         *
         */
        ComparisonApi comparisonApi =
                new ComparisonApi(application, ComparisonApi.createParams());
        int comparison_id = comparisonApi.getResult();
        Log.d(API_NAME, "comparison_id -> " + comparison_id);

        /**
         *
         */
        ComparisonThresholdApi comparisonThresholdApi =
                new ComparisonThresholdApi(application, ComparisonThresholdApi.createParams());
        int threshold = comparisonThresholdApi.getResult();
        Log.d(API_NAME, "threshold -> " + threshold);

        /**
         *
         */
        ComparisonSelectionApi comparisonSelectionApi =
                new ComparisonSelectionApi(application, ComparisonSelectionApi.createParams(comparison_id));
        int selection_id = comparisonSelectionApi.getResult();
        Log.d(API_NAME, "selection_id -> " + selection_id);

        /**
         *
         */
        ComparisonSelectionDetailApi comparisonSelectionDetailApi =
                new ComparisonSelectionDetailApi(application,
                        ComparisonSelectionDetailApi.createParams(comparison_id, selection_id));
        comparisonSelectionDetailApi.getResult();


    }

    public ArrayList<Comparison> getResult() {

        /**
         *
         */
        ArrayList<Comparison> comparisons = new ArrayList<>();

        /**
         *
         */
        comparisons.add(Comparison.newInstance(
                Food.newInstance("본점 보쌈",
                        "http://file.smartbaedal.com/usr/menuitm/2013/3/26/m000893_286.jpg",
                        Store.newInstance("본점 장충 왕족발보쌈")),
                Food.newInstance("묵은지갈비찜",
                        "http://file.smartbaedal.com/usr/menuitm/2015/3/11/527061_286_20150311092758.jpg",
                        Store.newInstance("힘내리 갈비찜안동찜닭")),
                "\"지금 더 먹고 싶은 음식을 선택해주세요.\""
        ));

        comparisons.add(Comparison.newInstance(
                Food.newInstance("굴짬뽕",
                        "http://file.smartbaedal.com/usr/menuitm/2013/3/26/m000706_286.jpg",
                        Store.newInstance("금룡 중화요리")),
                Food.newInstance("볶은 닭갈비",
                        "http://file.smartbaedal.com/usr/menuitm/2015/3/26/504804_286_20150326102421.jpg",
                        Store.newInstance("홍씨네닭갈비")),
                "\"한번 더, 지금 더 먹고 싶은 음식을 선택해주세요.\""
        ));

        comparisons.add(Comparison.newInstance(
                Food.newInstance("눈꽃치즈치킨",
                        "http://file.smartbaedal.com/usr/menuitm/2014/11/3/2011100213_286_20141103154027.png",
                        Store.newInstance("멕시카나치킨 만년점")),
                Food.newInstance("몽뻬르 피자",
                        "http://file.smartbaedal.com/usr/menuitm/2013/5/9/m0004632011100290_286_20130509113852.jpg",
                        Store.newInstance("피자마루 월평점")),
                "\"마지막으로, 지금 더 먹고 싶은 음식을 선택해주세요.\""
        ));


        return comparisons;
    }
}
