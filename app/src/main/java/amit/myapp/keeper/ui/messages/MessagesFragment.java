package amit.myapp.keeper.ui.messages;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;
import java.util.List;

import amit.myapp.keeper.MainActivity;
import amit.myapp.keeper.Model.Messages.Message;
import amit.myapp.keeper.Model.Messages.MessagesModel;
import amit.myapp.keeper.R;
import amit.myapp.keeper.databinding.FragmentMessagesBinding;

public class MessagesFragment extends Fragment {

    private FragmentMessagesBinding binding;
    private MessagesRecyclerAdapter adapter;
    private MessagesViewModel messagesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMessagesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.messagesRecyclerView.setHasFixedSize(true);
        binding.messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Create the adapter with a delete message listener
        adapter = new MessagesRecyclerAdapter(getLayoutInflater(), messagesViewModel.getData().getValue(), ()->{reloadData();}, (MainActivity) getActivity());
        binding.messagesRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(pos -> {
            // ToDo: add user page maybe
        });
        binding.messagesProgressBar.setVisibility(View.GONE);

        NavDirections action = MessagesFragmentDirections.actionGlobalAddMessageFragment();
        binding.getRoot().findViewById(R.id.messages_bar_add_btn).setOnClickListener(Navigation.createNavigateOnClickListener(action));

        messagesViewModel.getData().observe(getViewLifecycleOwner(), list ->{
            adapter.setData(list);
        });

        MessagesModel.instance().EventMessageListLoadingState.observe(getViewLifecycleOwner(), status ->{
            binding.messagesSwipeRefresh.setRefreshing(status == MessagesModel.LoadingState.LOADING);
        });

        binding.messagesSwipeRefresh.setOnRefreshListener(()->{
            reloadData();
        });
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        messagesViewModel = new ViewModelProvider(this).get(MessagesViewModel.class);
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
        if(((MainActivity)getActivity()).getCurrentUser() == null) {
            Navigation.findNavController(binding.getRoot()).navigate(MessagesFragmentDirections.actionGlobalLoginFragment());
            return;
        }
        MessagesModel.instance().refreshAllMessages();
    }
}