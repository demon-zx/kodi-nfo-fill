package ru.java.fun.nfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "movie")
@XmlAccessorType(XmlAccessType.FIELD)
public class MovieNfo extends BaseNfo {

    @XmlElement(name = "country")
    private String country;

    @XmlElement(name = "tag")
    private List<String> tag;

    @XmlElement(name = "trailer")
    private String trailer;



    @XmlElement(name = "showlink")
    private String showLink;

    @XmlElement(name = "actor")
    private List<ActorNfo> actors;

/*
film collections
<set>
   <name></name>
   <overview></overview>
</set>
 */

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getShowLink() {
        return showLink;
    }

    public void setShowLink(String showLink) {
        this.showLink = showLink;
    }

    public List<ActorNfo> getActors() {
        return actors;
    }

    public void setActors(List<ActorNfo> actors) {
        this.actors = actors;
    }
}