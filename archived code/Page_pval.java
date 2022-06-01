package edu.ucr.cs242;

import java.io.Serializable;

public class Page_pval implements Serializable {

    private String url;
    private String title;
    private int depth;
    private String data;
    private String[] plab;
    private String[] pval;

    public Page_pval(String url, String title, int depth, String data, String[] plab, String[] pval) {
        this.url = url;
        this.title = title;
        this.depth = depth;
        this.data = data;
        this.plab = plab;
        this.pval = pval;
    }

    public Page_pval() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getData() {
        return data;
    }

    public void setData(String data) { this.data = data; }

    public String getPlabel() {
        return plab;
    }

    public void setPlab(String[] plab) {
        this.plab = plab;
    }

    public String getPvalue() {
        return pval;
    }

    public void setPval(String[] pval) {
        this.pval = pval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Page_pval page = (Page_pval) o;

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
