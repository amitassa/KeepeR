package amit.myapp.keeper.Model.Roles;

import amit.myapp.keeper.Model.Messages.Message;
import amit.myapp.keeper.Model.Users.AppUser;

public class PermissionManager {
    public static Boolean checkEditMessagePermissions(AppUser editor, Message message){
        return editor.getId() == message.getPublisherId();
    }
}
