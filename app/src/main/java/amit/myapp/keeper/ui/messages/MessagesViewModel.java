package amit.myapp.keeper.ui.messages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

import amit.myapp.keeper.Model.Messages.Message;
import amit.myapp.keeper.Model.Messages.MessagesModel;

public class MessagesViewModel extends ViewModel {

    private LiveData<List<Message>> data = MessagesModel.instance().getAllMessages();

    public MessagesViewModel() {
    }

    public LiveData<List<Message>> getData(){
        return data;
    }

}