package edu.ucr.cs242.common.dto;

import javax.xml.bind.annotation.*;
import java.io.Serializable;


@XmlRootElement(name = "OutgoingLink")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "OutgoingLink")
public class OutgoingLink implements Serializable {

    @XmlAttribute(name = "value")
    private String value;

    public OutgoingLink() {
    }

    public OutgoingLink(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
