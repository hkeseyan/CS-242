package edu.ucr.cs242.common.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class HttpHeaders implements Serializable {

    @XmlElement(name = "header")
    private List<HttpHeader> headers;

    public HttpHeaders() {
    }

    public List<HttpHeader> getHeaders() {
        if(headers == null) {
            headers = new ArrayList<>();
        }
        return headers;
    }

    public void setHeaders(List<HttpHeader> headers) {
        this.headers = headers;
    }
}
