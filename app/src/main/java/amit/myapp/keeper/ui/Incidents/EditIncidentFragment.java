package amit.myapp.keeper.ui.Incidents;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import amit.myapp.keeper.Model.Incidents.Incident;
import amit.myapp.keeper.Model.Incidents.IncidentsModel;
import amit.myapp.keeper.R;
import amit.myapp.keeper.databinding.FragmentEditIncidentBinding;

public class EditIncidentFragment extends Fragment {

    private FragmentEditIncidentBinding binding;
    private Incident currentIncident;
    private Boolean isImageChanged;
    private ActivityResultLauncher<Void> cameraLauncher;
    private ActivityResultLauncher<String> galleryLauncher;

    public EditIncidentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            this.currentIncident = (Incident) bundle.getSerializable("incident");
        }

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), result -> {
            if (result != null) {
                binding.fragmentLayoutInclude.addIncidentFragmentIncidentImg.setImageBitmap(result);
                isImageChanged = true;
            }
        });
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null){
                binding.fragmentLayoutInclude.addIncidentFragmentIncidentImg.setImageURI(result);
                isImageChanged = true;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditIncidentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setData();
        binding.fragmentLayoutInclude.addIncidentFragmentPublishBtn.setOnClickListener((view1)->updateIncident(view1));
        binding.fragmentLayoutInclude.addIncidentFragmentCancelBtn.setOnClickListener((view1)->{
            Navigation.findNavController(view1).popBackStack(R.id.incidentsFragment, false);
            root.findViewById(R.id.incidents_bar_add_btn).setVisibility(View.VISIBLE);
        });
        binding.fragmentLayoutInclude.addIncidentFragmentIncidentImg
                .setOnClickListener((view1)->{galleryLauncher.launch("image/*");});

        isImageChanged = false;
        return root;
    }

    public void setData(){
        binding.fragmentLayoutInclude.addIncidentFragmentIncidentTitle
                .setText(currentIncident.getTitle());
        binding.fragmentLayoutInclude.addIncidentFragmentIncidentContent
                .setText(currentIncident.getContent());
        if(currentIncident.getPhotourl() != null && !currentIncident.getPhotourl().isEmpty()){
            //ToDo: change placeholder and fix fit
            Picasso.get().load(currentIncident.getPhotourl())
                    .placeholder(R.drawable.gallery_icon)

                    .into(binding.fragmentLayoutInclude.addIncidentFragmentIncidentImg);
        }
    }

    public void updateIncident(View view){
        currentIncident.setContent(binding.fragmentLayoutInclude.addIncidentFragmentIncidentContent.getText().toString());
        currentIncident.setTitle(binding.fragmentLayoutInclude.addIncidentFragmentIncidentTitle.getText().toString());
        if (isImageChanged){
            binding.fragmentLayoutInclude.addIncidentFragmentIncidentImg
                    .setDrawingCacheEnabled(true);
            binding.fragmentLayoutInclude.addIncidentFragmentIncidentImg
                    .buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) binding.fragmentLayoutInclude.addIncidentFragmentIncidentImg
                    .getDrawable()).getBitmap();
            IncidentsModel.instance().uploadIncidentImage(currentIncident.getId(), bitmap, (url)->{
                if (url != null){
                    this.currentIncident.setPhotourl(url);
                }
                IncidentsModel.instance().editIncident(currentIncident, (date)->{
                    Navigation.findNavController(view).popBackStack(R.id.incidentsFragment, false);
                });
            });
        }
        else {
            IncidentsModel.instance().editIncident(currentIncident, (date)->{
                Navigation.findNavController(view).popBackStack(R.id.incidentsFragment, false);
            });
        }
    }

}