package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class LogVO implements Serializable {

    ArrayList<AirconditionerVO> airconditionerList = new ArrayList<AirconditionerVO>();
    ArrayList <AirpurifierVO> airpurifierList = new ArrayList<AirpurifierVO>();
    ArrayList <DoorVO> doorList = new ArrayList<DoorVO>();
    ArrayList <LightVO> lightList = new ArrayList<LightVO>();
    ArrayList <WindowVO> windowList = new ArrayList<WindowVO>();

    public ArrayList<AirconditionerVO> getAirconditionerList() {
        return airconditionerList;
    }
    public void setAirconditionerList(ArrayList<AirconditionerVO> airconditionerList) {
        this.airconditionerList = airconditionerList;
    }
    public ArrayList<AirpurifierVO> getAirpurifierList() {
        return airpurifierList;
    }
    public void setAirpurifierList(ArrayList<AirpurifierVO> airpurifierList) {
        this.airpurifierList = airpurifierList;
    }
    public ArrayList<DoorVO> getDoorList() {
        return doorList;
    }
    public void setDoorList(ArrayList<DoorVO> doorList) {
        this.doorList = doorList;
    }

    public ArrayList<LightVO> getLightList() {
        return lightList;
    }
    public void setLightList(ArrayList<LightVO> lightList) {
        this.lightList = lightList;
    }

    public ArrayList<WindowVO> getWindowList() {
        return windowList;
    }
    public void setWindowList(ArrayList<WindowVO> windowList) {
        this.windowList = windowList;
    }


}
