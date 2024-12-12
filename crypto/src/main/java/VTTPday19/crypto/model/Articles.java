package VTTPday19.crypto.model;

import java.io.StringReader;
import java.util.Date;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Articles {
    private String id;
    private Date published_on;
    private String title;
    private String url;
    private String imageurl;
    private String body;
    private String tags;
    private String categories;

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public Date getPublished_on() {return published_on;}
    public void setPublished_on(Date published_on) {this.published_on = published_on;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}
    public String getImageurl() {return imageurl;}
    public void setImageurl(String imageurl) {this.imageurl = imageurl;}
    public String getBody() {return body;}
    public void setBody(String body) {this.body = body;}
    public String getTags() {return tags;}
    public void setTags(String tags) {this.tags = tags;}
    public String getCategories() {return categories;}
    public void setCategories(String categories) {this.categories = categories;}

    @Override
    public String toString() {
        return "Articles [id=" + id + ", published_on=" + published_on + ", title=" + title + ", url=" + url
                + ", imageurl=" + imageurl + ", body=" + body + ", tags=" + tags + ", categories=" + categories + "]";
    }

    public static JsonObject getJsonObj(String jsonStr){
        JsonReader reader = Json.createReader(new StringReader(jsonStr));
        JsonObject jsonObj = reader.readObject();
        return jsonObj;
    }


    public static JsonObject toJson(Articles art){
        JsonObject jObj = Json.createObjectBuilder()
            .add("id", art.getId())
            .add("published_on", dateToepochSeconds(art.getPublished_on()))
            .add("title", art.getTitle())
            .add("url", art.getUrl())
            .add("imageurl", art.getImageurl())
            .add("body", art.getBody())
            .add("tags", art.getTags())
            .add("categories", art.getCategories())
            .build();
        return jObj;
    }



    public static Date epochSecondsToDate(String epoch){
        long epochmilli = Long.valueOf(epoch) * 1000;
        Date date = new Date(epochmilli);
        return date;
    }

    public static String dateToepochSeconds(Date publishedDate){
        long epochMilli = publishedDate.getTime();
        String epochSeconds = String.valueOf(epochMilli/1000);
        return epochSeconds;
    }


}
