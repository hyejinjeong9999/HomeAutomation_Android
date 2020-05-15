package model;

import java.io.Serializable;

public  class alarmVO implements Serializable{
    String id = "0";
    String time = "0";

    public alarmVO(String id, String time) {
        this.id = id;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}