package model;

import java.io.Serializable;

public  class SensorDataVO implements Serializable{
//    String temp = "0";
//    String light =  "0";
//    String onOff = "0";
//    String dustDensity = "0";
//
//    public String getDustDensity() {
//        return dustDensity;
//    }
//
//    public void setDustDensity(String dustDensity) {
//        this.dustDensity = dustDensity;
//    }
//
//    public String getTemp() {
//        return temp;
//    }
//
//    public void setTemp(String temp) {
//        this.temp = temp;
//    }
//
//    public String getLight() {
//        return light;
//    }
//
//    public void setLight(String light) {
//        this.light = light;
//    }
//
//    public String getOnOff() {
//        return onOff;
//    }
//
//    public void setOnOff(String onOff) {
//        this.onOff = onOff;
//    }

    String temp = "0"; // 실내 모드
    String mode="0"; // 모드
    String windowStatus="0"; // 창문 onoff
    String dustDensity="0"; // 실내 미세먼지
    String airpurifierStatus="0"; // 공기청정기 onoff
    String airconditionerStatus="0"; // 에어컨
    String lightStatus = "0";

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getWindowStatus() {
        return windowStatus;
    }

    public void setWindowStatus(String windowStatus) {
        this.windowStatus = windowStatus;
    }

    public String getDustDensity() {
        return dustDensity;
    }

    public void setDustDensity(String dustDensity) {
        this.dustDensity = dustDensity;
    }

    public String getAirpurifierStatus() {
        return airpurifierStatus;
    }

    public void setAirpurifierStatus(String airpurifierStatus) {
        this.airpurifierStatus = airpurifierStatus;
    }

    public String getAirconditionerStatus() {
        return airconditionerStatus;
    }

    public void setAirconditionerStatus(String airconditionerStatus) {
        this.airconditionerStatus = airconditionerStatus;
    }

    public String getLightStatus() {
        return lightStatus;
    }

    public void setLightStatus(String lightStatus) {
        this.lightStatus = lightStatus;
    }
}