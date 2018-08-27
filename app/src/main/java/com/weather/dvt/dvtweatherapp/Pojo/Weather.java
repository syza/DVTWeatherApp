package com.weather.dvt.dvtweatherapp.Pojo;

public class Weather {

    private Double marTemperature;
    private String iconIdentifies;
    private String weatherDescription;
    private String weatherDate;


    public Weather(Double marTemperature, String iconIdentifies, String weatherDescription, String weatherDate) {
        this.marTemperature = marTemperature;
        this.iconIdentifies = iconIdentifies;
        this.weatherDescription = weatherDescription;
        this.weatherDate = weatherDate;
    }

    public Double getMarTemperature() {
        return marTemperature;
    }

    public void setMarTemperature(Double marTemperature) {
        this.marTemperature = marTemperature;
    }

    public String getIconIdentifies() {
        return iconIdentifies;
    }

    public void setIconIdentifies(String iconIdentifies) {
        this.iconIdentifies = iconIdentifies;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getWeatherDate() {
        return weatherDate;
    }

    public void setWeatherDate(String weatherDate) {
        this.weatherDate = weatherDate;
    }
}
