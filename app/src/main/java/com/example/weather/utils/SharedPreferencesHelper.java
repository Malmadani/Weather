package com.example.weather.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.example.weather.R;

public class SharedPreferencesHelper {

    public static String getPreferedWeatherLocation(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String locationKey = context.getString(R.string.pref_location_key);
        String defaultLocation = context.getString(R.string.pref_location_default);
        return preferences.getString(locationKey, defaultLocation);
    }

    public static String getPreferedMeasurementSystem(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String unitKey = context.getString(R.string.pref_units_key);
        String defaultUnit = context.getString(R.string.pref_unit_metric);
        return preferences.getString(unitKey, defaultUnit);
    }
}
