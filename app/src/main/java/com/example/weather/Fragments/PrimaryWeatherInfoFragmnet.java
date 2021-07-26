package com.example.weather.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weather.R;
import com.example.weather.entity.WeatherInfo;
import com.example.weather.utils.CustomDateUtils;
import com.example.weather.utils.WeatherUtils;

public class PrimaryWeatherInfoFragmnet extends Fragment {

    private ImageView mIconView;
    private TextView mCityNameTextView;
    private TextView mDateView;
    private TextView mDescriptionView;
    private TextView mTempTextView;
    private TextView mHighLowTempView;

    private WeatherInfo mWeatherInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_primary_weather_info, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View itemView = getView();

        // Initialize member variables
        mIconView = itemView.findViewById(R.id.weather_icon);
        mCityNameTextView = itemView.findViewById(R.id.city);
        mDateView = itemView.findViewById(R.id.date);
        mDescriptionView = itemView.findViewById(R.id.weather_description);
        mTempTextView = itemView.findViewById(R.id.temperature);
        mHighLowTempView = itemView.findViewById(R.id.high_low_temperature);

        showWeatherInfo();
    }

    public void ubdateWeatherInfo(WeatherInfo weatherInfo) {
        mWeatherInfo = weatherInfo;
        showWeatherInfo();
    }

    private void showWeatherInfo() {

        if (mWeatherInfo == null) {
            return;
        }
        /* Weather Icon ************************************************************************* */

        // Get the weather icon resource id based on icon string passed from the api
        int weatherImageId = WeatherUtils.getWeatherIcon(mWeatherInfo.getWeather().get(0).getIcon());

        // Display weather condition icon
        mIconView.setImageResource(weatherImageId);

        /* Current city ************************************************************************* */

        // Read date from weather info object
        String cityName = mWeatherInfo.getName();

        // Display city name
        mCityNameTextView.setText(cityName);

        /* Weather Date ************************************************************************* */

        // Get human readable string using getFriendlyDateString utility method and display it
        String dateString = CustomDateUtils.getFriendlyDateString(getContext(), mWeatherInfo.getDt(), false);

        /* Display friendly date string */
        mDateView.setText(dateString);

        /* Weather Description ****************************************************************** */

        // Get weather condition description
        String description = mWeatherInfo.getWeather().get(0).getDescription();

        // Display weather description
        mDescriptionView.setText(description);


        /* Temperature ************************************************************************** */

        // Read temperature from weather object
        String temperatureString = getString(R.string.format_temperature, mWeatherInfo.getMain().getTemp());

        // Display high temperature
        mTempTextView.setText(temperatureString);


        /* High (max) & Low (min) temperature temperature *************************************** */

        // Read high temperature from weather object
        String highTemperatureString = "19°";

        // Read low temperature from weather object
        String lowTemperatureString = "10°";

        // Display high/low temperature
        mHighLowTempView.setText(getString(R.string.high_low_temperature, mWeatherInfo.getMain().getTempMax(), mWeatherInfo.getMain().getTempMin()));

    }
}
