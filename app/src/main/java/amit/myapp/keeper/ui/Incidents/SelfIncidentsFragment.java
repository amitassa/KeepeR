package amit.myapp.keeper.ui.Incidents;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import amit.myapp.keeper.MainActivity;
import amit.myapp.keeper.Model.Users.AppUser;
import amit.myapp.keeper.Model.Users.AppUserModel;
import amit.myapp.keeper.R;
import amit.myapp.keeper.databinding.FragmentSelfIncidentsBinding;

public class SelfIncidentsFragment extends IncidentsFragment {
    private FragmentSelfIncidentsBinding binding;
    private AppUser currentUser;

    public SelfIncidentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        AppUserModel.getCurrentUserListener listener = new AppUserModel.getCurrentUserListener() {
//            @Override
//            public void onComplete(AppUser user) {
//                currentUser = user;
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//        };
//        AppUserModel.instance().getCurrentUser(listener);
        this.currentUser = ((MainActivity)getActivity()).getCurrentUser();
        target = DataFetchTarget.SELF;
        if (currentUser != null){
            targetId = currentUser.getId();
        }
        else {
            targetId = "x";
        }

        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}