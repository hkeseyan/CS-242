package edu.ucr.cs242.common.dto;

import javax.xml.bind.annotation.*;
import java.io.Serializable;


@XmlRootElement(name = "HttpHeader")
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "HttpHeader")
public class HttpHeader implements Serializable {

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "value")
    private String value;

    public HttpHeader() {
    }

    public HttpHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
