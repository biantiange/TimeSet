package com.timeset.photo.entity;

/**
 * @ClassName photo
 * @Description
 * @Author SkySong
 * @Date 2020-04-21 16:32
 */
public class Photo {
    private int id;
    private int albumId;
    private int userId;
    private String pdescribe;
    private String ptime;
    private String place;
    private String path;
    private String identify;
    private String longitude;
    private String latitude;
    private String district;
    private String city;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", albumId=" + albumId +
                ", userId=" + userId +
                ", pdescribe='" + pdescribe + '\'' +
                ", ptime='" + ptime + '\'' +
                ", place='" + place + '\'' +
                ", path='" + path + '\'' +
                ", identify='" + identify + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", district='" + district + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

    public Photo() { }
}
