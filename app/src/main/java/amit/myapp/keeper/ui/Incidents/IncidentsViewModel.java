package amit.myapp.keeper.ui.Incidents;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import amit.myapp.keeper.Model.Incidents.Incident;
import amit.myapp.keeper.Model.Incidents.IncidentsModel;
import amit.myapp.keeper.MyApplication;

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
        Log.d("selfincident", "setCurrentUserId: " + id);
    }
}