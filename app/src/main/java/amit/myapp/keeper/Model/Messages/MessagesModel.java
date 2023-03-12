package amit.myapp.keeper.Model.Messages;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import amit.myapp.keeper.Model.FirebaseModel;

public class MessagesModel {
    private static final MessagesModel _instance = new MessagesModel();
    private FirebaseModel firebaseModel = new FirebaseModel();

    public static MessagesModel instance(){
        return _instance;
    }
    private MessagesModel(){

    }

    public interface GetAllMessagesListener{
        void onComplete(List<Message> data);
    }

    public void getAllMessages(GetAllMessagesListener callback) {
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
        firebaseModel.getAllMessages(callback);
    }


    public interface AddMessageListener{
        void onComplete();
    }

    public void addMessage(Message message, AddMessageListener listener){
        firebaseModel.addMessage(message, listener);
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
