package com.example.shaply_app.Lists;

public class Track {
    private String trackName;
    private String artistName;

    public Track(String trackName, String artistName) {
        this.trackName = trackName;
        this.artistName = artistName;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getArtistName() {
        return artistName;
    }
}
