package com.forasterisk.board.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import com.forasterisk.board.R;
import com.forasterisk.board.api.UserSessionApi;
import com.forasterisk.board.utils.FacebookManager;

public class LoginActivity extends ActionBarActivity {

    private static String TAG = "LoginActivity";

    /**
     *
     */
    private UserSessionApiTask mAuthLoginFacebookApiTask = null;

    /**
     *
     */
    private View mLoginView;
    private View mLoginBtn;
    private View mProgressBar;

    private FacebookManager facebookManager;


    /**
     * @param activity
     */
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // It should init before setContentView()
        facebookManager = new FacebookManager(LoginActivity.this);
        setContentView(R.layout.login_activity);

        /**
         *
         */
        mLoginView = findViewById(R.id.login_view);
        mLoginBtn = findViewById(R.id.login_btn);
        mProgressBar = findViewById(R.id.progress_bar);
        facebookManager.setLoginButton(findViewById(R.id.hidden_fb_login_btn));

        /**
         *
         */
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mLoginView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                facebookManager.requestLogin();
            }
        });

        /**
         *
         */
        facebookManager.registerCallback(new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Facebook Login onSuccess()");
                String access_token = loginResult.getAccessToken().getToken();
                Log.d(TAG, "access_token -> " + access_token);

                if (mAuthLoginFacebookApiTask == null) {
                    mAuthLoginFacebookApiTask = new UserSessionApiTask();
                    mAuthLoginFacebookApiTask.execute(access_token);
                }
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Facebook Login Canceled");
                mLoginView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "Facebook Login Error!");
            }
        });
    }


    /**
     *
     */
    public class UserSessionApiTask extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            mLoginView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(String... token) {

            try {
                UserSessionApi authLoginFacebookApi =
                        new UserSessionApi(getApplication(), UserSessionApi.createParams(token[0]));
                authLoginFacebookApi.getResult();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void param) {

            /**
             *
             */
            MainActivity.startActivity(LoginActivity.this);
            finish();

            /**
             *
             */
            mAuthLoginFacebookApiTask = null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            /**
             *
             */
            mLoginView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);

            /**
             *
             */
            mAuthLoginFacebookApiTask = null;
        }
    }

    /**
     *
     */
    public void onDestroy() {
        super.onDestroy();

        if (mAuthLoginFacebookApiTask != null) {
            mAuthLoginFacebookApiTask.cancel(true);
            mAuthLoginFacebookApiTask = null;
        }
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookManager.onActivityResult(requestCode, resultCode, data);
    }
}
