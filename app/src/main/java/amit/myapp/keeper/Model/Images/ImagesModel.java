package amit.myapp.keeper.Model.Images;

import android.graphics.Bitmap;

import amit.myapp.keeper.Model.FirebaseModel;
import amit.myapp.keeper.Model.Users.AppUser;
import amit.myapp.keeper.Model.Users.AppUserModel;

public class ImagesModel {
    private static final ImagesModel _instance = new ImagesModel();
    private FirebaseModel firebaseModel = new FirebaseModel();

    public static ImagesModel instance(){
        return _instance;
    }

    private ImagesModel(){
    }

    public interface Listener<T>{
        void onComplete(T data);
    }

    public void uploadUserProfileImage(AppUser user, Bitmap imageBitmap, Listener<String> listener){
        firebaseModel.uploadUserProfileImage(user.getId(), imageBitmap, listener);
    }
}
