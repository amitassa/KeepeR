package amit.myapp.keeper.ui.Incidents;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import amit.myapp.keeper.Model.Incidents.Incident;
import amit.myapp.keeper.Model.Incidents.IncidentsModel;

public class IncidentsViewModel extends ViewModel {

    private LiveData<List<Incident>> data = IncidentsModel.instance().getAllIncidents();
    private LiveData<List<Incident>> currentUserData;
    private String currentUserId;

    public IncidentsViewModel() {

    }

    public LiveData<List<Incident>> getData() {
        return data;
    }

    public LiveData<List<Incident>> getCurrentUserData(){
        return currentUserData;
    }

    public void setCurrentUserId(String id){
        currentUserId = id;
        currentUserData  = IncidentsModel.instance().getAllUserIncidents(id);
    }
}