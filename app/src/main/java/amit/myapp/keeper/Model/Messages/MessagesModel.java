package amit.myapp.keeper.Model.Messages;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;

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

    LiveData<List<Message>> messageList;

    public LiveData<List<Message>> getAllMessages(){
        if (messageList == null){
            messageList = localDb.messageDao().getAll();
        }
        return messageList;
    }

    public void refreshAllMessages() {

        //get local last update
        Long localLatestDate = Message.getLocalLatestDate();
        firebaseModel.getAllMessagesSince(localLatestDate, list ->{
            executor.execute(() ->{
                Log.d("firebasereturn", "size" + list.size());
                Long time = localLatestDate;
                // insert new records into room
                for(Message message:list){
                    localDb.messageDao().insertAll(message);
                    if (time < message.getDate()){
                        time = message.getDate();
                    }
                }
                Message.setLocalLatestDate(time);
            });

        });
        // get all updated record from firebase since local last update


        // update local last update

        // return complete list from room
        //firebaseModel.getAllMessages(callback);
    }



    public interface AddMessageListener{
        void onComplete();
    }

    public void addMessage(Message message, AddMessageListener listener){

        firebaseModel.addMessage(message, () ->{
            listener.onComplete();
            refreshAllMessages();
        });
//        executor.execute(() ->{
//            Message newMessage = Message.fromJson(message.toJson());
//            localDb.messageDao().insertAll(newMessage);
//            mainHandler.post(() -> {
//                listener.onComplete();
//            });
//        });
    }

    public interface DeleteMessageListener{
        void onComplete();
    }

    public void deleteMessage(Message message, DeleteMessageListener listener){

        firebaseModel.deleteMessage(message, ()-> {
                    executor.execute(() -> {
                        localDb.messageDao().delete(message);
                        mainHandler.post(() -> {
                            listener.onComplete();
                        });
                    });
                });
    }

    public interface EditMessageListener{
        void onComplete();
    }

    public void editMessage(Message message, EditMessageListener listener){
        firebaseModel.editMessage(message, listener);
    }
}
