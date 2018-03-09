package kr.ac.kaist.board.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import kr.ac.kaist.board.R;
import kr.ac.kaist.board.api.ApiBase;
import kr.ac.kaist.board.api.BoardInitApi;
import kr.ac.kaist.board.api.LoginAraApi;
import kr.ac.kaist.board.api.LoginPortalApi;
import kr.ac.kaist.board.utils.Argument;


public class SplashActivity extends FragmentActivity {
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

        mLoginApiTask = new LoginApiTask();
        mLoginApiTask.execute();
    }

    /**
     *
     */
    public class LoginApiTask extends AsyncTask<Void, Void, Void> {
        private int request_code = Argument.REQUEST_CODE_UNEXPECTED;

        /**
         * @param params
         * @return
         */
        @Override
        protected Void doInBackground(Void... params) {

            try {

                if (ApiBase.getStringInPrefs(getApplication(), Argument.PREFS_PORTAL_LOGIN, "false").equals("false")) {
                    LoginPortalApi loginPortalApi = new LoginPortalApi(getApplication(), "", "");
                    request_code = loginPortalApi.getRequestCode();
                    if (request_code == Argument.REQUEST_CODE_SUCCESS) {
                        loginPortalApi.getResult();
                    }
                }

                if (ApiBase.getStringInPrefs(getApplication(), Argument.PREFS_ARA_LOGIN, "false").equals("false")) {
                    LoginAraApi loginPortalApi = new LoginAraApi(getApplication(), "", "");
                    request_code = loginPortalApi.getRequestCode();
                    if (request_code == Argument.REQUEST_CODE_SUCCESS) {
                        loginPortalApi.getResult();
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
/*
            try {
                //
                LoginAraApi loginAraApi = new LoginAraApi(getApplication(), "", "");
                request_code = loginAraApi.getRequestCode();
                if (request_code == Argument.REQUEST_CODE_SUCCESS) {
                    loginAraApi.getResult();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
*/
            /*
            try {
                LoginBfApi loginBfApi = new LoginBfApi(getApplication(), "", "");
                loginBfApi.getResult();

            } catch (Exception e) {
                e.printStackTrace();
            }*/

            return null;
        }


        @Override
        protected void onPostExecute(Void param) {
            mLoginApiTask = null;
            ApiBase.showToastMsg(getApplication(), request_code);

            //if (request_code == Argument.REQUEST_CODE_SUCCESS) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in,
                    android.R.anim.fade_out);
            /*
            } else if (request_code == Argument.REQUEST_CODE_FAIL) {

                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
            } */

            finish();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mLoginApiTask = null;
        }
    }

    @Override
    public void onDestroy() {
        if (mLoginApiTask != null) {
            mLoginApiTask.cancel(true);
        }

        super.onDestroy();
    }

}
