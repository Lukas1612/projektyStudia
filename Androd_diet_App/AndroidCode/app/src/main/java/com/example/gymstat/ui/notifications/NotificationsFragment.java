package com.example.gymstat.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymstat.AllProductsAdapter;
import com.example.gymstat.EatenProductsAdapter;
import com.example.gymstat.Product;
import com.example.gymstat.ProductInMeal;
import com.example.gymstat.R;
import com.example.gymstat.UserVievModel;
import com.example.gymstat.WaitingForUserProductsInMealActivity;
import com.example.gymstat.user_panel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    private static final int LOADING_EATEN_PRODUCTS_ACTIVITY_REQUEST_CODE = 0;

   // user_panel mainActivity;

  //  JSONObject user;
   // JSONArray productsInMeals;

    public UserVievModel userVievModel;

    protected MaterialCalendarView calendarView;

    protected RecyclerView eatenProductsRecyclerView;
    protected EatenProductsAdapter eatenProductsAdapter;
    protected RecyclerView.LayoutManager eatenProductsLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });


        userVievModel = new ViewModelProvider(requireActivity()).get(UserVievModel.class);

       // mainActivity = (user_panel) getActivity();

       // user = mainActivity.getUser();
       // productsInMeals = mainActivity.getProductsInMeals();

        calendarView = (MaterialCalendarView) root.findViewById(R.id.calendarViewMeals);

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String curDate = userVievModel.getSelectedItemCurrentDate().getValue();
            Date date = f.parse(curDate);
            calendarView.setCurrentDate(date);
            calendarView.setSelectedDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                JSONObject user = userVievModel.getSelectedItemUser().getValue();


                DateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd");
                eatenProductsAdapter = new EatenProductsAdapter(getEatenProductsArray(), user, dateFormat.format(date.getDate()), userVievModel);

                try {
                    userVievModel.selectItemcurrentDate(dateFormat.format(date.getDate()));

                    loadUserEatenProducts(dateFormat.format(date.getDate()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });




        // BEGIN_INCLUDE(initializeRecyclerView)
        eatenProductsRecyclerView = (RecyclerView) root.findViewById(R.id.eatenProductsRecyclerView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        eatenProductsLayoutManager = new LinearLayoutManager(getActivity());
        eatenProductsRecyclerView.setLayoutManager(eatenProductsLayoutManager);

        //*******************************************

      /*  ProductInMeal jablko = new ProductInMeal("jablko", 200, 20, 30, 50, 1, "dinner");
        ProductInMeal banan = new ProductInMeal("banan", 150, 10, 30, 50, 2, "breakfast");
        ProductInMeal chleb = new ProductInMeal("chleb", 200, 20, 30, 50, 1, "lunch");
        ProductInMeal maslo = new ProductInMeal("maslo", 150, 10, 30, 50, 2, "supper");

        List<ProductInMeal> mProducts = new ArrayList<ProductInMeal>();

        mProducts.add(jablko);
        mProducts.add(banan);
        mProducts.add(chleb);
        mProducts.add(maslo);
        mProducts.add(jablko);
        mProducts.add(banan);
        mProducts.add(chleb);
        mProducts.add(maslo);
        mProducts.add(banan);
        mProducts.add(chleb);
        mProducts.add(maslo);
        mProducts.add(jablko);
        mProducts.add(banan);
        mProducts.add(chleb);
        mProducts.add(maslo);
        mProducts.add(jablko);
        mProducts.add(banan);
        mProducts.add(chleb);
        mProducts.add(maslo);
        mProducts.add(banan);
        mProducts.add(chleb);
        mProducts.add(maslo);
        mProducts.add(banan);
        mProducts.add(chleb);
        mProducts.add(maslo);*/

        //*******************************************

        JSONObject user = userVievModel.getSelectedItemUser().getValue();
        String curDate = userVievModel.getSelectedItemCurrentDate().getValue();
        eatenProductsAdapter = new EatenProductsAdapter(getEatenProductsArray(), user, curDate, userVievModel);
        eatenProductsRecyclerView.setAdapter(eatenProductsAdapter);


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == LOADING_EATEN_PRODUCTS_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // get String data from Intent
                String returnString = data.getStringExtra(Intent.EXTRA_TEXT);
                try {



                    userVievModel.selectItemProductsInMeals(new JSONArray(returnString));
                   // productsInMeals = new JSONArray(returnString);



                    eatenProductsAdapter.setData(getEatenProductsArray());
                    eatenProductsAdapter.notifyDataSetChanged();
                    eatenProductsRecyclerView.setAdapter(eatenProductsAdapter);
                   // mainActivity.setProductsInMeals(productsInMeals);





                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void loadUserEatenProducts(String currentDate) throws JSONException {

        JSONObject user = userVievModel.getSelectedItemUser().getValue();

        Intent intent = new Intent(getActivity(), WaitingForUserProductsInMealActivity.class);
        intent.putExtra("name", user.getString("name"));
        intent.putExtra("password", user.getString("password"));
        intent.putExtra("date", currentDate);
        startActivityForResult(intent, LOADING_EATEN_PRODUCTS_ACTIVITY_REQUEST_CODE);
    }

    public List<ProductInMeal> getEatenProductsArray()
    {

        List<ProductInMeal> mProducts = new ArrayList<ProductInMeal>();
        int kcal = 0;
        int carb = 0;
        int fat = 0;
        int prot = 0;
        int one_portion = 0;

        JSONObject jsonObject;

        JSONArray productsInMeals = userVievModel.getSelectedItemProductsInMeals().getValue();


        for (int i=0; i<productsInMeals.length(); ++i)
        {
            try {
                jsonObject = (JSONObject) productsInMeals.get(i);

                ProductInMeal product = new ProductInMeal(jsonObject.getString("name"), jsonObject.getString("brand"), kcal(jsonObject.getInt("carbohydrates"), jsonObject.getInt("fat"),jsonObject.getInt("protein")), jsonObject.getInt("carbohydrates"), jsonObject.getInt("protein"), jsonObject.getInt("fat"), (int) jsonObject.getLong("id"),  jsonObject.getInt("one_portion"), jsonObject.getString("meal"), jsonObject.getInt("portions"), jsonObject.getInt("eaten_products_id"));

                mProducts.add(product);



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return mProducts;


    }


    private int kcal(int carb, int fat, int prot)
    {
        return 4*carb + 4*prot + 9*fat;
    }
}