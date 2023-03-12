package amit.myapp.keeper.Model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import amit.myapp.keeper.Model.Messages.Message;
import amit.myapp.keeper.Model.Messages.MessageDao;
import amit.myapp.keeper.MyApplication;

public class AppLocalDb {
    public static AppLocalRepository getAppDb() {
        return Room.databaseBuilder(MyApplication.getMyContext(),
                AppLocalRepository.class,
                "localAppDb.db")
                .fallbackToDestructiveMigration()
                .build();
    }
    private AppLocalDb(){}
}
