package amit.myapp.keeper.Model.Users;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AppUser implements Serializable {
    private String fullName;
    private String id;
    private Integer role;
    private String email;

    public static final String COLLECTION = "AppUser";
    public static final String FULLNAME = "name";
    public static final String ID = "id";
    public static final String ROLE = "role";
    public static final String EMAIL = "email";



    public AppUser() {}

    public AppUser(String fullName, String id, String email, Integer role){
        this.fullName = fullName; this.role = role; this.id = id; this.email = email;
    }

    public static AppUser fromJson(Map<String,Object> json){
        String id = (String)json.get(ID);
        String fullName = (String)json.get(FULLNAME);
        Integer role = (Integer) json.get(ROLE);
        String email = (String)json.get(EMAIL);
        AppUser appUser = new AppUser(fullName, id, email, role);
        return appUser;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(FULLNAME, getFullName());
        json.put(EMAIL, getEmail());
        json.put(ROLE, getRole());
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
}
