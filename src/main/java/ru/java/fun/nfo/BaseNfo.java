package ru.java.fun.nfo;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class BaseNfo {

    @XmlElement(name = "uniqueid", required = true)
    private UniqueIdNfo uniqueId;

    @XmlElement(name = "title", required = true)
    private String title;

    @XmlElement(name = "originaltitle")
    private String originalTitle;

    @XmlElement(name = "outline")
    private String outline;

    @XmlElement(name = "plot")
    private String plot;

    @XmlElement(name = "tagline")
    private String tagline;

    /**
     * Deprecated, but used in TinyMediaManager
     */
    @XmlElement(name = "year")
    private Integer year;
    /**
     * Premier date: yyyy-MM-dd
     */
    @XmlElement(name = "premiered")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate premiered;

    @XmlElementWrapper(name = "ratings")
    @XmlElement(name = "rating")
    private List<RatingNfo> ratings;

    @XmlElement(name = "userrating")
    private Integer userRating;

    @XmlElement(name = "top250")
    private Integer top250;

    @XmlElement(name = "thumb ")
    private List<ThumbNfo> thumbs;

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

    @XmlElement(name = "tag")
    private List<String> tag;

    @XmlElement(name = "trailer")
    private String trailer;

    @XmlElement(name = "actor")
    private List<ActorNfo> actors;

    public UniqueIdNfo getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UniqueIdNfo uniqueId) {
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

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public LocalDate getPremiered() {
        return premiered;
    }

    public void setPremiered(LocalDate premiered) {
        this.premiered = premiered;
    }

    public List<RatingNfo> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingNfo> ratings) {
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

    public List<ThumbNfo> getThumbs() {
        return thumbs;
    }

    public void setThumbs(List<ThumbNfo> thumbs) {
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

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public List<ActorNfo> getActors() {
        return actors;
    }

    public void setActors(List<ActorNfo> actors) {
        this.actors = actors;
    }
}