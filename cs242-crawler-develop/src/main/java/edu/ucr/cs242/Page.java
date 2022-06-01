package edu.ucr.cs242;

import java.io.Serializable;

public class Page implements Serializable {

    private String url;
    private String title;
    private int depth;
    private String data;
    private String pi;

    public Page(String url, String title, int depth, String data, String pi) {
        this.url = url;
        this.title = title;
        this.depth = depth;
        this.data = data;
        this.pi = pi;
    }

    public Page() {
    }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public int getDepth() { return depth; }

    public void setDepth(int depth) { this.depth = depth; }

    public String getData() { return data; }

    public void setData(String data) { this.data = data; }

    public String getPi() { return pi; }

    public void setPi(String pi) { this.pi = pi; }


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
