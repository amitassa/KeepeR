package amit.myapp.keeper;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import amit.myapp.keeper.databinding.ActivityMainBinding;
import amit.myapp.keeper.ui.Incidents.IncidentsFragment;
import amit.myapp.keeper.ui.messages.MessagesFragment;
import amit.myapp.keeper.ui.messages.MessagesFragmentDirections;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    FirebaseAuth mAuth;
    BottomNavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        createBottomNavigation();
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
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){

        }
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
}