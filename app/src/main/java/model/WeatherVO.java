package model;

import java.io.Serializable;

public class WeatherVO implements Serializable {
    String weather = "0"; // weather - main
    String temp = "0"; // main - temp
    String feelsLike = "0"; // main - feels_like
    String tempMin = "0"; // main - temp_min
    String tempMax = "0"; // main - temp_max
    String humidity = "0"; // main - humidity
    String name = "0"; // name, 지역명
    String pm10Value = "0";
    String pm10Value24 = "0";
    String pm25Value = "0";
    String pm25Value24 = "0";

    public String getPm10Value() {
        return pm10Value;
    }

    public void setPm10Value(String pm10Value) {
        this.pm10Value = pm10Value;
    }

    public String getPm10Value24() {
        return pm10Value24;
    }

    public void setPm10Value24(String pm10Value24) {
        this.pm10Value24 = pm10Value24;
    }

    public String getPm25Value() {
        return pm25Value;
    }

    public void setPm25Value(String pm25Value) {
        this.pm25Value = pm25Value;
    }

    public String getPm25Value24() {
        return pm25Value24;
    }

    public void setPm25Value24(String pm25Value24) {
        this.pm25Value24 = pm25Value24;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemp() {
        // String 소숫점 한 자리까지 잘라오기
        temp = String.format("%.4s",temp);
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(String feelsLike) {
        this.feelsLike = feelsLike;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void checkElement(){
        if(weather == null) weather = "0";
        if(temp == null)temp = "0"; // main - temp
        if(feelsLike == null)feelsLike = "0"; // main - feels_like
        if(tempMin == null)tempMin = "0"; // main - temp_min
        if(tempMax == null) tempMax = "0"; // main - temp_max
        if(humidity == null)humidity = "0"; // main - humidity
        if(name == null)name = "0"; // name, 지역명
        if(pm10Value == null)pm10Value = "0";
        if(pm10Value24 == null)pm10Value24 = "0";
        if(pm25Value == null)pm25Value = "0";
        if(pm25Value24 == null)pm25Value24 = "0";

    }

}