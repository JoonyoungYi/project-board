package com.forasterisk.board.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.forasterisk.board.R;

public class SplashActivity extends ActionBarActivity {
    private static final String TAG = "Splash Activity";

    /**
     *
     */

    LoginApiTask mLoginApiTask = null;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);


        /**
         *
         */
        mLoginApiTask = new LoginApiTask();
        mLoginApiTask.execute();

    }

    @Override
    public void onDestroy() {
        if (mLoginApiTask != null) {
            mLoginApiTask.cancel(true);
        }

        super.onDestroy();
    }


    /**
     *
     */
    public class LoginApiTask extends AsyncTask<Void, Void, String> {

        /**
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(Void... params) {

            /**
             *
             */
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String server_status_message) {

            //MainActivity.startActivity(SplashActivity.this);
            LoginActivity.startActivity(SplashActivity.this);
            finish();


            mLoginApiTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mLoginApiTask = null;
        }

    }

}
