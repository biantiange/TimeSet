package com.example.lt.timeset_andorid.Entity;

/**
 * 王天
 */
public class Album {
    private int id;
    private int user_id;
    private String theme;
    private String album_name;
    private String album_pic;

    public String getAlbumPic() {
        return album_pic;
    }

    public void setAlbumPic(String albumPic) {
        this.album_pic = albumPic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUseId() {
        return user_id;
    }

    public void setUseId(int useId) {
        this.user_id = useId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getAlbumName() {
        return album_name;
    }

    public void setAlbumName(String albumName) {
        this.album_name = albumName;
    }
}
