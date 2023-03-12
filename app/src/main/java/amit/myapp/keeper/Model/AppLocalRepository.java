package amit.myapp.keeper.Model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import amit.myapp.keeper.Model.Messages.Message;
import amit.myapp.keeper.Model.Messages.MessageDao;

@Database(entities = {Message.class}, version = 3)
public abstract class AppLocalRepository extends RoomDatabase {
    public abstract MessageDao messageDao();
}
