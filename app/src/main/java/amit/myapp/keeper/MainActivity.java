package amit.myapp.keeper;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;

import amit.myapp.keeper.Model.Users.AppUser;
import amit.myapp.keeper.Model.Users.AppUserModel;
import amit.myapp.keeper.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    BottomNavigationView navView; MaterialToolbar topAppBar; AppUserModel appUserModel;
    AppUser currentUser; NavController navController;


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
        navView.setOnItemSelectedListener(item -> {
            navController.popBackStack();
            return NavigationUI.onNavDestinationSelected(item, navController);
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
                setHelloUser();
                validateUserLogged();
            }

            @Override
            public void onFailure() {
                currentUser = null;
                setHelloUser();
                validateUserLogged();
            }
        };
        appUserModel.getCurrentUser(listener);
    }

    public void validateUserLogged() {
        if (this.currentUser == null) {
            navController.navigate(R.id.loginFragment);
            }
        else {
            navController.navigate(R.id.messagesFragment);
            binding.bottomNavBar.setVisibility(View.VISIBLE);
        }
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
                    navController.popBackStack();
                    navController.navigate(R.id.profileFragment, bundle);
                    return true;
            }
            return false;
        });
        updateWeather();
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
            //ToDo: check for a better way
            int count = getSupportFragmentManager().getBackStackEntryCount();
            for (int i = 0; i < count; i++){
                navController.popBackStack();
            }
            navController.navigate(R.id.loginFragment);
            setHelloUser();
        };
        appUserModel.logOutUser(listener);

    }

    private void updateWeather(){
        TextView weatherTv = binding.topAppBarWeatherTv;
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=32.085300&lon=34.781769&appid=a5bde77ec681834fe89bae88263750f6";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, response -> {
            try {
                JSONObject main = response.getJSONObject("main");
                Double kelvinTemp = main.getDouble("temp");
                int celciusTemp = (int) (kelvinTemp - 273.15);

                weatherTv.setText(Integer.toString(celciusTemp) + " C");
            } catch (JSONException e) {
                e.printStackTrace();
                weatherTv.setText("Json error");
            }
        }, error -> weatherTv.setText("get error"));

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


}