package model;

import java.io.Serializable;

public class LightVO implements Serializable {

    String lightStatus = "";
    String lightTime = "";

    public String getLightStatus() {
        return lightStatus;
    }

    public void setLightStatus(String lightStatus) {
        lightStatus = lightStatus;
    }

    public String getLightTime() {
        return lightTime;
    }

    public void setLightTime(String lightTime) {
        lightTime = lightTime;
    }

}
