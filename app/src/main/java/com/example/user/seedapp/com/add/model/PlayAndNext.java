package com.example.user.seedapp.com.add.model;

/**
 * Created by LacNoito on 3/17/2015.
 */
public class PlayAndNext {
    private String event_type;
    private String actual_date_time;
    private String link_title;
    private String songTitle;
    private String songCover;
    private String linkTitle;
    private String linkCover;
    private String linkUrl;
    private String nowLyric;
    private String nowMv;
    private String nowAuthor;
    private String nowAuthor2;
    private String nowAuthor3;
    private String artistName;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getActual_date_time() {
        return actual_date_time;
    }

    public void setActual_date_time(String actual_date_time) {
        this.actual_date_time = actual_date_time;
    }

    public String getLink_title() {
        return link_title;
    }

    public void setLink_title(String link_title) {
        this.link_title = link_title;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongCover() {
        return songCover;
    }

    public void setSongCover(String songCover) {
        this.songCover = songCover;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkCover() {
        return linkCover;
    }

    public void setLinkCover(String linkCover) {
        this.linkCover = linkCover;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getNowLyric() {
        return nowLyric;
    }

    public void setNowLyric(String nowLyric) {
        this.nowLyric = nowLyric;
    }

    public String getNowMv() {
        return nowMv;
    }

    public void setNowMv(String nowMv) {
        this.nowMv = nowMv;
    }

    public String getNowAuthor() {
        return nowAuthor;
    }

    public void setNowAuthor(String nowAuthor) {
        this.nowAuthor = nowAuthor;
    }

    public String getNowAuthor2() {
        return nowAuthor2;
    }

    public void setNowAuthor2(String nowAuthor2) {
        this.nowAuthor2 = nowAuthor2;
    }

    public String getNowAuthor3() {
        return nowAuthor3;
    }

    public void setNowAuthor3(String nowAuthor3) {
        this.nowAuthor3 = nowAuthor3;
    }

    @Override
    public boolean equals(Object o) {
        PlayAndNext playAndNext = (PlayAndNext) o;
        if(((songTitle == null && playAndNext.getSongTitle() == null) || (songTitle != null && songTitle.equals(playAndNext.getSongTitle())))
                && (((artistName == null && playAndNext.getArtistName() == null)) || (artistName != null && artistName.equals(playAndNext.getArtistName())))){
            return true;
        }else
            return false;
    }
}
