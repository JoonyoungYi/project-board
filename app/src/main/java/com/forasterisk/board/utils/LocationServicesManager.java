package com.forasterisk.board.utils;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import com.forasterisk.board.model.LocationModel;

/**
 * Created by yearnning on 15. 8. 10..
 */
public class LocationServicesManager {

    private static final String TAG = "LocationServicesManager";

    /**
     *
     */
    private Context context;
    private GoogleApiClient mGoogleApiClient;

    /**
     *
     */
    interface OnCompleted {
        void onSuccess(LocationModel locationModel);
        void onFailed(String msg);
    }

    /**
     *
     */

    /**
     * @param context
     */
    public LocationServicesManager(Context context) {
        this.context = context;

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (location == null) {
                            // Blank for a moment...
                        } else {
                            Log.d(TAG, "google play service -> latitude:" + location.getLatitude() + ",longitude:" + location.getLongitude());
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.d(TAG, "fail connection -> " + connectionResult.toString());
                    }
                })
                .addApi(LocationServices.API)
                .build();

        // mGoogleApiClient.connect();

    }
}
