package amit.myapp.keeper.ui.messages;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import amit.myapp.keeper.Model.Messages.Message;
import amit.myapp.keeper.Model.Messages.MessagesModel;
import amit.myapp.keeper.R;
import amit.myapp.keeper.databinding.FragmentAddMessageBinding;
import amit.myapp.keeper.databinding.FragmentEditMessageBinding;


public class EditMessageFragment extends Fragment {
    private FragmentEditMessageBinding binding;
    private Message currentMessage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            this.currentMessage = (Message) bundle.getSerializable("message");
        }
    }

    public EditMessageFragment() {
        // Required empty public constructor
    }

    public static EditMessageFragment newInstance(Message message){
        EditMessageFragment frag = new EditMessageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("message", message);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditMessageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setData();
        binding.editMessageFragmentPublishBtn.setOnClickListener(view1 ->{
            updateMessage(view1);
        });



        // Cancel button returns to messages fragment
        binding.editMessageFragmentCancelBtn.setOnClickListener(view1->{
            Navigation.findNavController(view1).popBackStack(R.id.messagesFragment, false);
            root.findViewById(R.id.messages_bar_add_btn).setVisibility(View.VISIBLE);
        });

        return root;

    }

    void updateMessage(View view){
        currentMessage.setContent(binding.editMessageFragmentMessageContent.getText().toString());
        currentMessage.setTitle(binding.editMessageFragmentMessageTitle.getText().toString());
        MessagesModel.instance().editMessage(currentMessage, ()-> {Navigation.findNavController(view).popBackStack();});
    }

    private void setData(){
        binding.editMessageFragmentMessageTitle.setText(currentMessage.getTitle());
        binding.editMessageFragmentMessageContent.setText(currentMessage.getContent());
    }
}