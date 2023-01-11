package amit.myapp.keeper.Model.Messages;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import amit.myapp.keeper.Model.Users.AppUser;


public class Message {

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

    public Message() {}

    public Message(String content, String title, AppUser publisher){
        this.content = content; this.title = title; this.publisherName = publisher.getFullName(); this.publisherId = publisher.getId();
    }

    public Message(String content, String title, String publisherName, String publisherId){
        this.content = content; this.title = title; this.publisherName = publisherName; this.publisherId = publisherId;
        this.date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    }

    public Message(String content, String title, String publisherName, String publisherId, String date){
        this.content = content; this.title = title; this.publisherName = publisherName; this.publisherId = publisherId;
    }

    public static Message fromJson(Map<String,Object> json){
        String content = (String)json.get(CONTENT);
        String title = (String)json.get(TITLE);
        String publisherId = (String) json.get(PUBLISHER_ID);
        String publisherName = (String) json.get(PUBLISHER_NAME);

        Message message = new Message(content, title, publisherName, publisherId);
        return message;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(CONTENT, getContent());
        json.put(TITLE, getTitle());
        json.put(PUBLISHER_ID, getPublisherId());
        json.put(PUBLISHER_NAME, getPublisherName());
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
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
