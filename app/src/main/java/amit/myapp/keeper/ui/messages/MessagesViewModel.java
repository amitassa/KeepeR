package amit.myapp.keeper.ui.messages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;
import java.util.List;

import amit.myapp.keeper.Model.Messages.Message;
import amit.myapp.keeper.Model.Messages.MessagesModel;

public class MessagesViewModel extends ViewModel {

    private List<Message> data = new LinkedList<Message>();

    public MessagesViewModel() {
        //MessagesModel.instance().getAllMessages((list) -> {this.data = list;});
    }

    public List<Message> getData(){
        return data;
    }

    public void setData(List<Message> list){
        this.data = list;
    }
}