package ru.java.fun.nfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <a href="https://kodi.wiki/view/NFO_files/Episodes">Format</a>
 */
@XmlRootElement(name = "episodedetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class EpisodeNfo extends BaseNfo {

}