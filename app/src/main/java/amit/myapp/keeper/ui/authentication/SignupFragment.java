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
import amit.myapp.keeper.Model.Users.AppUserModel;
import amit.myapp.keeper.Model.Users.UserInputValidation;
import amit.myapp.keeper.R;
import amit.myapp.keeper.databinding.FragmentSignupBinding;

public class SignupFragment extends Fragment {

    FragmentSignupBinding binding;
    AppUserModel appUserModel;
    MainActivity mainActivity;
    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        appUserModel = AppUserModel.instance();
        binding.signupBtn.setOnClickListener((view1) -> {
            hideKeyboard();
            createUser();});
        NavDirections loginAction = SignupFragmentDirections.actionSignupFragmentToLoginFragment();
        binding.loginSignupBtn.setOnClickListener(Navigation.createNavigateOnClickListener(loginAction));
        mainActivity = (MainActivity) getActivity();
        return root;
    }

    public void createUser(){
        if (!validateUserInput()){return;}
        String email = binding.signupEmailEt.getText().toString();
        String ID = binding.signupIdEt.getText().toString();
        String fullName = binding.signupFullnameEt.getText().toString();
        String password = binding.signupPasswordEt.getText().toString();
        binding.signupLoading.setVisibility(View.VISIBLE);
        appUserModel.registerUser(email, password, fullName, ID, 1, ()-> {
            Navigation.findNavController(binding.getRoot()).popBackStack(R.id.loginFragment, false);
            mainActivity.updateCurrentUser();
            mainActivity.setHelloUser();
            NavDirections action = SignupFragmentDirections.actionGlobalMessagesFragment();
            Navigation.findNavController(binding.getRoot()).navigate(action);
            binding.signupLoading.setVisibility(View.GONE);

        });
        //ToDo: on failed..

    }

    private Boolean validateUserInput(){
        String email = binding.signupEmailEt.getText().toString();
        String ID = binding.signupIdEt.getText().toString();
        String fullName = binding.signupFullnameEt.getText().toString();
        String password = binding.signupPasswordEt.getText().toString();
        if (!UserInputValidation.validateName(fullName)){
            showError("Please enter a valid name");
            return false;
        }
        if (!UserInputValidation.validateId(ID)){
            showError("Please enter a valid ID");
            return false;
        }
        if (!UserInputValidation.validateEmail(email)){
            showError("Please enter a valid email");
            return false;
        }
        if (!UserInputValidation.validatePassword(password)){
            showError("Password should be at least 6 numbers/letters");
            return false;
        }
        return true;
    }

    private void showError(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void hideKeyboard(){
        ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(binding.getRoot().getWindowToken(),0);
    }
}