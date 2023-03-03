package amit.myapp.keeper;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import amit.myapp.keeper.Model.Users.AppUser;
import amit.myapp.keeper.Model.Users.AppUserModel;
import amit.myapp.keeper.databinding.ActivityMainBinding;
import amit.myapp.keeper.ui.Incidents.IncidentsFragment;
import amit.myapp.keeper.ui.authentication.LoginFragment;
import amit.myapp.keeper.ui.messages.MessagesFragment;
import amit.myapp.keeper.ui.messages.MessagesFragmentDirections;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    BottomNavigationView navView;

    MaterialToolbar topAppBar;

    AppUserModel appUserModel;

    AppUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = null;
        appUserModel = AppUserModel.instance();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createBottomNavigation();
        createTopAppBar();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.incidentsFragment, R.id.messagesFragment)
//                .build();
//        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
//        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        navController = navHostFragment.getNavController();
//        //NavigationUI.setupActionBarWithNavController(this, navController);//, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);


    }

    @Override
    protected void onStart() {
        super.onStart();
        LoginFragment loginFragment = new LoginFragment();
        if (currentUser == null){
            //getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, loginFragment).commit();
        }
        updateCurrentUser();
        //setHelloUser();


    }

    private void createBottomNavigation(){
        IncidentsFragment incidentsFragment = new IncidentsFragment();
        MessagesFragment messagesFragment = new MessagesFragment();
        navView = (BottomNavigationView) binding.bottomNavBar;
        navView.setSelectedItemId(R.id.messagesFragment);
        navView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.incidentsFragment:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, incidentsFragment).commit();
                    return true;
                case R.id.messagesFragment:
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, messagesFragment).commit();
                    return true;
            }

            return false;
        });
    }

    public AppUser getCurrentUser(){
        return this.currentUser;
    }

    public void updateCurrentUser(){
        AppUserModel.getCurrentUserListener listener = new AppUserModel.getCurrentUserListener() {
            @Override
            public void onComplete(AppUser user) {
                currentUser = user;
                //ToDo: shoudnt be here, may be async problem
                setHelloUser();
            }

            @Override
            public void onFailure() {
                currentUser = null;
                setHelloUser();
            }
        };
        appUserModel.getCurrentUser(listener);
    }

    private void createTopAppBar(){
        topAppBar = (MaterialToolbar) binding.topAppBar;
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.top_app_bar_logout:
                        logOutUser();
                        return true;
                }
                return false;
            }
        });
    }
    public void setHelloUser(){
        // Todo: create the hello, user on top app bar title
        // check things like "main activity on fragment change"
        if (currentUser != null){
            binding.topAppBarHelloUserTv.setText("Hello, " + currentUser.getFullName());
        }
        else{
            binding.topAppBarHelloUserTv.setText("Hello, guest");
        }
    }

    private void logOutUser(){
        //ToDo: logout from User Model
        LoginFragment loginFragment = new LoginFragment();
        AppUserModel.logOutUserListener listener = () -> {
            currentUser = null;
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, loginFragment).commit();
            setHelloUser();
        };
        appUserModel.logOutUser(listener);

    }


}