package amit.myapp.keeper.Model.Messages;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import amit.myapp.keeper.Model.AppLocalDb;
import amit.myapp.keeper.Model.AppLocalRepository;
import amit.myapp.keeper.Model.FirebaseModel;

public class MessagesModel {
    private static final MessagesModel _instance = new MessagesModel();
    private FirebaseModel firebaseModel = new FirebaseModel();
    AppLocalRepository localDb = AppLocalDb.getAppDb();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private Executor executor = Executors.newSingleThreadExecutor();

    public static MessagesModel instance(){
        return _instance;
    }
    private MessagesModel(){

    }

    public interface GetAllMessagesListener{
        void onComplete(List<Message> data);
    }

    public void getAllMessages(GetAllMessagesListener callback) {
        executor.execute(() -> {
            List<Message> allMessages = localDb.messageDao().getAll();
            mainHandler.post(() ->{
                callback.onComplete(allMessages);
            });
        });
        //callback.onComplete();
        //get local last update
//        Long localLastUpdate = Message.getLocalLastUpdate();
//        firebaseModel.getAllMessagesSince(localLastUpdate, list ->{
//            for(Message message:list){
//                localDb.studentDao().insertAll(message);
//            }
//        });
        // get all updated record from firebase since local last update

        // insert new records into room

        // update local last update

        // return complete list from room
        //firebaseModel.getAllMessages(callback);
    }



    public interface AddMessageListener{
        void onComplete();
    }

    public void addMessage(Message message, AddMessageListener listener){
        //firebaseModel.addMessage(message, listener);
        executor.execute(() ->{
            Message newMessage = Message.fromJson(message.toJson());
            localDb.messageDao().insertAll(newMessage);
            mainHandler.post(() -> {
                listener.onComplete();
            });
        });
    }

    public interface DeleteMessageListener{
        void onComplete();
    }

    public void deleteMessage(Message message, DeleteMessageListener listener){
        firebaseModel.deleteMessage(message, listener);
    }

    public interface EditMessageListener{
        void onComplete();
    }

    public void editMessage(Message message, EditMessageListener listener){
        firebaseModel.editMessage(message, listener);
    }
}
