package com.olgaz.testnytimesmostpopular.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class News {
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("adx_keywords")
    @Expose
    private String adxKeywords;
    @SerializedName("column")
    @Expose
    private Object column;
    @SerializedName("section")
    @Expose
    private String section;
    @SerializedName("byline")
    @Expose
    private String byline;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("abstract")
    @Expose
    private String description;
    @SerializedName("published_date")
    @Expose
    private String publishedDate;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("id")
    @Expose
    private long id;
    @SerializedName("asset_id")
    @Expose
    private long assetId;
    @SerializedName("views")
    @Expose
    private int views;
//    @SerializedName("des_facet")
//    @Expose
//    private List<String> desFacet = null;
//    @SerializedName("org_facet")
//    @Expose
//    private List<String> orgFacet = null;
//    @SerializedName("per_facet")
//    @Expose
//    private List<String> perFacet = null;
//    @SerializedName("geo_facet")
//    @Expose
//    private List<String> geoFacet = null;
    @SerializedName("media")
    @Expose
    private List<Media> media = null;
    @SerializedName("uri")
    @Expose
    private String uri;

    public News(Long id, String url, String section, String title, String description, String publishedDate, String source, List<Media> media) {
        this.id = id;
        this.url = url;
        this.section = section;
        this.title = title;
        this.description = description;
        this.publishedDate = publishedDate;
        this.source = source;
        this.media = media;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAdxKeywords() {
        return adxKeywords;
    }

    public void setAdxKeywords(String adxKeywords) {
        this.adxKeywords = adxKeywords;
    }

    public Object getColumn() {
        return column;
    }

    public void setColumn(Object column) {
        this.column = column;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getByline() {
        return byline;
    }

    public void setByline(String byline) {
        this.byline = byline;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String _abstract) {
        this.description = _abstract;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

//    public List<String> getDesFacet() {
//        return desFacet;
//    }
//
//    public void setDesFacet(List<String> desFacet) {
//        this.desFacet = desFacet;
//    }
//
//    public List<String> getOrgFacet() {
//        return orgFacet;
//    }
//
//    public void setOrgFacet(List<String> orgFacet) {
//        this.orgFacet = orgFacet;
//    }
//
//    public List<String> getPerFacet() {
//        return perFacet;
//    }
//
//    public void setPerFacet(List<String> perFacet) {
//        this.perFacet = perFacet;
//    }
//
//    public List<String> getGeoFacet() {
//        return geoFacet;
//    }
//
//    public void setGeoFacet(List<String> geoFacet) {
//        this.geoFacet = geoFacet;
//    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
