package ru.java.fun.nfo;

import javax.xml.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class BaseNfo {

    @XmlElement(name = "uniqueid", required = true)
    private UniqueId uniqueId;

    @XmlElement(name = "title", required = true)
    private String title;

    @XmlElement(name = "originaltitle")
    private String originalTitle;

    @XmlElement(name = "outline")
    private String outline;

    @XmlElement(name = "plot")
    private String plot;

    /**
     * Premier date: yyyy-MM-dd
     */
    @XmlElement(name = "premiered")
    private LocalDate premiered;

    @XmlElementWrapper(name = "ratings")
    @XmlElement(name = "rating")
    private List<Rating> ratings;

    @XmlElement(name = "userrating")
    private Integer userRating;

    @XmlElement(name = "top250")
    private Integer top250;

    @XmlElement(name = "thumb ")
    private List<Thumb> thumbs;

    @XmlElement(name = "runtime")
    private String runtime;

    /**
     * Country specific mpaa rating system.
     * Check with skin author which prefix is required to diplay your country certification flags
     */
    @XmlElement(name = "mpaa")
    private String mpaa;

    @XmlElement(name = "genre")
    private List<String> genre;

    @XmlElement
    private List<String> credits;

    @XmlElement(name = "director")
    private List<String> director;

    @XmlElement(name = "studio")
    private List<String> studio;

    /*
    <actor>
  <name></name>
  <role></role>
  <order></order>
  <thumb></thumb>
</actor>
     */

    public UniqueId getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UniqueId uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOutline() {
        return outline;
    }

    public void setOutline(String outline) {
        this.outline = outline;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public LocalDate getPremiered() {
        return premiered;
    }

    public void setPremiered(LocalDate premiered) {
        this.premiered = premiered;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public Integer getUserRating() {
        return userRating;
    }

    public void setUserRating(Integer userRating) {
        this.userRating = userRating;
    }

    public Integer getTop250() {
        return top250;
    }

    public void setTop250(Integer top250) {
        this.top250 = top250;
    }

    public List<Thumb> getThumbs() {
        return thumbs;
    }

    public void setThumbs(List<Thumb> thumbs) {
        this.thumbs = thumbs;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getMpaa() {
        return mpaa;
    }

    public void setMpaa(String mpaa) {
        this.mpaa = mpaa;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public List<String> getCredits() {
        return credits;
    }

    public void setCredits(List<String> credits) {
        this.credits = credits;
    }

    public List<String> getDirector() {
        return director;
    }

    public void setDirector(List<String> director) {
        this.director = director;
    }

    public List<String> getStudio() {
        return studio;
    }

    public void setStudio(List<String> studio) {
        this.studio = studio;
    }
}