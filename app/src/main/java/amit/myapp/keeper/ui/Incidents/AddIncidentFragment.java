package amit.myapp.keeper.ui.Incidents;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.BoringLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.UUID;

import amit.myapp.keeper.MainActivity;
import amit.myapp.keeper.Model.Incidents.Incident;
import amit.myapp.keeper.Model.Incidents.IncidentsModel;
import amit.myapp.keeper.Model.Users.AppUser;
import amit.myapp.keeper.R;
import amit.myapp.keeper.databinding.FragmentAddIncidentBinding;

public class AddIncidentFragment extends Fragment {
    private FragmentAddIncidentBinding binding;
    private AppUser currentUser;
    private Boolean isImageSelected;
    private ActivityResultLauncher<Void> cameraLauncher;

    private ActivityResultLauncher<String> galleryLauncher;

    public AddIncidentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = ((MainActivity)getActivity()).getCurrentUser();
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null){
                binding.addIncidentFragmentIncidentImg.setImageURI(result);
                isImageSelected = true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddIncidentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.incidentsRowInclude.incidentsBarAddBtn.setVisibility(View.GONE);
        binding.addIncidentFragmentPublishBtn.setOnClickListener(view1 ->{
            publishIncident(view1);
        });
        binding.addIncidentFragmentCancelBtn.setOnClickListener(view1 ->{
            Navigation.findNavController(view1).popBackStack(R.id.incidentsFragment, false);
            binding.incidentsRowInclude.incidentsBarAddBtn.setVisibility(View.VISIBLE);
        });

        binding.addIncidentFragmentIncidentImg.setOnClickListener(view1 ->{
            galleryLauncher.launch("image/*");
        });

        return root;
    }

    private void publishIncident(View view){
        if(!validateData()){
            return;
        }
        UUID uuid = UUID.randomUUID();
        String title = binding.addIncidentFragmentIncidentTitle.getText().toString();
        String content = binding.addIncidentFragmentIncidentContent.getText().toString();
        binding.addIncidentFragmentIncidentImg.setDrawingCacheEnabled(true);
        binding.addIncidentFragmentIncidentImg.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) binding.addIncidentFragmentIncidentImg.getDrawable())
                .getBitmap();
        IncidentsModel.instance().uploadIncidentImage(uuid.toString(), bitmap, url->{
            Log.d("url", url);
            if (url != null){
                String photoUrl = url;
                Incident incident = new Incident(uuid.toString(), title, currentUser.getId(), currentUser.getFullName(),
                        photoUrl, content);
                IncidentsModel.instance().addIncident(incident,
                        (data)->Navigation.findNavController(view).popBackStack());
            }
        });
    }

    private Boolean validateData(){
        if (!isImageSelected){
            //ToDo: toast
            return false;
        }
        String title = binding.addIncidentFragmentIncidentTitle.getText().toString();
        String content = binding.addIncidentFragmentIncidentContent.getText().toString();
        if (title.isEmpty() || content.isEmpty()){
            //ToDo: toast
            return false;
        }
        return true;

    }
}