package com.example.weather.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weather.R;
import com.example.weather.entity.WeatherInfo;

public class SecondaryWeatherInfoFragment extends Fragment {

    private TextView mWind;
    private TextView mPressure;
    private TextView mHumidity;
    private View itemView;

    private WeatherInfo mWeatherInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.secondary_weather_info_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        itemView = getView();

        mWind = itemView.findViewById(R.id.wind);
        mPressure = itemView.findViewById(R.id.pressure);
        mHumidity = itemView.findViewById(R.id.humidity);

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

        String pressure = mWeatherInfo.getMain().getPressure() + "";

        mPressure.setText(pressure);

        String humidity = mWeatherInfo.getMain().getHumidity() + "";

        mHumidity.setText(humidity);
    }
}
