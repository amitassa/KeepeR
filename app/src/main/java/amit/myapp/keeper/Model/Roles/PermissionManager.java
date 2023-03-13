package amit.myapp.keeper.Model.Roles;

import amit.myapp.keeper.Model.Incidents.Incident;
import amit.myapp.keeper.Model.Messages.Message;
import amit.myapp.keeper.Model.Users.AppUser;

public class PermissionManager {
    public static Boolean checkEditMessagePermissions(AppUser editor, Message message){
        return editor.getId().equals(message.getPublisherId()) || editor.getRole() == Role.ADMIN;
    }

    public static Boolean checkMessageDeletionPermissions(AppUser editor, Message message){
        return editor.getRole() == Role.ADMIN;
    }

    public static Boolean checkEditIncidentPermissions(AppUser editor, Incident incident){
        return editor.getId().equals(incident.getPublisherId()) || editor.getRole() == Role.ADMIN;
    }

    public static Boolean checkIncidentDeletionPermissions(AppUser editor, Incident incident){
        return editor.getRole() == Role.ADMIN;
    }
}
