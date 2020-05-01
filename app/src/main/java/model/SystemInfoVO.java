package model;

import android.widget.ImageView;

public class SystemInfoVO {
    int imageView;
    String title = "";
    String situation="";
    int viewType;

    public SystemInfoVO(int imageView, String title, String situation,int viewType) {
        this.imageView=imageView;
        this.title = title;
        this.situation = situation;
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
}
