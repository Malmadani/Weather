package com.example.weather.entity;

import java.util.List;

public class Forecast {

    private List<Weather> weathers;
    private Main main;
    private Wind wind;
    private Long Dt;
    private Sys sys;
    private String name;

    public List<Weather> getWeathers() {
        return weathers;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public void setWeathers(List<Weather> weathers) {
        this.weathers = weathers;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Long getDt() {
        return Dt;
    }

    public void setDt(Long dt) {
        Dt = dt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
