package amit.myapp.keeper.ui.Incidents;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import amit.myapp.keeper.MainActivity;
import amit.myapp.keeper.Model.Incidents.IncidentsModel;
import amit.myapp.keeper.databinding.FragmentIncidentsBinding;
import amit.myapp.keeper.ui.messages.MessagesFragmentDirections;

public class IncidentsFragment extends Fragment {

    private FragmentIncidentsBinding binding;
    private IncidentsRecyclerAdapter adapter;
    private IncidentsViewModel incidentsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentIncidentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.incidentsTemplateInclude.incidentsRecyclerView.setHasFixedSize(true);
        binding.incidentsTemplateInclude.incidentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));;
        adapter = new IncidentsRecyclerAdapter(getLayoutInflater(), incidentsViewModel.getData().getValue(),
                (T)-> {reloadData();}, (MainActivity) getActivity());

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
                ()->{reloadData();}
        );

        IncidentsModel.instance().EventIncidentsListLoadingState.observe(
                getViewLifecycleOwner(), status ->{
                    binding.incidentsTemplateInclude.incidentsSwipeRefresh.setRefreshing(
                            status == IncidentsModel.LoadingState.LOADING
                    );
                }
        );

        incidentsViewModel.getData().observe(getViewLifecycleOwner(), list ->{
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
    }

    private void reloadData(){
        if(((MainActivity)getActivity()).getCurrentUser() == null) {
            Navigation.findNavController(binding.getRoot())
                    .navigate(IncidentsFragmentDirections.actionGlobalLoginFragment());
            return;
        }
            IncidentsModel.instance().refreshAllIncidents();
    }
}