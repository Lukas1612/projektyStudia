package com.example.gymstat.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.fonts.SystemFonts;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gymstat.ItemViewModel;
import com.example.gymstat.MainActivity;
import com.example.gymstat.R;
import com.example.gymstat.UserVievModel;
import com.example.gymstat.WaitingForUserProductsInMealActivity;
import com.example.gymstat.ui.dashboard.DashboardFragment;
import com.example.gymstat.user_panel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private static final int LOADING_EATEN_PRODUCTS_ACTIVITY_REQUEST_CODE = 0;


    //public user_panel.Meal meal;

    private HomeViewModel homeViewModel;
    private PieChart pieChart;
    private boolean isPopUpWindowOpen = false;

    private ItemViewModel viewModel;
    protected TextView eatentKcalTextView;
    protected MaterialCalendarView calendarView;
    public UserVievModel userVievModel;


    // user_panel mainActivity;

   // JSONObject user;
   // JSONArray productsInMeals;

    private FloatingActionButton fab;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


       // mainActivity = (user_panel) getActivity();

      //  user = mainActivity.getUser();
       // productsInMeals = mainActivity.getProductsInMeals();

        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        userVievModel = new ViewModelProvider(requireActivity()).get(UserVievModel.class);


        homeViewModel =  new ViewModelProvider(this).get(HomeViewModel.class);
         View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        fab = root.findViewById(R.id.fab);

        calendarView = (MaterialCalendarView) root.findViewById(R.id.calendarViewHome);


        //"yyyy-MM-dd"
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String curDate = userVievModel.getSelectedItemCurrentDate().getValue();

            Date date = f.parse(curDate);
            calendarView.setCurrentDate(date);
            calendarView.setSelectedDate(date);


          //  viewModel.selectItem(curDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        pieChart = root.findViewById(R.id.chart);





        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                DateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd");

                try {

                    //viewModel.selectItem(dateFormat.format(date.getDate()));
                    userVievModel.selectItemcurrentDate(dateFormat.format(date.getDate()));


                    loadUserEatenProducts(dateFormat.format(date.getDate()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // viewModel.selectItem(item);
              //  item = item + 99;
                isPopUpWindowOpen = true;
                showPopUpWindow();
            }
        });



        eatentKcalTextView = root.findViewById(R.id.eatentKcalTextView);
        eatentKcalTextView.setText(String.valueOf(eatenCalories()));




        return root;
    }


    private void setFragmentInTheChartLayout(Fragment fragment)
    {
        FragmentManager fragMan = getFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        fragTransaction.add(R.id.chartLinearLayout, fragment, "fragment");
        fragTransaction.commit();

    }

    private void initChart(Integer carbohydrates, Integer proteins, Integer fats) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        String label = "types";

        if (carbohydrates == null) {
            carbohydrates = 0;
            proteins = 0;
            fats = 0;
        }

        if (carbohydrates == 0 && proteins == 0 && fats == 0) {
            Map<String, Integer> typeAmountMap = new HashMap<>();
            typeAmountMap.put("empty", 100);

            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.parseColor("#7893ff"));

            for (String type : typeAmountMap.keySet()) {
                pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
            }

            //collecting the entries with label name
            PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
            //setting text size of the value
            pieDataSet.setValueTextSize(12f);
            //providing color list for coloring different entries
            pieDataSet.setColors(colors);
            pieDataSet.setValueTextColor(colors.get(0));
            //grouping the data set from entry to chart
            PieData pieData = new PieData(pieDataSet);
            //showing the value of the entries, default true if not set
            pieData.setDrawValues(true);



            pieChart.setData(pieData);
            pieChart.invalidate();
        } else {


        //initializing data
        Map<String, Integer> typeAmountMap = new HashMap<>();
        typeAmountMap.put("Carbohydrates", carbohydrates);
        typeAmountMap.put("Proteins", proteins);
        typeAmountMap.put("Fats", fats);

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#ff0000")); //proteins
        colors.add(Color.parseColor("#0905ff")); //carbohydrates
        colors.add(Color.parseColor("#e6e68c")); //fats

        //input data and fit data into pie chart entry
        for (String type : typeAmountMap.keySet()) {
            pieEntries.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(pieEntries, label);
        //setting text size of the value
        pieDataSet.setValueTextSize(12f);
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
        pieChart.invalidate();
        }

    }

    private void showPopUpWindow()
    {
        Context context = this.getContext();

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.meal_selector, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        //boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        TextView breakfastTextView = (TextView) popupView.findViewById(R.id.breakfastTextView);
        TextView lunchTextView = (TextView) popupView.findViewById(R.id.lunchTextView);
        TextView dinnerTextView = (TextView) popupView.findViewById(R.id.dinnerTextView);
        TextView teaTextView = (TextView) popupView.findViewById(R.id.teaTextView);
        TextView supperTextView = (TextView) popupView.findViewById(R.id.supperTextView);

       user_panel mainActivity = (user_panel) getActivity();

        breakfastTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopUpWindowOpen = false;
                popupWindow.dismiss();
                viewModel.selectItemMeal("breakfast");

                Fragment fragment = new DashboardFragment();

                mainActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment)
                        .commit();
            }
        });

        lunchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopUpWindowOpen = false;
                popupWindow.dismiss();
                viewModel.selectItemMeal("lunch");

                Fragment fragment = new DashboardFragment();

                mainActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment)
                        .commit();
            }
        });
        dinnerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopUpWindowOpen = false;
                popupWindow.dismiss();
                viewModel.selectItemMeal("dinner");

                Fragment fragment = new DashboardFragment();

                mainActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment)
                        .commit();
            }
        });
        teaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopUpWindowOpen = false;
                popupWindow.dismiss();
                viewModel.selectItemMeal("tea");

                Fragment fragment = new DashboardFragment();

                mainActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment)
                        .commit();
            }
        });
        supperTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopUpWindowOpen = false;
                popupWindow.dismiss();
                viewModel.selectItemMeal("supper");

                Fragment fragment = new DashboardFragment();

               mainActivity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment)
                        .commit();
            }
        });


        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isPopUpWindowOpen = false;
            }
        });

        popupWindow.showAtLocation(this.getView(), Gravity.CENTER, 0, 0);

    }

    public int eatenCalories()
    {
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


                one_portion = (jsonObject.getInt("one_portion"))* (jsonObject.getInt("portions"));
                carb = carb + ((jsonObject.getInt("carbohydrates"))*one_portion)/100;


                fat = fat + ((jsonObject.getInt("fat"))*one_portion)/100;


                prot = prot + ((jsonObject.getInt("protein"))*one_portion)/100;


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        initChart(carb, prot, fat);
        kcal = kcal + kcal(carb, fat, prot);
        return kcal;
    }

    private int kcal(int carb, int fat, int prot)
    {
        return 4*carb + 4*prot + 9*fat;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == LOADING_EATEN_PRODUCTS_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {


                JSONArray productsInMeals;
                // get String data from Intent
                String returnString = data.getStringExtra(Intent.EXTRA_TEXT);
                try {

                    System.out.println("returnString " + returnString);
                    productsInMeals = new JSONArray(returnString);
                    eatentKcalTextView.setText(String.valueOf(eatenCalories()));
                 //   mainActivity.setProductsInMeals(productsInMeals);

                    userVievModel.selectItemProductsInMeals(productsInMeals);

                    eatentKcalTextView.setText(String.valueOf(eatenCalories()));

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



}

