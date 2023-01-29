package amit.myapp.keeper.ui.messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;
import java.util.List;

import amit.myapp.keeper.Model.Messages.Message;
import amit.myapp.keeper.Model.Messages.MessagesModel;
import amit.myapp.keeper.R;
import amit.myapp.keeper.databinding.FragmentMessagesBinding;

public class MessagesFragment extends Fragment {

    private FragmentMessagesBinding binding;
    private MessagesRecyclerAdapter adapter;
    private List<Message> messageList = new LinkedList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMessagesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.messagesRecyclerView.setHasFixedSize(true);
        binding.messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // sent the adapter the delete message callbacks
        adapter = new MessagesRecyclerAdapter(getLayoutInflater(), messageList, ()->{reloadData();});
        binding.messagesRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MessagesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {}
        });
        binding.messagesfragSignout.setOnClickListener((view -> {
            FirebaseAuth.getInstance().signOut();
            reloadData();
        }));

        NavDirections action = MessagesFragmentDirections.actionGlobalAddMessageFragment();
        binding.getRoot().findViewById(R.id.messages_bar_add_btn).setOnClickListener(Navigation.createNavigateOnClickListener(action));

        //binding.messagesFragAddBtn.setOnClickListener(Navigation.);
//        Message message = new Message("תוכן", "כותרת", "עמית", "13/01/2023");
//        MessagesModel.instance().addMessage(message, ()->{
//        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onResume() {
        super.onResume();
        reloadData();
    }

    public void reloadData(){
        // ToDo: progress bar
        //binding.progressBar.setVisibility(View.VISIBLE);
        MessagesModel.instance().getAllMessages((list)->{
            messageList = list;
            adapter.setData(messageList);
            //binding.progressBar.setVisibility(View.GONE);
            // ToDo: Get it from appuser model
            if(FirebaseAuth.getInstance().getCurrentUser() == null){
                Navigation.findNavController(binding.getRoot()).navigate(MessagesFragmentDirections.actionGlobalLoginFragment());
            }
        });
    }
}