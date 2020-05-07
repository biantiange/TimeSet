package com.example.lt.timeset_andorid.Entity;
public class Photo {
    private int id;
    private int albumId;
    private int userId;
    private String pdescribe;
    private String ptime;
    private String place;
    private String path;
    private String identify;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", albumId=" + albumId +
                ", userId=" + userId +
                ", pdescribe='" + pdescribe + '\'' +
                ", ptime='" + ptime + '\'' +
                ", palce='" + place + '\'' +
                ", path='" + path + '\'' +
                ", identify='" + identify + '\'' +
                '}';
    }

    public Photo(int albumId, int userId, String pdescribe, String ptime, String palce, String path, String identify) {
        this.albumId = albumId;
        this.userId = userId;
        this.pdescribe = pdescribe;
        this.ptime = ptime;
        this.place = palce;
        this.path = path;
        this.identify = identify;
    }

    public Photo() { }
}
