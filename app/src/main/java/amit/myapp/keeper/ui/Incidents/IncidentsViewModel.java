package amit.myapp.keeper.ui.Incidents;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import amit.myapp.keeper.Model.Incidents.Incident;
import amit.myapp.keeper.Model.Incidents.IncidentsModel;

public class IncidentsViewModel extends ViewModel {

    private LiveData<List<Incident>> data = IncidentsModel.instance().getAllIncidents();

    public IncidentsViewModel() {

    }

    public LiveData<List<Incident>> getData() {
        return data;
    }
}