package model;

import java.io.Serializable;

public class SystemInfoVO implements Serializable {
    private int imageView;
    private String title = "";
    private String situation="";
    private int viewType = 0;

    public SystemInfoVO(int imageView, String title, String situation,int viewType) {
        this.imageView=imageView;
        this.title = title;
        this.situation = situation;
        this.viewType = viewType;
    }
    public SystemInfoVO(int imageView, String title,int viewType) {
        this.imageView=imageView;
        this.title = title;
        this.viewType = viewType;
    }
    public SystemInfoVO(String title,int viewType) {
        this.title = title;
        this.viewType = viewType;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
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

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
