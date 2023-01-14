package amit.myapp.keeper.ui.messages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import amit.myapp.keeper.Model.Messages.Message;
import amit.myapp.keeper.Model.Messages.MessagesModel;
import amit.myapp.keeper.R;
import amit.myapp.keeper.databinding.FragmentAddMessageBinding;
import amit.myapp.keeper.databinding.FragmentMessagesBinding;

public class AddMessageFragment extends Fragment {

    private FragmentAddMessageBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public AddMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddMessageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root.findViewById(R.id.messages_bar_add_btn).setVisibility(View.GONE);

        binding.addMessageFragmentPublishBtn.setOnClickListener(view1 ->{
            publishMessage(view1);
        });



        // Cancel button returns to messages fragment
        binding.addMessageFragmentCancelBtn.setOnClickListener(view1->{
            Navigation.findNavController(view1).popBackStack(R.id.messagesFragment, false);
            root.findViewById(R.id.messages_bar_add_btn).setVisibility(View.VISIBLE);
        });

        return root;

    }

    private void publishMessage(View view){
        String title = binding.addMessageFragmentMessageTitle.getText().toString();
        String content = binding.addMessageFragmentMessageContent.getText().toString();
        Message message = new Message(content, title, "Amit", "123");
        MessagesModel.instance().addMessage(message, ()-> {Navigation.findNavController(view).popBackStack();});
    }
}