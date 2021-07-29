package com.example.weather.network;

import com.example.weather.entity.WeatherForecasts;
import com.example.weather.entity.WeatherInfo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface OpenWeatherApiInterface {

    String WEATHER_ENDPOINT = "weather";
    String FORECAST_ENDPOINT = "forecast";

    @GET(WEATHER_ENDPOINT)
    Call<WeatherInfo> getWeatherInfo(@QueryMap Map<String, String> queryParams);

    @GET(FORECAST_ENDPOINT)
    Call<WeatherForecasts> getForecastInfo(@QueryMap Map<String, String> queryParams);

}
