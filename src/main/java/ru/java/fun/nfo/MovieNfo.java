package ru.java.fun.nfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * <a href="https://kodi.wiki/view/NFO_files/Movies">Format</a>
 */
@XmlRootElement(name = "movie")
@XmlAccessorType(XmlAccessType.FIELD)
public class MovieNfo extends BaseNfo {

    @XmlElement(name = "country")
    private String country;

    @XmlElement(name = "showlink")
    private String showLink;

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

    public String getShowLink() {
        return showLink;
    }

    public void setShowLink(String showLink) {
        this.showLink = showLink;
    }

}