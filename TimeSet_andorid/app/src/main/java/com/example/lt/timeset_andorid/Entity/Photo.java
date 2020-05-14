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
    private String Latitude;//纬度
    private String longitude;//经度
    private String district;//区县
    private String city;//市
    private String province;//省

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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
                ", albumId=" + albumId +
                ", userId=" + userId +
                ", pdescribe='" + pdescribe + '\'' +
                ", ptime='" + ptime + '\'' +
                ", place='" + place + '\'' +
                ", path='" + path + '\'' +
                ", identify='" + identify + '\'' +
                ", Latitude='" + Latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", district='" + district + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                '}';
    }

    public Photo(int albumId, int userId, String pdescribe, String ptime, String place, String path, String identify, String latitude, String longitude, String district, String city, String province) {
        this.albumId = albumId;
        this.userId = userId;
        this.pdescribe = pdescribe;
        this.ptime = ptime;
        this.place = place;
        this.path = path;
        this.identify = identify;
        Latitude = latitude;
        this.longitude = longitude;
        this.district = district;
        this.city = city;
        this.province = province;
    }

    public Photo() { }
}
