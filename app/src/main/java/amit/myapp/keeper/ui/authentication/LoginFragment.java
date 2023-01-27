package amit.myapp.keeper.ui.authentication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import amit.myapp.keeper.R;
import amit.myapp.keeper.databinding.FragmentLoginBinding;
import amit.myapp.keeper.databinding.FragmentMessagesBinding;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    FirebaseAuth mAuth;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();

        NavDirections registerAction = LoginFragmentDirections.actionLoginFragmentToSignupFragment();
        binding.loginSignupBtn.setOnClickListener(Navigation.createNavigateOnClickListener(registerAction));

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            NavDirections action = LoginFragmentDirections.actionLoginFragmentToMessagesFragment();
            Navigation.findNavController(binding.getRoot()).navigate(action);
        }
    }
}