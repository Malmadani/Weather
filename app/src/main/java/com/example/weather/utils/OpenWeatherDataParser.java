package com.example.weather.utils;

import android.util.Log;

import com.example.weather.entity.Forecast;
import com.example.weather.entity.ForecastLists;
import com.example.weather.entity.Main;
import com.example.weather.entity.Weather;
import com.example.weather.entity.WeatherInfo;
import com.example.weather.entity.Wind;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OpenWeatherDataParser {

    private static final String TAG = OpenWeatherDataParser.class.getSimpleName();

    private static final String OWM_MESSAGE_CODE = "cod";
    private static final String OWM_CITY = "city";
    private static final String OWM_CITY_NAME = "name";
    private static final String OWM_LIST = "list";
    private static final String OWM_DATE = "dt";
    private static final String OWM_DATE_TEXT = "dt_txt";
    private static final String OWM_WIND = "wind";
    private static final String OWM_WINDSPEED = "speed";
    private static final String OWM_WIND_DIRECTION = "deg";
    private static final String OWM_MAIN = "main";
    private static final String OWM_TEMPERATURE = "temp";
    private static final String OWM_MAX = "temp_max";
    private static final String OWM_MIN = "temp_min";
    private static final String OWM_PRESSURE = "pressure";
    private static final String OWM_HUMIDITY = "humidity";
    private static final String OWM_WEATHER = "weather";
    private static final String OWM_WEATHER_DESCRIPTION = "description";
    private static final String OWM_WEATHER_ICON = "icon";

    private static boolean isError(JSONObject jsonObject) {
        try {
            // Check the response code to see if there is an error
            if (jsonObject.has(OWM_MESSAGE_CODE)) {
                int errorCode = jsonObject.getInt(OWM_MESSAGE_CODE);
                switch (errorCode) {
                    case HttpURLConnection.HTTP_OK:
                        return false;
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        Log.e(TAG, "Location Invalid");
                    default:
                        Log.e(TAG, "Server probably down");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static WeatherInfo getWeatherInfoObjectFormJson(JSONObject weatherJson) throws JSONException {

        JSONObject weatherJsonObject = weatherJson.getJSONArray(OWM_WEATHER).getJSONObject(0);

        JSONObject mainJsonObject = weatherJson.getJSONObject(OWM_MAIN);

        JSONObject windJsonObject = weatherJson.getJSONObject(OWM_WIND);

        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setDt(weatherJson.getLong(OWM_DATE));
        Main main = new Main();
        main.setTemp(mainJsonObject.getDouble(OWM_TEMPERATURE));
        main.setTempMax(mainJsonObject.getDouble(OWM_MAX));
        main.setTempMin(mainJsonObject.getDouble(OWM_MIN));
        main.setHumidity(mainJsonObject.getInt(OWM_HUMIDITY));
        main.setPressure(mainJsonObject.getLong(OWM_PRESSURE));
        weatherInfo.setMain(main);
        Wind wind = new Wind();
        wind.setSpeed(windJsonObject.getDouble(OWM_WINDSPEED));
        wind.setDeg(windJsonObject.getLong(OWM_WIND_DIRECTION));
        weatherInfo.setWind(wind);
        List<Weather> weatherList = new ArrayList<>();
        Weather weather = new Weather();
        weather.setDescription(weatherJsonObject.getString(OWM_WEATHER_DESCRIPTION));
        weather.setIcon(weatherJsonObject.getString(OWM_WEATHER_ICON));
        weatherList.add(weather);
        weatherInfo.setWeather(weatherList);
        weatherInfo.setName(weatherJson.has(OWM_CITY_NAME) ? weatherJson.getString(OWM_CITY_NAME) : "");
        return weatherInfo;

    }

    public static ForecastLists getForecastInfoObjectFormJson(JSONObject forecastsJson) throws JSONException {

        if (isError(forecastsJson)) {
            return null;
        }

        JSONArray jsonForecastsArray = forecastsJson.getJSONArray(OWM_LIST);

        Forecast forecast = new Forecast();

        List<Forecast> hoursForecasts = new ArrayList<>();
        LinkedHashMap<String, List<Forecast>> daysForecasts = new LinkedHashMap<>();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String currentDay = df.format(new Date());
        int hoursForecastsCount = 0;

        for (int i = 0; i < jsonForecastsArray.length(); i++) {

            JSONObject singleForecastJson = jsonForecastsArray.getJSONObject(i);

            // Weather description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = singleForecastJson.getJSONArray(OWM_WEATHER).getJSONObject(0);

            // Temperatures are sent by OpenWeatherMap in a child object called Main
            JSONObject mainObject = singleForecastJson.getJSONObject(OWM_MAIN);

            // Wind speed and direction are wrapped in a Wind object
            JSONObject windObject = singleForecastJson.getJSONObject(OWM_WIND);

            forecast.setDt(singleForecastJson.getLong(OWM_DATE));
            forecast.setName(singleForecastJson.getString(OWM_DATE_TEXT));
            Main main = new Main();
            main.setTemp(mainObject.getDouble(OWM_TEMPERATURE));
            main.setTempMax(mainObject.getDouble(OWM_MAX));
            main.setTempMin(mainObject.getDouble(OWM_MIN));
            main.setHumidity(mainObject.getInt(OWM_HUMIDITY));
            main.setPressure(mainObject.getLong(OWM_PRESSURE));
            forecast.setMain(main);
            Wind wind = new Wind();
            wind.setSpeed(windObject.getDouble(OWM_WINDSPEED));
            wind.setDeg(windObject.getLong(OWM_WIND_DIRECTION));
            forecast.setWind(wind);
            Weather weather = new Weather();
            weather.setDescription(weatherObject.getString(OWM_WEATHER_DESCRIPTION));
            weather.setIcon(weatherObject.getString(OWM_WEATHER_ICON));
            List<Weather> weatherList = new ArrayList<>();
            weatherList.add(weather);
            forecast.setWeathers(weatherList);


            if (hoursForecastsCount++ < 8) {
                hoursForecasts.add(forecast);
            }

            String date = forecast.getName().split(" ")[0];

            if (!date.equals(currentDay)) {
                if (daysForecasts.containsKey(date)) {
                    List<Forecast> forecasts = daysForecasts.get(date);
                    assert forecasts != null;
                    forecasts.add(forecast);
                } else {
                    List<Forecast> forecasts = new ArrayList<>();
                    forecasts.add(forecast);
                    daysForecasts.put(date, forecasts);
                }
            }
        }

        ForecastLists forecastsData = new ForecastLists();
        forecastsData.setHoursForecasts(hoursForecasts);
        List<List<Forecast>> listOfDaysForecasts = new ArrayList<>();
        for (Map.Entry entry : daysForecasts.entrySet()) {
            listOfDaysForecasts.add((List<Forecast>) entry.getValue());
        }
        forecastsData.setDaysForecast(listOfDaysForecasts);

        return forecastsData;
    }
}
