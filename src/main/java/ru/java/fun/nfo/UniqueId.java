package ru.java.fun.nfo;

import javax.xml.bind.annotation.*;

/**
 * The ID from the scraper site.
 * <p>
 * type="" Identifier of the ID. Do not use "default". Examples of identifier include...
 * {@code
 * <uniqueid type="imdb"
 * <uniqueid type="tvdb"
 * <uniqueid type="tmdb"
 * }
 * For non-scraped items, use simple values like "home", "sport", "doco" etc
 * The value can be alpha-numeric eg tt3480556 for imdb, 286668 for tmdb, home001 for home movies, etc
 * {@code
 * default="true" default="false"
 * }
 * <p>
 * Only one uniqueID can be set as default="true", additional uniqueID's must be set as default="false" or ommit the default attribute
 * <p>
 * Example of complete entry...
 * {@code
 * <uniqueid type="tmdb" default="true">
 * <uniqueid type="imdb" default="false"> or <uniqueid type="imdb">
 * }
 */
@XmlRootElement(name = "uniqueid")
@XmlAccessorType(XmlAccessType.FIELD)
public class UniqueId {
    @XmlAttribute
    private String type;
    @XmlAttribute(name = "default")
    private boolean defaultValue;
    @XmlValue
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}