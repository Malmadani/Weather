package com.example.weather.entity;

import java.util.List;

public class ForecastLists {

    private List<Forecast> hoursForecasts = null;
    private List<List<Forecast>> daysForecast = null;

    public List<Forecast> getHoursForecasts() {
        return hoursForecasts;
    }

    public void setHoursForecasts(List<Forecast> hoursForecasts) {
        this.hoursForecasts = hoursForecasts;
    }

    public List<List<Forecast>> getDaysForecast() {
        return daysForecast;
    }

    public void setDaysForecast(List<List<Forecast>> daysForecast) {
        this.daysForecast = daysForecast;
    }
}
