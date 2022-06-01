package edu.ucr.cs242.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"docNo", "docHdr", "html", "url", "normaizedUri", "title", "depth", "content", "pi", "images", "outgoingLinks"}
)
@XmlRootElement(
        name = "DOC"
)
public class Page implements Serializable {

    @XmlElement(name = "DOCNO", nillable = true)
    private String docNo;

    @XmlElement(name = "DOCHDR", nillable = true)
    private String docHdr;

    @XmlElement(name = "url", nillable = true)
    private String url;

    @XmlElement(name = "normaizedUri", nillable = true)
    private String normaizedUri;

    @XmlElement(name = "title", nillable = true)
    private String title;

    @XmlElement(name = "depth", nillable = true)
    private int depth;

    @XmlElement(name = "html", nillable = true)
    private String html;

    @XmlElement(name = "content", nillable = true)
    private String content;

    @XmlElement(name = "pi", nillable = true)
    private String pi;

    @XmlElement(name = "images", nillable = true)
    private Images images;

    @XmlElement(name = "outgoingLinks", nillable = true)
    private OutgoingLinks outgoingLinks = new OutgoingLinks();

    public Page(String url, String title, int depth) {
        this.url = url;
        this.title = title;
        this.depth = depth;
    }

    public Page() {
    }

    public void addImage(Image image) {
        getImages().getImages().add(image);
    }

    public Images getImages() {
        if (images == null) {
            images = new Images();
        }
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public OutgoingLinks getOutgoingLinks() {
        return outgoingLinks;
    }

    public void setOutgoingLinks(OutgoingLinks outgoingLinks) {
        this.outgoingLinks = outgoingLinks;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNormaizedUri() {
        return normaizedUri;
    }

    public void setNormaizedUri(String normaizedUri) {
        this.normaizedUri = normaizedUri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPi() {
        return pi;
    }

    public void setPi(String pi) {
        this.pi = pi;
    }

    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }

    public String getDocHdr() {
        return docHdr;
    }

    public void setDocHdr(String docHdr) {
        this.docHdr = docHdr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page page = (Page) o;

        return url.equals(page.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    @Override
    public String toString() {
        return url;
    }
}
