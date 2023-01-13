package amit.myapp.keeper.Model.Messages;

import java.util.List;

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

    public void getAllStudents(GetAllMessagesListener callback) {
        firebaseModel.getAllMessages(callback);
    }

    public interface AddMessageListener{
        void onComplete();
    }

    public void addMessage(Message message, AddMessageListener listener){
        firebaseModel.addMessage(message, listener);
    }
}
