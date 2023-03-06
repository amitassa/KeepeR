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
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateCurrentUser();
    }

    private void createBottomNavigation(){
        navView = binding.bottomNavBar;
        NavigationUI.setupWithNavController(navView, navController);
        navView.setSelectedItemId(R.id.messagesFragment);
        navView.setOnItemSelectedListener(item -> NavigationUI.onNavDestinationSelected(item, navController));

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
        topAppBar = binding.topAppBar;
        topAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.top_app_bar_logout:
                    logOutUser();
                    return true;
                case R.id.top_app_bar_profile:
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", this.currentUser);
                    navController.navigate(R.id.profileFragment, bundle);
                    return true;
            }
            return false;
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
        AppUserModel.logOutUserListener listener = () -> {
            currentUser = null;
            navController.navigate(R.id.loginFragment);
            setHelloUser();
        };
        appUserModel.logOutUser(listener);

    }


}