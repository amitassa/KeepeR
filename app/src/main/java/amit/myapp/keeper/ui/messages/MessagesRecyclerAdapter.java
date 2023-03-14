package amit.myapp.keeper.ui.messages;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import amit.myapp.keeper.MainActivity;
import amit.myapp.keeper.Model.FirebaseModel;
import amit.myapp.keeper.Model.Messages.Message;
import amit.myapp.keeper.Model.Messages.MessagesModel;
import amit.myapp.keeper.Model.Roles.PermissionManager;
import amit.myapp.keeper.Model.Users.AppUser;
import amit.myapp.keeper.R;

class MessageViewHolder extends RecyclerView.ViewHolder{
    TextView titleTv; TextView contentTv; TextView dateTv; TextView publisherTv; ImageButton deleteBtn; ImageButton editBtn;
    List<Message> messageList; MessagesModel.DeleteMessageListener deleteMessageListener;




    public MessageViewHolder(@NonNull View itemView, MessagesRecyclerAdapter.OnItemClickListener listener, List<Message> list,
                             MessagesModel.DeleteMessageListener deleteMessageListener, MainActivity mainActivity) {
        super(itemView);

        this.messageList = list;
        titleTv = itemView.findViewById(R.id.messagelistrow_title);
        publisherTv = itemView.findViewById(R.id.messagelistrow_publisher);
        contentTv = itemView.findViewById(R.id.messagelistrow_content);
        dateTv = itemView.findViewById(R.id.messagelistrow_date);
        deleteBtn = itemView.findViewById(R.id.messagelistrow_delete_btn);
        editBtn = itemView.findViewById(R.id.messagelistrow_edit_btn);
        this.deleteMessageListener = deleteMessageListener;

        deleteBtn.setOnClickListener(view -> {
            int pos = getAdapterPosition();
            Message message = messageList.get(pos);

            if (!PermissionManager.checkMessageDeletionPermissions(mainActivity.getCurrentUser(), message)) {
                showNoPermissionError();
                return;
            }
            MessagesModel.instance().deleteMessage(message, () ->{
                deleteMessageListener.onComplete();
            });
        });

        editBtn.setOnClickListener(view -> {
            int pos = getAdapterPosition();
            Message message = messageList.get(pos);

            if (!PermissionManager.checkEditMessagePermissions(mainActivity.getCurrentUser(), message)) {
                showNoPermissionError();
                return;
            }
            //ToDo: safe args
            NavDirections action = MessagesFragmentDirections.actionMessagesFragmentToEditMessageFragment(message);
            Navigation.findNavController(itemView).navigate(action);
        });

        itemView.setOnClickListener(view -> {
            int pos = getAdapterPosition();
            listener.onItemClick(pos);
        });
    }

    private void showNoPermissionError(){
        Toast.makeText(contentTv.getContext(), "You do not have permissions to do this action", Toast.LENGTH_SHORT).show();
    }

    public void bind(Message message, int pos) {
        titleTv.setText(message.getTitle());
        contentTv.setText(message.getContent());
        publisherTv.setText(message.getPublisherName());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(message.getDate()*1000);
        dateTv.setText(dateString);
    }
}

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    public static interface OnItemClickListener{
        void onItemClick(int pos);
    }
    OnItemClickListener listener;
    MessagesModel.DeleteMessageListener deleteMessageListener;
    LayoutInflater inflater;
    List<Message> messageList = new LinkedList<Message>();
    MainActivity mainActivity;

    public void setData(List<Message> list){
        this.messageList = list;
        notifyDataSetChanged();
    }

    public MessagesRecyclerAdapter(LayoutInflater inflater, List<Message> list,
                                   MessagesModel.DeleteMessageListener deleteListener, MainActivity mainActivity){
        this.inflater = inflater;
        this.messageList = list;
        this.deleteMessageListener = deleteListener;
        this.mainActivity = mainActivity;
    }

    void setOnItemClickListener(OnItemClickListener listener){this.listener = listener;}

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.message_list_row, parent, false);
        return new MessageViewHolder(view, listener, messageList, deleteMessageListener, mainActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bind(message,position);
    }

    @Override
    public int getItemCount() {
        if (messageList == null){return 0;}
        return messageList.size();
    }
}
