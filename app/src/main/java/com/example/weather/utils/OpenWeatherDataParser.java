package com.example.weather.utils;

import com.example.weather.entity.Main;
import com.example.weather.entity.Weather;
import com.example.weather.entity.WeatherInfo;
import com.example.weather.entity.Wind;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OpenWeatherDataParser {

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
}
