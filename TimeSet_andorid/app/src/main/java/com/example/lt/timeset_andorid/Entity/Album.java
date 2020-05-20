package com.example.lt.timeset_andorid.Entity;

/**
 * 王天
 */
public class Album {
    private int id;
    private int useId;
    private String theme;
    private String albumName;
    private String albumPic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUseId() {
        return useId;
    }

    public void setUseId(int useId) {
        this.useId = useId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumPic() {
        return albumPic;
    }

    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", useId=" + useId +
                ", theme='" + theme + '\'' +
                ", albumName='" + albumName + '\'' +
                ", albumPic='" + albumPic + '\'' +
                '}';
    }
}
