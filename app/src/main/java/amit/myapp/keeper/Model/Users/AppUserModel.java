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

    public void registerUser(String email, String password, String fullName, String ID, int role, RegisterUserListener listener){
        firebaseModel.registerUser(email, password, fullName, ID, role, listener);
    }

    public interface LoginUserListener{
        void onComplete();
        void onFailure();
    }

    public void loginUser(String email, String password, LoginUserListener listener){
        firebaseModel.loginUser(email, password, listener);
    }

    public interface getCurrentUserListener{
        void onComplete(AppUser user);
        void onFailure();
    }

    public void getCurrentUser(getCurrentUserListener listener){
        firebaseModel.getCurrentUser(listener);
    }

    public interface logOutUserListener{
        void onComplete();
    }

    public void logOutUser(logOutUserListener listener){
        firebaseModel.logOutUser(listener);
    }

    public interface BasicListener{
        void onComplete();
    }

    public void updateUser(AppUser user, BasicListener listener){
        firebaseModel.updateUser(user, listener);
    }

}
