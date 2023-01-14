package amit.myapp.keeper.ui.messages;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import amit.myapp.keeper.Model.Messages.Message;
import amit.myapp.keeper.Model.Messages.MessagesModel;
import amit.myapp.keeper.R;

class MessageViewHolder extends RecyclerView.ViewHolder{
    TextView titleTv; TextView contentTv; TextView dateTv; TextView publisherTv; ImageButton deleteBtn; ImageButton editBtn;
    List<Message> messageList; FragmentActivity fa;


    public MessageViewHolder(@NonNull View itemView, MessagesRecyclerAdapter.OnItemClickListener listener, List<Message> list, FragmentActivity fa) {
        super(itemView);

        this.messageList = list;
        titleTv = itemView.findViewById(R.id.messagelistrow_title);
        publisherTv = itemView.findViewById(R.id.messagelistrow_publisher);
        contentTv = itemView.findViewById(R.id.messagelistrow_content);
        dateTv = itemView.findViewById(R.id.messagelistrow_date);
        deleteBtn = itemView.findViewById(R.id.messagelistrow_delete_btn);
        editBtn = itemView.findViewById(R.id.messagelistrow_edit_btn);
        this.fa = fa;

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                MessagesModel.instance().deleteMessage(messageList.get(pos), () ->{
//                    FragmentManager fragManager = fa.getSupportFragmentManager();
//                    Fragment currentFragment = fragManager.findFragmentById(R.id.messagesFragment);
//
//                    if (true) {
//                        FragmentTransaction fragTransaction = fragManager.beginTransaction();
//                        fragTransaction.detach(currentFragment);
//                        fragTransaction.attach(currentFragment);
//                        fragTransaction.commit();
//                    }
                });
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                listener.onItemClick(pos);
            }
        });


    }

    public void bind(Message message, int pos) {
        titleTv.setText(message.getTitle());
        contentTv.setText(message.getContent());
        publisherTv.setText(message.getPublisherName());
        dateTv.setText(message.getDate());
    }
}

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    public static interface OnItemClickListener{
        void onItemClick(int pos);
    }
    OnItemClickListener listener;
    LayoutInflater inflater;
    List<Message> messageList = new LinkedList<Message>();
    FragmentActivity fa;

    public void setData(List<Message> list){
        this.messageList = list;
        notifyDataSetChanged();
    }

    public MessagesRecyclerAdapter(LayoutInflater inflater, List<Message> list, FragmentActivity fa){
        this.inflater = inflater;
        this.messageList = list;
        this.fa = fa;
    }

    void setOnItemClickListener(OnItemClickListener listener){this.listener = listener;}

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.message_list_row, parent, false);
        return new MessageViewHolder(view, listener, messageList, fa);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.bind(message,position);
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
