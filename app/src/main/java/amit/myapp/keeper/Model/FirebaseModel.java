package amit.myapp.keeper.Model;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import amit.myapp.keeper.Model.Images.ImagesModel;
import amit.myapp.keeper.Model.Incidents.Incident;
import amit.myapp.keeper.Model.Incidents.IncidentsModel;
import amit.myapp.keeper.Model.Messages.Message;
import amit.myapp.keeper.Model.Messages.MessagesModel;
import amit.myapp.keeper.Model.Users.AppUser;
import amit.myapp.keeper.Model.Users.AppUserModel;


public class FirebaseModel {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference appUserDatabaseReference;
    FirebaseStorage storage;
    StorageReference storageRef;

    public FirebaseModel() {
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        appUserDatabaseReference = firebaseDatabase.getReference(AppUser.COLLECTION);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public void getAllMessages(MessagesModel.GetAllMessagesListener callback) {
        db.collection(Message.COLLECTION).get().addOnCompleteListener(task -> {
            List<Message> messageList = new LinkedList<>();
            if (task.isSuccessful()) {
                QuerySnapshot jsonsList = task.getResult();
                for (DocumentSnapshot json : jsonsList) {
                    //String id = json.getId();
                    Message message = Message.fromJson(json.getData());
                    messageList.add(message);
                }
            }
            callback.onComplete(messageList);
        });
    }
    public void getAllMessagesSince(Long Since, MessagesModel.GetAllMessagesListener callback) {
        db.collection(Message.COLLECTION)
                .whereGreaterThanOrEqualTo(Message.DATE, new Timestamp(Since, 0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Message> messageList = new LinkedList<>();
                    if (task.isSuccessful()) {
                        QuerySnapshot jsonsList = task.getResult();
                        for (DocumentSnapshot json : jsonsList) {
                            Message message = Message.fromJson(json.getData());
                            messageList.add(message);
                        }
                    }
                    callback.onComplete(messageList);
                });
    }

    public void addMessage(Message message, MessagesModel.AddMessageListener listener) {
        db.collection(Message.COLLECTION).document(message.getId()).set(message.toJson()).
                addOnCompleteListener(task -> listener.onComplete());
    }

    public void deleteMessage(Message message, MessagesModel.DeleteMessageListener listener) {
        db.collection(Message.COLLECTION).document(message.getId()).delete().
                addOnCompleteListener(task -> listener.onComplete());
    }

    public void editMessage(Message message, MessagesModel.EditMessageListener listener) {
        db.collection(Message.COLLECTION).document(message.getId()).set(message.toJson())
                .addOnCompleteListener(task -> listener.onComplete());
    }

    public void getAllIncidentsSince(Long Since, IncidentsModel.IncidentListener<List<Incident>> callback) {
        db.collection(Incident.COLLECTION)
                .whereGreaterThanOrEqualTo(Message.DATE, new Timestamp(Since, 0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Incident> incidentList = new LinkedList<>();
                    if (task.isSuccessful()) {
                        QuerySnapshot jsonsList = task.getResult();
                        for (DocumentSnapshot json : jsonsList) {
                            Incident incident = Incident.fromJson(json.getData());
                            incidentList.add(incident);
                        }
                    }
                    callback.onComplete(incidentList);
                });
    }

    public void getAllIncidentsForUserSince(String id, Long Since, IncidentsModel.IncidentListener<List<Incident>> callback) {
        db.collection(Incident.COLLECTION)
                .whereEqualTo(Incident.PUBLISHER_ID, id)
                .whereGreaterThanOrEqualTo(Message.DATE, new Timestamp(Since, 0))
                .get()
                .addOnCompleteListener(task -> {
                    List<Incident> incidentList = new LinkedList<>();
                    if (task.isSuccessful()) {
                        QuerySnapshot jsonsList = task.getResult();
                        for (DocumentSnapshot json : jsonsList) {
                            Incident incident = Incident.fromJson(json.getData());
                            incidentList.add(incident);
                        }
                    }
                    callback.onComplete(incidentList);
                });
    }

    public void addIncident(Incident incident, IncidentsModel.IncidentListener<Void> listener) {
        db.collection(Incident.COLLECTION).document(incident.getId()).set(incident.toJson())
                .addOnCompleteListener(task -> listener.onComplete(null));
    }

    public void deleteIncident(Incident incident, IncidentsModel.IncidentListener<Void> listener) {
        db.collection(Incident.COLLECTION).document(incident.getId()).delete()
                .addOnCompleteListener(task -> listener.onComplete(null));
    }

    public void editIncident(Incident incident, IncidentsModel.IncidentListener<Void> listener) {
        db.collection(Incident.COLLECTION).document(incident.getId()).set(incident.toJson())
                .addOnCompleteListener(task -> listener.onComplete(null));
    }

    public void registerUser(String email, String password, String fullName, String ID, int role, AppUserModel.RegisterUserListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AppUser user = new AppUser(fullName, ID, email, role, null);
                appUserDatabaseReference.child(mAuth.getCurrentUser().getUid()).setValue(user).
                        addOnCompleteListener(task1 -> listener.onComplete());
            }
        });
    }


    public void loginUser(String email, String password, AppUserModel.LoginUserListener listener) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onComplete();
            }
        })
                .addOnFailureListener(e -> listener.onFailure());
    }

    public void getCurrentUser(AppUserModel.getCurrentUserListener listener) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            listener.onFailure();
            return;
        }
        String userId = mAuth.getCurrentUser().getUid();
        appUserDatabaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AppUser user = snapshot.getValue(AppUser.class);
                listener.onComplete(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onFailure();
            }
        });
    }

    public void logOutUser(AppUserModel.logOutUserListener listener){
        mAuth.signOut();
        listener.onComplete();
    }

    public void updateUser(AppUser user, AppUserModel.BasicListener listener){
        Map<String,Object> userJson = user.toJson();
        appUserDatabaseReference.child(mAuth.getCurrentUser().getUid()).updateChildren(userJson)
                .addOnSuccessListener(unused -> listener.onComplete())
                .addOnFailureListener(e -> {});
    }

    public void uploadUserProfileImage(String name, Bitmap imageBitmap, ImagesModel.Listener<String> listener){
        StorageReference userProfileImagesRef = storageRef.child("profileImages/" + name + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = userProfileImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> listener.onComplete(null))
                .addOnSuccessListener(taskSnapshot ->
                        userProfileImagesRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> listener.onComplete(uri.toString())));
    }


    public void uploadIncidentImage(String name, Bitmap imageBitmap, IncidentsModel.IncidentListener<String> listener) {
        StorageReference incidentsImagesRef = storageRef.child("incidentsImages/" + name + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = incidentsImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(exception ->
                listener.onComplete(null))
                .addOnSuccessListener(taskSnapshot ->
                        incidentsImagesRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> listener.onComplete(uri.toString())));
    }
}