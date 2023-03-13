package amit.myapp.keeper.Model.Incidents;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import amit.myapp.keeper.MyApplication;

@Entity
public class Incident implements Serializable {

    private String publisherId = "";
    private String publisherName = "";

    @PrimaryKey
    @NonNull
    private String id = "";
    private String title="";

    private String content="";
    private String photourl="";
    private Long date;
    private Boolean isDeleted = false;

    public Incident(String id, String title, String publisherId,
                    String publisherName, String photourl, String content,
                    Long date, Boolean isDeleted){
        this.id = id; this.title = title; this.publisherId = publisherId;
        this.content = content; this.photourl = photourl; this.date = date;
        this.publisherName = publisherName; this.isDeleted = isDeleted;
    }

   @Ignore
    public Incident(String id, String title, String publisherId,
                    String publisherName, String photourl, String content,
                    Long date){
        this.id = id; this.title = title; this.publisherId = publisherId;
        this.content = content; this.photourl = photourl; this.date = date;
        this.publisherName = publisherName; this.isDeleted = false;
    }

    @Ignore
    public Incident(String id, String title, String publisherId,
                    String publisherName, String photourl, String content){
        this.id = id; this.title = title; this.publisherId = publisherId;
        this.content = content; this.photourl = photourl;
        this.publisherName = publisherName;

    }

    public static final String COLLECTION = "incidents";
    static final String TITLE = "title";
    static final String CONTENT = "content";
    static final String PUBLISHER_NAME = "publisherName";
    static final String PUBLISHER_ID = "publisherId";
    static final String INCIDENT_ID = "id";
    static final String PHOTO_URL = "photourl";
    public static final String DATE = "date";
    public static final String IS_DELETED = "IsDeleted";

    public static final String LOCAL_LATEST_DATE = "incidents_local_latest_date";


    public static Incident fromJson(Map<String, Object> json){
        String content = (String)json.get(CONTENT);
        String title = (String)json.get(TITLE);
        String publisherId = (String) json.get(PUBLISHER_ID);
        String publisherName = (String) json.get(PUBLISHER_NAME);
        //String date = (String) json.get(DATE);
        String id = (String) json.get(INCIDENT_ID);
        // Timestamp is firebase module. Change if firebase changed as DB!
        Timestamp time = (Timestamp) json.get(DATE);
        Long date = time.getSeconds();
        String photo_url = (String) json.get(PHOTO_URL);
        Boolean isDeleted = (Boolean) json.get(IS_DELETED);

        Incident incident = new Incident(id, title, publisherId, publisherName,
                photo_url,content, date, isDeleted);
        return incident;

    }

    public Map<String, Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(CONTENT, getContent());
        json.put(TITLE, getTitle());
        json.put(PUBLISHER_ID, getPublisherId());
        json.put(PUBLISHER_NAME, getPublisherName());
        //json.put(DATE, getDate());
        // Change if firebase changed as DB!
        json.put(DATE, FieldValue.serverTimestamp());
        json.put(INCIDENT_ID, getId());
        json.put(PHOTO_URL, getPhotourl());
        json.put(IS_DELETED, getDeleted());
        return json;
    }

    public static Long getLocalLatestDate() {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sharedPref.getLong(LOCAL_LATEST_DATE, 0);
    }

    public static void setLocalLatestDate(Long time) {
        SharedPreferences sharedPref = MyApplication.getMyContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(LOCAL_LATEST_DATE,time);
        editor.commit();
    }


    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
