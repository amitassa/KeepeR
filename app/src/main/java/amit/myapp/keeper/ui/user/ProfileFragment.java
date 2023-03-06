package amit.myapp.keeper.ui.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import amit.myapp.keeper.Model.Users.AppUser;
import amit.myapp.keeper.R;
import amit.myapp.keeper.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    View root;
    AppUser user;


    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle == null){
            this.user = null;
        }
        else {
            this.user = (AppUser) bundle.getSerializable("user");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        setData();
        binding.profileBackBtn.setOnClickListener(view -> {
            Navigation.findNavController(view).popBackStack();
        });
        return root;
    }

    private void setData(){
        binding.profileEmailTv.setText(user.getEmail());
        binding.profileIdTv.setText(user.getId());
        binding.profileNameTv.setText(user.getFullName());

        //ToDo: set photo here!
        //binding.profilePictureImg.setImageBitmap(R.drawable.add_icon);
    }
}