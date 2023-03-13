package amit.myapp.keeper.Model.Incidents;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import amit.myapp.keeper.Model.AppLocalDb;
import amit.myapp.keeper.Model.AppLocalRepository;
import amit.myapp.keeper.Model.FirebaseModel;
import amit.myapp.keeper.Model.Messages.MessagesModel;


public class IncidentsModel {

    private static final IncidentsModel _instance = new IncidentsModel();

    private FirebaseModel firebaseModel = new FirebaseModel();
    AppLocalRepository localDb = AppLocalDb.getAppDb();
    private Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private Executor executor = Executors.newSingleThreadExecutor();

    private LiveData<List<Incident>> incidentsList;

    public static IncidentsModel instance(){
        return _instance;
    }

    private IncidentsModel(){

    }

    public enum LoadingState{
        LOADING,
        NOT_LOADING
    }
    final public MutableLiveData<LoadingState> EventIncidentsListLoadingState = new MutableLiveData<LoadingState>(LoadingState.NOT_LOADING);

    public interface IncidentListener<T>{
        void onComplete(T data);
    }

    public LiveData<List<Incident>> getAllIncidents(){
        if (incidentsList == null){
            incidentsList = localDb.incidentDao().getAll();
        }
        return incidentsList;
    }


    public void refreshAllIncidents(){
        EventIncidentsListLoadingState.setValue(LoadingState.LOADING);
        Long localLatestDate = Incident.getLocalLatestDate();

        firebaseModel.getAllIncidentsSince(localLatestDate, list->{
            Log.d("firebasereturn", "size" + list.size());
            Long time = localLatestDate;
            for(Incident incident:list){
                localDb.incidentDao().insertAll(incident);
                if(time < incident.getDate()){
                    time = incident.getDate();
                }
            }
            Incident.setLocalLatestDate(time);
            EventIncidentsListLoadingState.postValue(LoadingState.NOT_LOADING);
        });
    }

    public void addIncident(Incident incident, IncidentListener<Void> listener){
        firebaseModel.addIncident(incident, (data)->{
            listener.onComplete(null);
            refreshAllIncidents();
        });
    }

    public void deleteIncident(Incident incident, IncidentListener<Void> listener){
        firebaseModel.deleteIncident(incident, (data)->{
            executor.execute(()->{
                localDb.incidentDao().delete(incident);
                mainHandler.post(()->{
                    listener.onComplete(null);
                });
            });
        });
    }

    public void editIncident(Incident incident, IncidentListener<Void> listener){
        firebaseModel.editIncident(incident, listener);
    }

    public void uploadIncidentImage(String uuid, Bitmap imageBitmap, IncidentListener<String> listener){
        firebaseModel.uploadIncidentImage(uuid, imageBitmap, listener);
    }
}
