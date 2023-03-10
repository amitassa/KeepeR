package amit.myapp.keeper.ui.Incidents;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import amit.myapp.keeper.MainActivity;
import amit.myapp.keeper.Model.Users.AppUser;
import amit.myapp.keeper.Model.Users.AppUserModel;
import amit.myapp.keeper.R;
import amit.myapp.keeper.databinding.FragmentSelfIncidentsBinding;

public class SelfIncidentsFragment extends IncidentsFragment {
    private AppUser currentUser;

    public SelfIncidentsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.currentUser = ((MainActivity)getActivity()).getCurrentUser();
        this.target = DataFetchTarget.SELF;
        if (currentUser != null){
            this.targetId = currentUser.getId();
        }
        else {
            this.targetId = "x";
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}