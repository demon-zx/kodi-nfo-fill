package ru.java.fun.nfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * <a href="https://kodi.wiki/view/NFO_files/TV_shows">Format</a>
 */
@XmlRootElement(name = "tvshow")
@XmlAccessorType(XmlAccessType.FIELD)
public class TVShowNfo extends BaseNfo {

    @XmlElement(name = "season")
    private int seasonsCount;
    @XmlElement(name = "episode")
    private int episodesCount;

    @XmlElement(name = "status")
    private Status status;

    // <episodeguide> //https://forum.kodi.tv/showthread.php?tid=370489
//    <namedseason number=""></namedseason>


    public int getSeasonsCount() {
        return seasonsCount;
    }

    public void setSeasonsCount(int seasonsCount) {
        this.seasonsCount = seasonsCount;
    }

    public int getEpisodesCount() {
        return episodesCount;
    }

    public void setEpisodesCount(int episodesCount) {
        this.episodesCount = episodesCount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        Continuing, Ended
    }

}