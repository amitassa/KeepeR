package amit.myapp.keeper.ui.authentication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import amit.myapp.keeper.R;
import amit.myapp.keeper.databinding.FragmentLoginBinding;
import amit.myapp.keeper.databinding.FragmentSignupBinding;

public class SignupFragment extends Fragment {

    FragmentSignupBinding binding;
    FirebaseAuth mAuth;
    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();

        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        return root;
    }

    private void createUser(){

    }
}