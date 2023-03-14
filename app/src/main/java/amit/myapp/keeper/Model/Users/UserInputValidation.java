package amit.myapp.keeper.Model.Users;


import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInputValidation {
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static Boolean validateId(String ID){
        return Pattern.matches("[a-zA-Z]+", ID) == false && ID.length() == 9;
    }

    public static Boolean validateEmail(String email){

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches() && !TextUtils.isEmpty(email);
    }

    public static Boolean validateName(String name){
        return !TextUtils.isEmpty(name);
    }

    public static Boolean validatePassword(String password){
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }
}
