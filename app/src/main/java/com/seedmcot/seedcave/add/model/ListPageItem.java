package com.seedmcot.seedcave.add.model;

/**
 * Created by LacNoito on 3/8/2015.
 */
public class ListPageItem {
    private String event_type;
    private String actual_date_time;
    private String link_title;
    private String songTitle;
    private String linkTitle;
    private String artistName;

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

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
