package vn.edu.usth.test.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Podcast implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("rss")
    private String rss;

    @SerializedName("link")
    private String link;

    @SerializedName("audio")
    private String audio;

    @SerializedName("image")
    private String image;

    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("pub_date_ms")
    private long pubDateMs;

    @SerializedName("title_original")
    private String titleOriginal;

    @SerializedName("listennotes_url")
    private String listenNotesUrl;

    @SerializedName("audio_length_sec")
    private int audioLengthSec;

    @SerializedName("explicit_content")
    private boolean explicitContent;

    @SerializedName("description_original")
    private String description;

    // Constructor
    public Podcast(String id, String rss, String link, String audio, String image,
                   String thumbnail, long pubDateMs, String titleOriginal, String listenNotesUrl, int audioLengthSec,
                   boolean explicitContent, String description) {
        this.id = id;
        this.rss = rss;
        this.link = link;
        this.audio = audio;
        this.image = image;
        this.thumbnail = thumbnail;
        this.pubDateMs = pubDateMs;
        this.titleOriginal = titleOriginal;
        this.listenNotesUrl = listenNotesUrl;
        this.audioLengthSec = audioLengthSec;
        this.explicitContent = explicitContent;
        this.description = description;
    }

    //Getters and setters for each field
    public String getId() { return id;  }
    public void setId(String id) {  this.id = id;   }

    public String getRss() { return rss;}
    public void setRss(String rss) { this.rss = rss;}

    public String getLink() {return link;}
    public void setLink(String link) {this.link = link;}

    public String getAudio() {return audio;}
    public void setAudio(String audio) {this.audio = audio;}

    public String getImage() { return image;}
    public void setImage(String image) {this.image = image;}

    public String getThumbnail() {return thumbnail;}
    public void setThumbnail(String thumbnail) {this.thumbnail = thumbnail;}

    public long getPubDateMs() {return pubDateMs;}
    public void setPubDateMs(long pubDateMs) {this.pubDateMs = pubDateMs;}

    public String getTitle() {return titleOriginal;}
    public void setTitle(String titleOriginal) {this.titleOriginal = titleOriginal;}

    public String getListenNotesUrl() {return listenNotesUrl;}
    public void setListenNotesUrl(String listenNotesUrl) {this.listenNotesUrl = listenNotesUrl;}

    public int getAudioLengthSec() {return audioLengthSec;}
    public void setAudioLengthSec(int audioLengthSec) {this.audioLengthSec = audioLengthSec;}

    public boolean isExplicitContent() {return explicitContent;}
    public void setExplicitContent(boolean explicitContent) {this.explicitContent = explicitContent;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
}
