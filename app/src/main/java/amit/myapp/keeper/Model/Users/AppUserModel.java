package amit.myapp.keeper.Model.Users;

import android.util.Log;

import amit.myapp.keeper.Model.FirebaseModel;

public class AppUserModel {
    private static final AppUserModel _instance = new AppUserModel();
    private FirebaseModel firebaseModel = new FirebaseModel();

    public static AppUserModel instance(){
        return _instance;
    }

    private AppUserModel(){
    }

    public interface RegisterUserListener{
        void onComplete();
    }

    public Boolean registerUser(String email, String password, String fullName, String ID, int role, RegisterUserListener listener){
        Log.d("d", "registerUser: model ");
        return firebaseModel.registerUser(email, password, fullName, ID, role, listener);
    }



}
