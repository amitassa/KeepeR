package amit.myapp.keeper.ui.authentication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import amit.myapp.keeper.Model.Users.AppUserModel;
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
        binding.signupBtn.setOnClickListener((view1) -> createUser());
        NavDirections loginAction = SignupFragmentDirections.actionSignupFragmentToLoginFragment();
        binding.loginSignupBtn.setOnClickListener(Navigation.createNavigateOnClickListener(loginAction));
        mAuth = FirebaseAuth.getInstance();

        return root;
    }

    public void createUser(){
        // do input validation
        Log.d("d", "createUser: start");
        String email = binding.signupEmailEt.getText().toString();
        String ID = binding.signupIdEt.getText().toString();
        String fullName = binding.signupFullnameEt.getText().toString();
        String password = binding.signupPasswordEt.getText().toString();
        appUserModel.registerUser(email, password, fullName, ID, 0, ()-> {/*callback*/});
    }
}