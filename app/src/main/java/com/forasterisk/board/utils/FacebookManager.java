package com.forasterisk.board.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yearnning on 15. 8. 1..
 */
public class FacebookManager {

    private static final String TAG = "FacebookManager";

    /**
     *
     */
    private Context context;
    private CallbackManager callbackManager;
    private FacebookCallback<LoginResult> facebookCallback;

    /**
     *
     */
    private LoginButton loginButton;

    /**
     * @param context
     */
    public FacebookManager(Context context) {
        FacebookSdk.sdkInitialize(context.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        this.context = context;

    }

    /**
     * @param loginButton
     */
    public void setLoginButton(View loginButton) {
        this.loginButton = (LoginButton) loginButton;
        List<String> permissions = new ArrayList<String>();
        permissions.add("public_profile");
        permissions.add("email");
        permissions.add("user_friends");
        //permissions.add("user_likes");
        permissions.add("user_posts");
        this.loginButton.setReadPermissions(permissions);
        this.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                Bundle bundle = new Bundle();
                bundle.putString("fields", "id,name,first_name,locale");
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/me",
                        bundle,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                Log.d(TAG, "response -> " + response.getRawResponse());
                                try {
                                    String locale = response.getJSONObject().getString("locale");
                                    if (locale.equals("ko_KR")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("locale", locale);
                                        bundle.putString("fields", "first_name,name,id");
                                        new GraphRequest(
                                                AccessToken.getCurrentAccessToken(),
                                                "/me",
                                                bundle,
                                                HttpMethod.GET,
                                                new GraphRequest.Callback() {
                                                    public void onCompleted(GraphResponse response) {
                                                        Log.d(TAG, "response -> " + response.getRawResponse());

                                                        try {
                                                            String first_name = response.getJSONObject().getString("first_name");
                                                            PreferenceManager.put(context, Argument.PREFS_USER_FIRST_NAME, first_name);

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        facebookCallback.onSuccess(loginResult);
                                                    }
                                                }
                                        ).executeAsync();
                                    } else {
                                        String first_name = response.getJSONObject().getString("first_name");
                                        PreferenceManager.put(context, Argument.PREFS_USER_FIRST_NAME, first_name);
                                        facebookCallback.onSuccess(loginResult);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                ).executeAsync();

            }

            @Override
            public void onCancel() {
                facebookCallback.onCancel();

            }

            @Override
            public void onError(FacebookException e) {
                facebookCallback.onError(e);
            }
        });
    }

    /**
     *
     */
    public void registerCallback(FacebookCallback<LoginResult> facebookCallback) {
        this.facebookCallback = facebookCallback;
    }

    /**
     *
     */
    public void requestLogin() {
        LoginManager.getInstance().logOut();
        loginButton.performClick();
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
