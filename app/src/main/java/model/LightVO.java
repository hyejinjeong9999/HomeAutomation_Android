package model;

import java.io.Serializable;

public class LightVO implements Serializable {

    String LightStatus = "";
    String LightTime = "";

    public String getLightStatus() {
        return LightStatus;
    }

    public void setLightStatus(String lightStatus) {
        LightStatus = lightStatus;
    }

    public String getLightTime() {
        return LightTime;
    }

    public void setLightTime(String lightTime) {
        LightTime = lightTime;
    }

}
