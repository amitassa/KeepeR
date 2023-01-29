package amit.myapp.keeper.ui.authentication;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.auth.User;

import amit.myapp.keeper.Model.Users.AppUserModel;
import amit.myapp.keeper.Model.Users.UserInputValidation;
import amit.myapp.keeper.databinding.FragmentSignupBinding;

public class SignupFragment extends Fragment {

    FragmentSignupBinding binding;
    AppUserModel appUserModel;
    FirebaseAuth mAuth;
    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        appUserModel = AppUserModel.instance();
        Log.d("D", "onCreateView: signup");
        binding.signupBtn.setOnClickListener((view1) -> {
            ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view1.getWindowToken(),0);
            createUser();});
        NavDirections loginAction = SignupFragmentDirections.actionSignupFragmentToLoginFragment();
        binding.loginSignupBtn.setOnClickListener(Navigation.createNavigateOnClickListener(loginAction));
        mAuth = FirebaseAuth.getInstance();

        return root;
    }

    public void createUser(){
        if (!validateUserInput()){return;}
        Log.d("d", "createUser: start");
        String email = binding.signupEmailEt.getText().toString();
        String ID = binding.signupIdEt.getText().toString();
        String fullName = binding.signupFullnameEt.getText().toString();
        String password = binding.signupPasswordEt.getText().toString();
        binding.signupLoading.setVisibility(View.VISIBLE);
        appUserModel.registerUser(email, password, fullName, ID, 0, ()-> {Navigation.findNavController(binding.getRoot()).popBackStack();
        binding.signupLoading.setVisibility(View.GONE);});
        //ToDo: on failed..

    }

    private Boolean validateUserInput(){
        String email = binding.signupEmailEt.getText().toString();
        String ID = binding.signupIdEt.getText().toString();
        String fullName = binding.signupFullnameEt.getText().toString();
        String password = binding.signupPasswordEt.getText().toString();
        if (!UserInputValidation.validateName(fullName)){
            Toast.makeText(getContext(), "Please enter a valid name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!UserInputValidation.validateId(ID)){
            Toast.makeText(getContext(), "Please enter a valid ID", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!UserInputValidation.validateEmail(email)){
            Toast.makeText(getContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!UserInputValidation.validatePassword(password)){
            Toast.makeText(getContext(), "Password should be at least 6 numbers/letters", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}