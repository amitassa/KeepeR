package amit.myapp.keeper.Model.Incidents;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IncidentDao {
    @Query("select * from Incident")
    LiveData<List<Incident>> getAll();


    @Query("select * from Incident where id = :incidentID")
    Incident getIncidentById(String incidentID);

    @Query("select * from Incident where publisherId = :publisherId")
    LiveData<List<Incident>> getIncidentsForUser(String publisherId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Incident... incidents);

    @Delete
    void delete(Incident incident);
}
