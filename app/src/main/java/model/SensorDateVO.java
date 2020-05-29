package model;

import java.io.Serializable;

public  class SensorDateVO implements Serializable{
    String temp = "0"; // 실내 모드
    String mode = "0"; // 모드
    String windowStatus = "0"; // 창문 onoff
    String dustDensity = "0"; // 실내 미세먼지

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

    String airpurifierStatus; // 공기청정기 onoff
    String airconditionerStatus; // 에어컨 onoff
}