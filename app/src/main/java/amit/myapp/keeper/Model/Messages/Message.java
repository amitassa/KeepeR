package amit.myapp.keeper.Model.Messages;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import amit.myapp.keeper.Model.Users.AppUser;


public class Message implements Serializable {

    public static final String COLLECTION = "messages";
    static final String TITLE = "title";
    static final String CONTENT = "content";
    static final String PUBLISHER_NAME = "publisherName";
    static final String PUBLISHER_ID = "publisherId";
    static final String DATE = "date";

    private String content="";
    private String title="";
    private String publisherName = "";
    private String publisherId = "";
    private String date = "";
    private String id = "";

    public Message() {}

    public Message(String content, String title, AppUser publisher){
        this.content = content; this.title = title; this.publisherName = publisher.getFullName(); this.publisherId = publisher.getId();
    }

    public Message(String content, String title, String publisherName, String publisherId){
        this.content = content; this.title = title; this.publisherName = publisherName; this.publisherId = publisherId;
        this.date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    public Message(String content, String title, String publisherName, String publisherId, String date, String id){
        this.content = content; this.title = title; this.publisherName = publisherName; this.publisherId = publisherId;
        this.date = date; this.id = id;
    }

    public static Message fromJson(Map<String,Object> json, String id){
        String content = (String)json.get(CONTENT);
        String title = (String)json.get(TITLE);
        String publisherId = (String) json.get(PUBLISHER_ID);
        String publisherName = (String) json.get(PUBLISHER_NAME);
        String date = (String) json.get(DATE);

        Message message = new Message(content, title, publisherName, publisherId, date, id);
        return message;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(CONTENT, getContent());
        json.put(TITLE, getTitle());
        json.put(PUBLISHER_ID, getPublisherId());
        json.put(PUBLISHER_NAME, getPublisherName());
        json.put(DATE, getDate());
        return json;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
