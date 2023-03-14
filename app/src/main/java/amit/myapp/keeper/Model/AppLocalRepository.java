package amit.myapp.keeper.Model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import amit.myapp.keeper.Model.Incidents.Incident;
import amit.myapp.keeper.Model.Incidents.IncidentDao;
import amit.myapp.keeper.Model.Messages.Message;
import amit.myapp.keeper.Model.Messages.MessageDao;

@Database(entities = {Message.class, Incident.class}, version = 13)
public abstract class AppLocalRepository extends RoomDatabase {
    public abstract MessageDao messageDao();
    public abstract IncidentDao incidentDao();
}
