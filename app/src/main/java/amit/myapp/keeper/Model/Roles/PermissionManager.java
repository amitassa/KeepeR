package amit.myapp.keeper.Model.Roles;

import amit.myapp.keeper.Model.Messages.Message;
import amit.myapp.keeper.Model.Users.AppUser;

public class PermissionManager {
    public static Boolean checkEditMessagePermissions(AppUser editor, Message message){
        return editor.getId().equals(message.getPublisherId()) || editor.getRole() == Role.ADMIN;
    }

    public static Boolean checkMessageDeletionPermissions(AppUser editor, Message message){
        return editor.getRole() == Role.ADMIN;
    }
}
