package com.forasterisk.board.api;

import android.app.Application;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.forasterisk.board.model.LocationModel;

/**
 * Created by yearnning on 15. 8. 4..
 */
public class UserLocationApi extends ApiBase {
    private static final String API_NAME = "UserLocationApi";

    /**
     *
     */
    private static final String PARAM_LATITUDE = "latitude";
    private static final String PARAM_LONGITUDE = "longitude";

    /**
     *
     */
    private LocationManager locationManager;

    /**
     *
     */
    private LocationModel locationModel = null;

    /**
     * @param application
     * @param params
     */
    public UserLocationApi(Application application, HashMap<String, String> params) {
        super(application, params);
        Log.d(API_NAME, "api_started!");

        /**
         *
         */
        Location location = getLocation();

        /**
         *
         */
        this.mParams = UserLocationApi.createParams(location.getLatitude(), location.getLongitude());

        /**
         *
         */
        this.locationModel = getLocationModel(location);

        /**
         * Handle Response
         */
        String response = getResponseFromRemote();
        if (response == null) {
            return;
        }

        Log.d(API_NAME, "response -> " + response);

    }

    public LocationModel getResult() {
        return this.locationModel;
    }

    private Location getLocation() {

        Location location = null;
        try {
            /**
             * Acquire a reference to the system Location Manager
             */
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.d(API_NAME, "latitude:" + location.getLatitude() + ",longitude:" + location.getLongitude());
            }

            if (location == null) {
                //TODO: 이거 핸들해야함!!! 지금... 간단하게 가져오고 있는데, 로케이션을 더 정확하게 가져와야 할듯!
                location = new Location("");
                location.setLatitude(36.3693921);
                location.setLongitude(127.3640249);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    private LocationModel getLocationModel(Location location) {

        LocationModel locationModel = new LocationModel();

        try {

            /**
             * get thoroughfare name from using Geocoder.
             */
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(), 20);
            //List<Address> addresses = geocoder.getFromLocation(
            //        36.3682313, 127.3822256, 20);
            String address_thoroughfare = null;
            for (Address address : addresses) {
                Log.d(API_NAME, "address -> " + address.getThoroughfare());
                if (address.getThoroughfare() != null) {
                    address_thoroughfare = (address.getThoroughfare());
                    break;
                }
            }

            /**
             * make LocationModel
             */
            if (address_thoroughfare == null) {
                address_thoroughfare = "구성동";
            }
            locationModel.setLatitude(location.getLatitude());
            locationModel.setLongitude(location.getLongitude());
            locationModel.setName(address_thoroughfare);

            return locationModel;
        } catch (Exception e) {
            e.printStackTrace();
            locationModel.setLatitude(location.getLatitude());
            locationModel.setLongitude(location.getLongitude());
            locationModel.setName("내 위치");

            return locationModel;
        }
    }


    /**
     * @return
     */

    protected String getResponseFromRemote() {
        return super.getResponseFromRemote("/user/location", "PUT", "httpa");
    }


    public static HashMap<String, String> createParams(double latitude, double longitude) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_LATITUDE, "" + latitude);
        params.put(PARAM_LONGITUDE, "" + longitude);
        return params;
    }

}
