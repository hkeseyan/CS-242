package edu.ucr.cs242.common.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
public class OutgoingLinks implements Serializable {

    @XmlElement(name = "link")
    private List<OutgoingLink> links;

    public OutgoingLinks() {
    }

    public List<OutgoingLink> getOutgoingLinks() {
        if(links == null) {
            links = new ArrayList<>();
        }
        return links;
    }

    public void setLinks(List<OutgoingLink> links) {
        this.links = links;
    }
}
