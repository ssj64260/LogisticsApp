package com.msqsoft.aboutapp.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LocationUtil {
    /**
    *
    * 根据经纬度获取地址
    *
    */
    public static Address getCity(Context context, double longitude, double latitude) throws IOException {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = new ArrayList<>();
        String locality = null;
        Address address = null;
        try {
            //根据经纬度获取地理位置信息
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (addresses.size() > 0) {
            address = addresses.get(0);
//            locality = address.getCountryName() + address.getAdminArea() + address.getLocality()
//                    + address.getSubLocality() + address.getFeatureName();
        }
//        return locality;
        return address;
    }
}
