package amit.myapp.keeper.Model.Users;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AppUser implements Serializable {
    private String fullName;
    private String id;
    private Integer role;
    private String email;

    private String avatarurl;

    public static final String COLLECTION = "AppUser";
    public static final String FULLNAME = "fullName";
    public static final String ID = "id";
    public static final String ROLE = "role";
    public static final String EMAIL = "email";
    public static final String AVATAR_URL = "avatarurl";




    public AppUser() {}

    public AppUser(String fullName, String id, String email, Integer role, String avatarurl){
        this.fullName = fullName; this.role = role; this.id = id; this.email = email;
        this.avatarurl = avatarurl;
    }

    public static AppUser fromJson(Map<String,Object> json){
        String id = (String)json.get(ID);
        String fullName = (String)json.get(FULLNAME);
        Integer role = (Integer) json.get(ROLE);
        String email = (String)json.get(EMAIL);
        String avatar_url = (String) json.get(AVATAR_URL);
        AppUser appUser = new AppUser(fullName, id, email, role, avatar_url);
        return appUser;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(FULLNAME, getFullName());
        json.put(EMAIL, getEmail());
        json.put(ROLE, getRole());
        json.put(AVATAR_URL, getAvatarUrl());
        return json;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return this.avatarurl;
    }

    public void setAvatarUrl(String avatarurl) {
        this.avatarurl = avatarurl;
    }
}
