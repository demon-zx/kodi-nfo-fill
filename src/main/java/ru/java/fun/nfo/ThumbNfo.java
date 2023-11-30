package ru.java.fun.nfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "thumb")
@XmlAccessorType(XmlAccessType.FIELD)
public class ThumbNfo {

    @XmlAttribute
    private Aspect aspect;
    @XmlAttribute(required = true)
    private String preview;

    public Aspect getAspect() {
        return aspect;
    }

    public void setAspect(Aspect aspect) {
        this.aspect = aspect;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public enum Aspect {
        banner,
        clearart,
        clearlogo,
        discart,
        keyart,
        landscape,
        poster
    }
}
