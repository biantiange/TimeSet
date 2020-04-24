package com.timeset.photo.entity;

/**
 * @ClassName photo
 * @Description
 * @Author SkySong
 * @Date 2020-04-21 16:32
 */
public class Photo {
    private int id;
    private int ablbumId;
    private String pdescribe;
    private String ptime;
    private String palce;
    private String path;
    private String identify;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAblbumId() {
        return ablbumId;
    }

    public void setAblbumId(int ablbumId) {
        this.ablbumId = ablbumId;
    }

    public String getPdescribe() {
        return pdescribe;
    }

    public void setPdescribe(String pdescribe) {
        this.pdescribe = pdescribe;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getPalce() {
        return palce;
    }

    public void setPalce(String palce) {
        this.palce = palce;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public Photo(int id, int ablbumId, String pdescribe, String ptime, String palce, String path, String identify) {
        this.id = id;
        this.ablbumId = ablbumId;
        this.pdescribe = pdescribe;
        this.ptime = ptime;
        this.palce = palce;
        this.path = path;
        this.identify = identify;
    }
}
