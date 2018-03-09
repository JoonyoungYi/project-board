package com.forasterisk.board.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.forasterisk.board.R;
import com.forasterisk.board.api.ComparisonAddApi;
import com.forasterisk.board.api.ComparisonListApi;
import com.forasterisk.board.api.ComparisonRecommendationsApi;
import com.forasterisk.board.api.UserLocationApi;
import com.forasterisk.board.model.Comparison;
import com.forasterisk.board.model.Food;
import com.forasterisk.board.model.LocationModel;
import com.forasterisk.board.utils.Argument;
import com.forasterisk.board.utils.CardManager;
import com.forasterisk.board.utils.PreferenceManager;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    /**
     *
     */
    private ArrayList<Comparison> mComparisons = new ArrayList<>();

    /**
     *
     */
    private TextView mLocationTv;
    private View mLocationBtn;

    private TextView mMsgTv;

    private View mComparisonCardView;
    private ImageView mFoodAIv;
    private TextView mFoodANameTv;
    private TextView mFoodAStoreNameTv;
    private ImageView mFoodBIv;
    private TextView mFoodBNameTv;
    private TextView mFoodBStoreNameTv;

    private View mRecommendCardView;
    private ImageView mFoodRecommendIv;
    private TextView mFoodRecommendNameTv;
    private TextView mFoodRecommendStoreNameTv;
    private TextView mFoodRecommendPriceTv;
    private View mAnotherRecommendBtn;
    private View mTelBtn;

    /**
     *
     */
    private CardManager mComparisonCardManager;
    private CardManager mRecommendCardManager;

    /**
     *
     */
    private LocationDetailApiReadyTask mLocationDetailApiReadyTask = null;
    private UserLocationApiTask mLocationDetailApiTask = null;
    private ComparisonListApiTask mComparisonApiTask = null;
    private ComparisonAddApiTask mComparisonAddApiTask = null;
    private RecommendListApiTask mRecommendListApiTask = null;

    /**
     * @param activity
     */
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        /**
         *
         */
        mLocationTv = (TextView) findViewById(R.id.location_tv);
        mLocationBtn = findViewById(R.id.location_btn);

        mMsgTv = (TextView) findViewById(R.id.msg_tv);

        mComparisonCardView = findViewById(R.id.comparison_card_view);
        mFoodAIv = (ImageView) findViewById(R.id.food_a_iv);
        mFoodANameTv = (TextView) findViewById(R.id.food_a_name_tv);
        mFoodAStoreNameTv = (TextView) findViewById(R.id.food_a_store_name_tv);
        mFoodBIv = (ImageView) findViewById(R.id.food_b_iv);
        mFoodBNameTv = (TextView) findViewById(R.id.food_b_name_tv);
        mFoodBStoreNameTv = (TextView) findViewById(R.id.food_b_store_name_tv);

        mRecommendCardView = findViewById(R.id.recommend_card_view);
        mFoodRecommendIv = (ImageView) findViewById(R.id.food_recommend_iv);
        mFoodRecommendNameTv = (TextView) findViewById(R.id.food_recommend_name_tv);
        mFoodRecommendStoreNameTv = (TextView) findViewById(R.id.food_recommend_store_name_tv);
        TextView mFoodRecommendPriceUnitTv = (TextView) findViewById(R.id.food_recommend_price_unit_tv);
        mFoodRecommendPriceTv = (TextView) findViewById(R.id.food_recommend_price_tv);
        mTelBtn = findViewById(R.id.tel_btn);

        /**
         *
         */
        mComparisonCardManager = new CardManager(MainActivity.this, mComparisonCardView);
        mRecommendCardManager = new CardManager(MainActivity.this, mRecommendCardView);

        /**
         *
         */
        char unit_won = 0xFFE6;
        mFoodRecommendPriceUnitTv.setText("" + unit_won);

        /**
         *
         */
        findViewById(R.id.location_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLocationDetailApiReadyTask != null) {
                    mLocationDetailApiReadyTask.cancel(true);
                }

                mLocationDetailApiReadyTask = new LocationDetailApiReadyTask();
                mLocationDetailApiReadyTask.execute();
            }
        });

        findViewById(R.id.food_a_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mComparisonAddApiTask == null) {
                    mComparisonAddApiTask = new ComparisonAddApiTask();
                    mComparisonAddApiTask.execute();
                }
            }
        });
        findViewById(R.id.food_b_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mComparisonAddApiTask == null) {
                    mComparisonAddApiTask = new ComparisonAddApiTask();
                    mComparisonAddApiTask.execute();
                }
            }
        });
        findViewById(R.id.another_recommend_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mComparisonApiTask == null) {
                    mComparisonApiTask = new ComparisonListApiTask();
                    mComparisonApiTask.execute();
                }
            }
        });
        mTelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = (String) view.getTag();
                Toast.makeText(MainActivity.this, tel, Toast.LENGTH_SHORT).show();
            }
        });

        /**
         *
         */
        if (mLocationDetailApiReadyTask == null) {
            //    mLocationDetailApiReadyTask = new LocationDetailApiReadyTask();
            //    mLocationDetailApiReadyTask.execute();
        }

        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friendlists",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        /* handle the result */
                        Toast.makeText(MainActivity.this, "1", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "feed response -> " + response.getRawResponse());

                        if (response.getError() != null)
                            Log.d(TAG, "feed error -> " + response.getError().toString());
                    }
                }
        ).executeAsync();


        /* make the API call */
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/feed",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {

                        /* handle the result */
                        Toast.makeText(MainActivity.this, "2", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "feed response -> " + response.getRawResponse());

                        if (response.getError() != null)
                            Log.d(TAG, "feed error -> " + response.getError().toString());
                    }
                }
        ).executeAsync();


    }


    private boolean updateComparison() {

        /**
         *
         */
        if (mComparisons.size() == 0) {
            return false;
        }

        /**
         *
         */
        Comparison comparison = mComparisons.get(0);
        Food food_a = comparison.getFood_a();
        Food food_b = comparison.getFood_b();
        mMsgTv.setText(comparison.getMsg());

        /**
         *
         */
        mFoodANameTv.setText(food_a.getName());
        mFoodAStoreNameTv.setText(food_a.getStore().getName());
        Picasso.with(MainActivity.this).load(food_a.getImg_url()).into(mFoodAIv);
        mFoodBNameTv.setText(food_b.getName());
        mFoodBStoreNameTv.setText(food_b.getStore().getName());
        Picasso.with(MainActivity.this).load(food_b.getImg_url()).into(mFoodBIv);

        return true;
    }

    /**
     *
     */
    public class UserLocationApiTask extends AsyncTask<Void, Void, LocationModel> {

        @Override
        protected void onPreExecute() {

            /**
             *
             */
            mComparisonCardManager.dismiss();
            mRecommendCardManager.dismiss();

        }

        @Override
        protected LocationModel doInBackground(Void... params) {

            try {

                UserLocationApi locationDetailApi = new UserLocationApi(getApplication(),
                        null);

                long sleep_datetime = Math.max(mComparisonCardManager.getLeftTimeOfAnimation(),
                        mRecommendCardManager.getLeftTimeOfAnimation());
                Thread.sleep(sleep_datetime);

                return locationDetailApi.getResult();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(LocationModel locationModel) {

            if (locationModel != null) {

                /**
                 *
                 */
                mLocationTv.setText(locationModel.getName());

                /**
                 *
                 */
                if (mComparisonApiTask == null) {
                    mComparisonApiTask = new ComparisonListApiTask();
                    mComparisonApiTask.execute();
                }

            } else {
                Toast.makeText(MainActivity.this, "위치를 얻어오지 못했습니다", Toast.LENGTH_SHORT).show();
            }


            /**
             *
             */
            mLocationDetailApiTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            /**
             *
             */
            mLocationDetailApiTask = null;
        }
    }


    /**
     *
     */
    public class ComparisonListApiTask extends AsyncTask<Void, Void, ArrayList<Comparison>> {

        @Override
        protected void onPreExecute() {
            /**
             *
             */
            mRecommendCardManager.dismiss();

            /**
             *
             */
            String user_name = PreferenceManager.get(MainActivity.this, Argument.PREFS_USER_FIRST_NAME, null);
            if (user_name == null) {
                user_name = getString(R.string.msg_user_name_default);
            }
            mMsgTv.setText(user_name + getString(R.string.msg_comparison_list_api));

        }

        @Override
        protected ArrayList<Comparison> doInBackground(Void... params) {

            try {

                ComparisonListApi comparisonListApi = new ComparisonListApi(getApplication(), null);
                ArrayList<Comparison> comparisons = comparisonListApi.getResult();

                //
                long sleep_datetime = Math.max(mComparisonCardManager.getLeftTimeOfAnimation(),
                        mRecommendCardManager.getLeftTimeOfAnimation());
                Thread.sleep(sleep_datetime);

                //
                return comparisons;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(ArrayList<Comparison> comparisons) {

            /**
             *
             */
            mComparisons.clear();
            mComparisons.addAll(comparisons);

            /**
             *
             */
            updateComparison();
            mComparisonCardManager.appear();

            /**
             *
             */
            mComparisonApiTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            /**
             *
             */
            mComparisonApiTask = null;
        }
    }

    /**
     *
     */
    public class LocationDetailApiReadyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            /**
             *
             */
            mLocationTv.setText(R.string.main_activity_location_tv_default);

            /**
             *
             */
            String user_name = PreferenceManager.get(MainActivity.this, Argument.PREFS_USER_FIRST_NAME, null);
            if (user_name == null) {
                user_name = getString(R.string.msg_user_name_default);
            }
            mMsgTv.setText(user_name + getString(R.string.msg_location_detail_api));

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                long sleep_datetime = Math.max(mComparisonCardManager.getLeftTimeOfAnimation(),
                        mRecommendCardManager.getLeftTimeOfAnimation());
                Thread.sleep(sleep_datetime);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void param) {

            if (mLocationDetailApiTask != null) {
                mLocationDetailApiTask.cancel(true);
            }
            mLocationDetailApiTask = new UserLocationApiTask();
            mLocationDetailApiTask.execute();

            /**
             *
             */
            mLocationDetailApiReadyTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            /**
             *
             */
            mLocationDetailApiReadyTask = null;
        }
    }

    /**
     *
     */
    public class ComparisonAddApiTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            /**
             *
             */
            mComparisonCardManager.dismiss();

            /**
             *
             */
            mMsgTv.setText(R.string.msg_comparison_add_api);
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                ComparisonAddApi comparisonAddApi = new ComparisonAddApi();
                comparisonAddApi.getResult();
                mComparisons.remove(0);

                long sleep_datetime = Math.max(mComparisonCardManager.getLeftTimeOfAnimation(),
                        mRecommendCardManager.getLeftTimeOfAnimation());
                Thread.sleep(sleep_datetime);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void param) {

            if (updateComparison()) {

                mComparisonCardManager.appear();


            } else {

                if (mRecommendListApiTask == null) {
                    mRecommendListApiTask = new RecommendListApiTask();
                    mRecommendListApiTask.execute();
                }

            }

            /**
             *
             */
            mComparisonAddApiTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            /**
             *
             */
            mComparisonAddApiTask = null;
        }
    }

    /**
     *
     */
    public class RecommendListApiTask extends AsyncTask<Void, Void, ArrayList<Food>> {

        @Override
        protected void onPreExecute() {
            String user_name = PreferenceManager.get(MainActivity.this, Argument.PREFS_USER_FIRST_NAME, null);
            if (user_name == null) {
                user_name = getString(R.string.msg_user_name_default);
            }
            mMsgTv.setText(user_name + getString(R.string.msg_recommend_list_api));
            mComparisonCardManager.dismiss();
        }

        @Override
        protected ArrayList<Food> doInBackground(Void... params) {

            ArrayList<Food> foods = null;

            try {
                ComparisonRecommendationsApi recommendListApi = new ComparisonRecommendationsApi(getApplication(),
                        ComparisonRecommendationsApi.createParams(1));
                foods = recommendListApi.getResult();

                long sleep_datetime = Math.max(mComparisonCardManager.getLeftTimeOfAnimation(),
                        mRecommendCardManager.getLeftTimeOfAnimation());
                Thread.sleep(sleep_datetime);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return foods;
        }

        @Override
        protected void onPostExecute(ArrayList<Food> foods) {

            /**
             *
             */
            Food food = foods.get(0);
            mFoodRecommendNameTv.setText(food.getName());
            mFoodRecommendStoreNameTv.setText(food.getStore().getName());
            Picasso.with(MainActivity.this).load(food.getImg_url()).into(mFoodRecommendIv);
            mFoodRecommendPriceTv.setText(String.format("%,d", food.getPrice()));
            mTelBtn.setTag(food.getStore().getTel());

            /**
             *
             */
            String user_name = PreferenceManager.get(MainActivity.this, Argument.PREFS_USER_FIRST_NAME, null);
            if (user_name == null) {
                user_name = getString(R.string.msg_user_name_default);
            }
            String msg = getString(R.string.msg_recommend_list_api_completed_pre)
                    + user_name
                    + getString(R.string.msg_recommend_list_api_completed);
            mMsgTv.setText(msg);

            /**
             *
             */
            mRecommendCardManager.appear();

            /**
             *
             */
            mRecommendListApiTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            /**
             *
             */
            mRecommendListApiTask = null;
        }
    }

    /**
     *
     */
    public void onDestroy() {
        super.onDestroy();

        if (mComparisonApiTask != null) {
            mComparisonApiTask.cancel(true);
            mComparisonApiTask = null;
        }

        if (mComparisonAddApiTask != null) {
            mComparisonAddApiTask.cancel(true);
            mComparisonAddApiTask = null;
        }

        if (mRecommendListApiTask != null) {
            mRecommendListApiTask.cancel(true);
            mRecommendListApiTask = null;
        }

    }

    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
