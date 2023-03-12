package amit.myapp.keeper.Model.Messages;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import amit.myapp.keeper.Model.Users.AppUser;

@Entity
public class Message implements Serializable {

    private String publisherId = "";
    private String publisherName = "";

    @PrimaryKey
    @NonNull
    private String id = "";
    private String title="";

    private String content="";
    private String date = "";


    public Message() {}
    public static final String COLLECTION = "messages";
    static final String TITLE = "title";
    static final String CONTENT = "content";
    static final String PUBLISHER_NAME = "publisherName";
    static final String PUBLISHER_ID = "publisherId";
    static final String MESSAGE_ID = "id";

    static final String DATE = "date";

    public Message(String content, String title, AppUser publisher){
        this.content = content; this.title = title; this.publisherName = publisher.getFullName(); this.publisherId = publisher.getId();
    }

    public Message(String content, String title, String publisherName, String publisherId, String id){
        this.content = content; this.title = title; this.publisherName = publisherName; this.publisherId = publisherId;
        this.date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        this.id = id;
    }

    public Message(String content, String title, String publisherName, String publisherId, String date, String id){
        this.content = content; this.title = title; this.publisherName = publisherName; this.publisherId = publisherId;
        this.date = date; this.id = id;
    }

    public static Message fromJson(Map<String,Object> json){//, String id){
        String content = (String)json.get(CONTENT);
        String title = (String)json.get(TITLE);
        String publisherId = (String) json.get(PUBLISHER_ID);
        String publisherName = (String) json.get(PUBLISHER_NAME);
        String date = (String) json.get(DATE);
        String id = (String) json.get(MESSAGE_ID);

        Message message = new Message(content, title, publisherName, publisherId, date, id);
        return message;
    }

    public static Long getLocalLastUpdate() {
        return new Long(0);
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(CONTENT, getContent());
        json.put(TITLE, getTitle());
        json.put(PUBLISHER_ID, getPublisherId());
        json.put(PUBLISHER_NAME, getPublisherName());
        json.put(DATE, getDate());
        json.put(MESSAGE_ID, getId());
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
