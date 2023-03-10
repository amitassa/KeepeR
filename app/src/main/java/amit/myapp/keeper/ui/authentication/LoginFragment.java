package amit.myapp.keeper.ui.authentication;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import amit.myapp.keeper.MainActivity;
import amit.myapp.keeper.Model.Users.AppUser;
import amit.myapp.keeper.Model.Users.AppUserModel;
import amit.myapp.keeper.Model.Users.UserInputValidation;
import amit.myapp.keeper.R;
import amit.myapp.keeper.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    AppUserModel appUserModel;
    MainActivity mainActivity;
    View root;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        appUserModel = AppUserModel.instance();
        mainActivity = ((MainActivity) getActivity());
        binding.loginSigninBtn.setOnClickListener((view) -> {
            hideKeyboard();
            loginUser();});

        NavDirections registerAction = LoginFragmentDirections.actionLoginFragmentToSignupFragment();
        binding.loginSignupBtn.setOnClickListener(Navigation.createNavigateOnClickListener(registerAction));

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        AppUser user = mainActivity.getCurrentUser();
        mainActivity.findViewById(R.id.bottom_nav_bar).setVisibility(View.GONE);
        if (user != null){
            goToMessagesFragment();
        }
    }

    public void loginUser(){
        String email = binding.loginEmailEt.getText().toString();
        String password = binding.loginPasswordEt.getText().toString();

        if (!UserInputValidation.validateEmail(email)){
            showError("Please enter a valid email!");
            return;
        }

        if (!UserInputValidation.validatePassword(password)){
            showError("Please enter a valid password!");
            return;
        }

        binding.loginProgressBar.setVisibility(View.VISIBLE);
        AppUserModel.LoginUserListener listener = new AppUserModel.LoginUserListener() {
            @Override
            public void onComplete() {
                binding.loginProgressBar.setVisibility(View.VISIBLE);
                mainActivity.updateCurrentUser();
                mainActivity.setHelloUser();
                goToMessagesFragment();
            }

            @Override
            public void onFailure() {
                showError("Email or password are incorrect");
                binding.loginProgressBar.setVisibility(View.GONE);
            }
        };
        appUserModel.loginUser(email, password, listener);
    }

    private void goToMessagesFragment(){
        NavDirections action = LoginFragmentDirections.actionLoginFragmentToMessagesFragment();
        Navigation.findNavController(binding.getRoot()).navigate(action);
        mainActivity.findViewById(R.id.bottom_nav_bar).setVisibility(View.VISIBLE);
    }

    private void showError(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void hideKeyboard(){
        ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(binding.getRoot().getWindowToken(),0);
    }
}