package com.example.gymstat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.fonts.SystemFonts;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.gymstat.ui.dashboard.DashboardFragment;
import com.example.gymstat.ui.home.HomeFragment;
import com.example.gymstat.ui.notifications.NotificationsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/*meals
breakfast
lunch
dinner
tea
supper
 */

public class user_panel extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {



    private static final int LOADING_EATEN_PRODUCTS_ACTIVITY_REQUEST_CODE = 0;

   public static RecyclerView allProductsRecycleView;
   public static ArrayList<Product> products;

    public ItemViewModel viewModel;
    public UserVievModel userVievModel;
   // public JSONObject user;
   // public JSONArray productsInMeals;
   // public String currentDate;

    public enum Meal {
        breakfast,
        lunch,
        dinner,
        tea,
        supper
    }

    // public Meal meal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

       // currentDate = getCurrentDate();
        userVievModel = new ViewModelProvider(this).get(UserVievModel.class);

        userVievModel.selectItemcurrentDate(getCurrentDate());


        Bundle extras = getIntent().getExtras();
        try {
            //user = new JSONObject(extras.getString("user"));
            userVievModel.selectItemUser(new JSONObject(extras.getString("user")));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            loadUserEatenProducts();
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_dashboard:
                fragment = new DashboardFragment();
                break;
            case R.id.navigation_my_profile:
                fragment = new NotificationsFragment();
                break;
        }
        return loadFragment(fragment);
    }
    private boolean loadFragment(Fragment fragment) {

        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void loadUserEatenProducts() throws JSONException {

        JSONObject user = userVievModel.getSelectedItemUser().getValue();

        Intent intent = new Intent(this, WaitingForUserProductsInMealActivity.class);
        intent.putExtra("name", user.getString("name"));
        intent.putExtra("password", user.getString("password"));
        intent.putExtra("date", getCurrentDate());
        startActivityForResult(intent, LOADING_EATEN_PRODUCTS_ACTIVITY_REQUEST_CODE);
    }

    //waiting for open activitys data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == LOADING_EATEN_PRODUCTS_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // get String data from Intent
                String returnString = data.getStringExtra(Intent.EXTRA_TEXT);
                try {
                    //productsInMeals = new JSONArray(returnString);
                    userVievModel.selectItemProductsInMeals(new JSONArray(returnString));

                    init();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getCurrentDate()
    {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Berlin");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf.setTimeZone(timeZone);

        String date = sdf.format(new Date());

        return date;
    }




   /* public JSONObject getUser()
    {
        return user;
    }*/

   /* public JSONArray getProductsInMeals() {

        return productsInMeals;
    }*/

   /* public void setProductsInMeals(JSONArray productsInMeals) {
        this.productsInMeals = productsInMeals;
    }
*/
    private void init()
    {



        setContentView(R.layout.activity_bottom_navigation_menu);

        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
       /* viewModel.getSelectedItem().observe(this, item -> {
            //System.out.println(item);
        });*/



        //loading the default fragment
        loadFragment(new HomeFragment());

//getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(this);



    }
}