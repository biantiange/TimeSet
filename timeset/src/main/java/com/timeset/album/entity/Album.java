package com.timeset.album.entity;

/**
 * @ClassName album
 * @Description
 * @Author SkySong
 * @Date 2020-04-21 16:32
 */
public class Album {

    private int id;
    private int useId;
    private String theme;
    private String albumName;
    private String albumPic;

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

    public String getAlbumPic() {
        return albumPic;
    }

    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic;
    }

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
}
