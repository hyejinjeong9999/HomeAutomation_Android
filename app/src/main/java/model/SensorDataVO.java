package model;

import java.io.Serializable;

public  class SensorDataVO implements Serializable{
    String mode="OFF"; // 모드
    String temp = "0"; // 실내 온도
    String dust25 = "0";
    String dust10 = "0";
    String gasStatus = "0";
    String windowStatus = "0"; // 창문 onoff
    String airpurifierStatus = "0"; // 공기청정기 onoff
    String airconditionerStatus = "0"; // 에어컨 onoff
    String lightStatus = "0"; //전등

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDust25() {
        return dust25;
    }

    public void setDust25(String dust25) {
        this.dust25 = dust25;
    }

    public String getDust10() {
        return dust10;
    }

    public void setDust10(String dust10) {
        this.dust10 = dust10;
    }

    public String getGasStatus() {
        return gasStatus;
    }

    public void setGasStatus(String gasStatus) {
        this.gasStatus = gasStatus;
    }

    public String getWindowStatus() {
        return windowStatus;
    }

    public void setWindowStatus(String windowStatus) {
        this.windowStatus = windowStatus;
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