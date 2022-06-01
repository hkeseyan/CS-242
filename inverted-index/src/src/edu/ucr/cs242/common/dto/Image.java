package edu.ucr.cs242.common.dto;


import javax.xml.bind.annotation.*;
import java.io.Serializable;

@XmlRootElement(name = "Image")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "Image")
public class Image implements Serializable {

    @XmlAttribute(name = "src")
    private String src;
    @XmlAttribute(name = "alt")
    private String alt;
    @XmlAttribute(name = "data")
    private String data;
    @XmlAttribute(name = "text")
    private String text;

    public Image() {
    }

    public Image(String src, String alt) {
        this.src = src;
        this.alt = alt;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
