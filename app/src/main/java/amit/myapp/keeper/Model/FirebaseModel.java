package amit.myapp.keeper.Model;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import amit.myapp.keeper.Model.Messages.Message;
import amit.myapp.keeper.Model.Messages.MessagesModel;
import amit.myapp.keeper.Model.Roles.Role;
import amit.myapp.keeper.Model.Users.AppUser;
import amit.myapp.keeper.Model.Users.AppUserModel;


public class FirebaseModel {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference appUserDatabaseReference;

    public FirebaseModel(){
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        appUserDatabaseReference = firebaseDatabase.getReference("AppUser");
    }

    public void getAllMessages(MessagesModel.GetAllMessagesListener callback){
        db.collection(Message.COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Message> messageList = new LinkedList<>();
                if (task.isSuccessful()){
                    QuerySnapshot jsonsList = task.getResult();
                    for (DocumentSnapshot json: jsonsList){
                        String id = json.getId();
                        Message message = Message.fromJson(json.getData(), id);
                        messageList.add(message);
                    }
                }
                callback.onComplete(messageList);
            }
        });
    }

    public void addMessage(Message message, MessagesModel.AddMessageListener listener){
        db.collection(Message.COLLECTION).document().set(message.toJson()).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {listener.onComplete();}});
    }

    public void deleteMessage(Message message, MessagesModel.DeleteMessageListener listener){
        db.collection(Message.COLLECTION).document(message.getId()).delete().
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onComplete();
                    }
                });
    }

    public void editMessage(Message message, MessagesModel.EditMessageListener listener){
        db.collection(Message.COLLECTION).document(message.getId()).set(message.toJson())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onComplete();
                    }
                });
    }

    public Boolean registerUser(String email, String password, String fullName, String ID, int role, AppUserModel.RegisterUserListener listener){
        final Boolean[] isSuccessful = {false};
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    isSuccessful[0] = true;
                    AppUser user = new AppUser(fullName, ID, email, role);
                    appUserDatabaseReference.child(mAuth.getCurrentUser().getUid()).setValue(user).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    listener.onComplete();
                                }
                            });
                };
            }
        });

//        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//            @Override
//            public void onSuccess(AuthResult authResult) {
//                Log.d("model", "onSuccess: succeeded");
//                listener.onComplete();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("model", "onFailure: failed");
//            }
//        });

        return isSuccessful[0];
    }

    public Boolean loginUser(String email, String password, AppUserModel.LoginUserListener listener){
        final Boolean[] isSuccessful = {false};
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    isSuccessful[0] = true;
                    listener.onComplete();
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure();
                    }
                });
        return isSuccessful[0];
    }


}
