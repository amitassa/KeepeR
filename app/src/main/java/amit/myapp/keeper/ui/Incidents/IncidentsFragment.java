package amit.myapp.keeper.ui.Incidents;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import amit.myapp.keeper.MainActivity;
import amit.myapp.keeper.Model.Incidents.Incident;
import amit.myapp.keeper.Model.Incidents.IncidentsModel;
import amit.myapp.keeper.databinding.FragmentIncidentsBinding;
import amit.myapp.keeper.ui.messages.MessagesFragmentDirections;

public class IncidentsFragment extends Fragment {

    private FragmentIncidentsBinding binding;
    private IncidentsRecyclerAdapter adapter;
    private IncidentsViewModel incidentsViewModel;

    public enum DataFetchTarget{
        ALL,
        SELF
    }

    public DataFetchTarget target = DataFetchTarget.ALL;
    public String targetId = "";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentIncidentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.incidentsTemplateInclude.incidentsRecyclerView.setHasFixedSize(true);
        binding.incidentsTemplateInclude.incidentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));;

        List<Incident> data = (isSelfIncidentMode() ? incidentsViewModel.getCurrentUserData().getValue() : incidentsViewModel.getData().getValue());

        adapter = new IncidentsRecyclerAdapter(getLayoutInflater(), data,
                (T)-> {reloadData(this.target, targetId);}, (MainActivity) getActivity());

        binding.incidentsTemplateInclude.incidentsRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(pos ->{

        });
        binding.incidentsTemplateInclude.incidentsProgressBar.setVisibility(View.GONE);;

        //ToDo: fix this shit
        NavDirections action = amit.myapp.keeper.ui.messages.MessagesFragmentDirections.actionGlobalAddIncidentFragment();
        binding.incidentsTemplateInclude.incidentsInclude.incidentsBarAddBtn
                .setOnClickListener(
                Navigation.createNavigateOnClickListener(action)
        );

        binding.incidentsTemplateInclude.incidentsSwipeRefresh.setOnRefreshListener(
                ()->{reloadData(this.target, targetId);}
        );

        IncidentsModel.instance().EventIncidentsListLoadingState.observe(
                getViewLifecycleOwner(), status ->{
                    binding.incidentsTemplateInclude.incidentsSwipeRefresh.setRefreshing(
                            status == IncidentsModel.LoadingState.LOADING
                    );
                }
        );
        if (isSelfIncidentMode()){

        }
        else {

        }
        incidentsViewModel.getData().observe(getViewLifecycleOwner(), list ->{
            adapter.setData(list);
        });
        incidentsViewModel.getCurrentUserData().observe(getViewLifecycleOwner(), list ->{
            adapter.setData(list);
        });

//        final TextView textView = binding.textHome;
//        incidentsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        incidentsViewModel =
                new ViewModelProvider(this).get(IncidentsViewModel.class);
        incidentsViewModel.setCurrentUserId(((MainActivity)getActivity()).getCurrentUser().getId());

    }

    private void reloadData(DataFetchTarget fetchTarget ,String id){
        if(((MainActivity)getActivity()).getCurrentUser() == null) {
            Navigation.findNavController(binding.getRoot())
                    .navigate(IncidentsFragmentDirections.actionGlobalLoginFragment());
            return;
        }
        if (fetchTarget == DataFetchTarget.ALL) {
            IncidentsModel.instance().refreshAllIncidents();
        }
        else{
            Log.d("selfincident", "reload data: " + id);

            IncidentsModel.instance().refreshAllIncidentsForUser(id);
        }
    }
    private Boolean isSelfIncidentMode(){
        return this.target != DataFetchTarget.ALL;
    }
}