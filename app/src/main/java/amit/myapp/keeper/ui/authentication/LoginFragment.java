package amit.myapp.keeper.ui.authentication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavHost;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import amit.myapp.keeper.Model.Users.AppUserModel;
import amit.myapp.keeper.R;
import amit.myapp.keeper.databinding.FragmentLoginBinding;
import amit.myapp.keeper.databinding.FragmentMessagesBinding;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    AppUserModel appUserModel;
    NavHostFragment navHostFragment;
    NavController navController;
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
        appUserModel = AppUserModel.instance();

        binding.loginSigninBtn.setOnClickListener(view -> loginUser());

        NavDirections registerAction = LoginFragmentDirections.actionLoginFragmentToSignupFragment();
        binding.loginSignupBtn.setOnClickListener(Navigation.createNavigateOnClickListener(registerAction));

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        //ToDo: get user from users model
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
           goToMessagesFragment();
        }
    }

    public void loginUser(){
        //ToDo: validation
        String email = binding.loginEmailEt.getText().toString();
        String password = binding.loginPasswordEt.getText().toString();
        binding.loginProgressBar.setVisibility(View.VISIBLE);
        appUserModel.loginUser(email, password, () -> {goToMessagesFragment();
        binding.loginProgressBar.setVisibility(View.GONE);});
        //ToDo: on failed..

    }

    private void goToMessagesFragment(){
        NavDirections action = LoginFragmentDirections.actionLoginFragmentToMessagesFragment();
        Navigation.findNavController(binding.getRoot()).navigate(action);
    }
}