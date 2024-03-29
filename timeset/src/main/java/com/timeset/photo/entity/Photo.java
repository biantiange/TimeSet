package com.timeset.photo.entity;

/**
 * @ClassName photo
 * @Description
 * @Author SkySong
 * @Date 2020-04-21 16:32
 */
public class Photo {
    private int id;
    private int album_id;
    private int user_id;
    private String pdescribe;
    private String ptime;
    private String place;
    private String path;
    private String identify;
    private String longitude;
    private String latitude;
    private String district;
    private String city;
    private String province;

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
        return album_id;
    }

    public void setAlbumId(int albumId) {
        this.album_id = albumId;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int userId) {
        this.user_id = userId;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", albumId=" + album_id +
                ", userId=" + user_id +
                ", pdescribe='" + pdescribe + '\'' +
                ", ptime='" + ptime + '\'' +
                ", place='" + place + '\'' +
                ", path='" + path + '\'' +
                ", identify='" + identify + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", district='" + district + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                '}';
    }

    public Photo() { }
}
