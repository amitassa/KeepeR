package amit.myapp.keeper.Model;

import androidx.room.Room;
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
