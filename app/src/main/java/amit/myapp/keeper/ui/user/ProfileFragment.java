package amit.myapp.keeper.ui.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import amit.myapp.keeper.MainActivity;
import amit.myapp.keeper.Model.Images.ImagesModel;
import amit.myapp.keeper.Model.Users.AppUser;
import amit.myapp.keeper.Model.Users.AppUserModel;
import amit.myapp.keeper.R;
import amit.myapp.keeper.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private View root;
    private AppUser user;

    private ActivityResultLauncher<Void> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;

    private Boolean isAvatarSelected = false;
    private AppUserModel appUserModel;


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
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), result -> {
            if (result != null) {
                binding.profilePictureImg.setImageBitmap(result);
                isAvatarSelected = true;
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null){
                binding.profilePictureImg.setImageURI(result);
                isAvatarSelected = true;
            }
        });
        appUserModel = AppUserModel.instance();

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
        binding.profileNameTv.setEnabled(false);
        binding.profileEditBtn.setOnClickListener(view -> setEditMode());

        binding.profileTakePicture.setOnClickListener(view1->{
            cameraLauncher.launch(null);
        });

        binding.profileGalleryBtn.setOnClickListener(view1->{
            galleryLauncher.launch("image/*");
        });

        return root;
    }

    private void setData(){
        binding.profileEmailTv.setText(user.getEmail());
        binding.profileIdTv.setText(user.getId());
        binding.profileNameTv.setText(user.getFullName());

        if (user.getAvatarUrl()!= null && !user.getAvatarUrl().equals("")){
            Picasso.get().load(user.getAvatarUrl()).placeholder(R.drawable.user)
                    .into(binding.profilePictureImg);
        }
        else {
            binding.profilePictureImg.setImageResource(R.drawable.user);

        }
        // else, if user profile pic is null:
        //Bitmap user = BitmapFactory.decodeResource(getResources(), R.drawable.user);
    }

    private void setEditMode(){
        binding.profileNameTv.setEnabled(true);
        binding.profileEditUpdate.setVisibility(View.VISIBLE);
        binding.profileEditUpdate.setOnClickListener(view -> updateProfileData());
        binding.profileTakePicture.setVisibility(View.VISIBLE);
        binding.profileGalleryBtn.setVisibility(View.VISIBLE);
    }

    private void updateProfileData(){

        if (!user.getFullName().equals(binding.profileNameTv.getText().toString())){
            user.setFullName(binding.profileNameTv.getText().toString());
        }
        if (isAvatarSelected){
            binding.profilePictureImg.setDrawingCacheEnabled(true);
            binding.profilePictureImg.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) binding.profilePictureImg.getDrawable()).getBitmap();
            ImagesModel.instance().uploadUserProfileImage(user, bitmap, url->{
                Log.d("url", url);
                if (url != null){
                    user.setAvatarUrl(url);
                }
                appUserModel.updateUser(user, () -> {
                    navigateAfterUpdate();
//            NavDirections action = ProfileFragmentDirections.actionGlobalProfileFragment();
//            Navigation.findNavController(binding.getRoot()).navigate(action);
                });
            });
            }
        else {
            appUserModel.updateUser(user, () -> {
                navigateAfterUpdate();});
        }
    }
    private void navigateAfterUpdate(){
        ((MainActivity)getActivity()).updateCurrentUser();
        Navigation.findNavController(binding.getRoot()).popBackStack();
    }
}
