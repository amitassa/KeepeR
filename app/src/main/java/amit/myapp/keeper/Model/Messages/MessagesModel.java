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
        firebaseModel.getAllMessages(callback);
    }

    public List<Message> getAllMessages(){
        AtomicReference<List<Message>> data = new AtomicReference<>();
        firebaseModel.getAllMessages((list) -> {
            data.set(list);});
        return data.get();
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
