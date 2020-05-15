package model;

import java.io.Serializable;

public  class WindowVO implements Serializable{
    String temp = "0";
    String light =  "0";
    String onOff = "0";
    String dustDensity = "0";

    public String getDustDensity() {
        return dustDensity;
    }

    public void setDustDensity(String dustDensity) {
        this.dustDensity = dustDensity;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getOnOff() {
        return onOff;
    }

    public void setOnOff(String onOff) {
        this.onOff = onOff;
    }
}