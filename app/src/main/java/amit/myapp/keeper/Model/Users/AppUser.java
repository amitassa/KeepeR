package amit.myapp.keeper.Model.Users;

import java.util.HashMap;
import java.util.Map;

public class AppUser {
    private String fullName;
    private String id;
    private Integer role;

    static final String COLLECTION = "roles";
    static final String FULLNAME = "name";
    static final String ID = "id";
    static final String ROLE = "role";


    public AppUser() {}

    public AppUser(String fullName, String id, Integer role){
        this.fullName = fullName; this.role = role; this.id = id;
    }

    public static AppUser fromJson(Map<String,Object> json){
        String id = (String)json.get(ID);
        String fullName = (String)json.get(FULLNAME);
        Integer role = (Integer) json.get(ROLE);
        AppUser appUser = new AppUser(fullName, id, role);
        return appUser;
    }

    public Map<String,Object> toJson(){
        Map<String, Object> json = new HashMap<>();
        json.put(ID, getId());
        json.put(FULLNAME, getFullName());
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
}
