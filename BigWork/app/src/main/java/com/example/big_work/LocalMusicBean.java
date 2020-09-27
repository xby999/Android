package com.example.big_work;

public class LocalMusicBean {
    private String id;         //歌曲id
    private String song;       //歌名
    private String singer;     //歌手
    private String album;      //专辑
    private String duration;   //时长
    private String path;       //存储路径

    public LocalMusicBean() {
    }

    public LocalMusicBean(String id, String song, String singer, String album, String duration, String path) {
        this.id = id;
        this.song = song;
        this.singer = singer;
        this.album = album;
        this.duration = duration;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
