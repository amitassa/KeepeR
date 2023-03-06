package amit.myapp.keeper;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
    NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = null;
        appUserModel = AppUserModel.instance();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        navController = navHostFragment.getNavController();
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
        NavigationUI.setupWithNavController(navView, navController);
        navView.setSelectedItemId(R.id.messagesFragment);
        navView.setOnItemSelectedListener(item -> NavigationUI.onNavDestinationSelected(item, navController));
//            switch (item.getItemId()){
//                case R.id.incidentsFragment:
//                    //getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, incidentsFragment).commit();
//                    NavigationUI.onNavDestinationSelected(item, navController);
//                    return true;
//                case R.id.messagesFragment:
//                    //getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, messagesFragment).commit();
//                    NavigationUI.onNavDestinationSelected(item, navController);
//                    return true;
//            }
//
//            return false;
//        });
    }

    public AppUser getCurrentUser(){
        return this.currentUser;
    }

    public void updateCurrentUser(){
        AppUserModel.getCurrentUserListener listener = new AppUserModel.getCurrentUserListener() {
            @Override
            public void onComplete(AppUser user) {
                currentUser = user;
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
        if (currentUser != null){
            binding.topAppBarHelloUserTv.setText("Hello, " + currentUser.getFullName());
        }
        else{
            binding.topAppBarHelloUserTv.setText("Hello, guest");
        }
    }

    private void logOutUser(){
        //LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.loginFragment);
//        LoginFragment loginFragment = new LoginFragment();
        AppUserModel.logOutUserListener listener = () -> {
            currentUser = null;
            // ToDo: maybe this what cracks up the fragments
            //getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main, LoginFragment.class, null).commit();
            navController.navigate(R.id.loginFragment);
            setHelloUser();
        };
        appUserModel.logOutUser(listener);

    }


}