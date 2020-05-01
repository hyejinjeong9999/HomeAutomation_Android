package model;

public class SystemInfoVO {
    String title = "";
    String situation="";
    int viewType;

    public SystemInfoVO(String title, String situation,int viewType) {
        this.title = title;
        this.situation = situation;
        this.viewType = viewType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }
}
