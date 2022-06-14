package com.example.gymstat.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymstat.AllProductsAdapter;
import com.example.gymstat.ItemViewModel;
import com.example.gymstat.Product;
import com.example.gymstat.ProductInMeal;
import com.example.gymstat.R;
import com.example.gymstat.UserVievModel;
import com.example.gymstat.WaitingForAllProductsActivity;
import com.example.gymstat.WaitingForUserProductsInMealActivity;
import com.example.gymstat.ui.home.HomeViewModel;
import com.example.gymstat.user_panel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class DashboardFragment extends Fragment {

    boolean isMealUpdated = false;

    private static final int LOADING_ALL_PRODUCTS_ACTIVITY_REQUEST_CODE = 0;

    private HomeViewModel dashboardViewModel;
    private ItemViewModel viewModel;

    //protected RecyclerView allProductsRecyclerView;
    protected RecyclerView addedProductsRecycleView;
    //protected AllProductsAdapter allProductsAdapter;
    protected AllProductsAdapter addedProductsAdapter;
    // protected RecyclerView.LayoutManager allProductsLayoutManager;
    protected RecyclerView.LayoutManager addedProductsLayoutManager;
    protected static ArrayList<Product> products = null;
    protected static ArrayList<Product> addedProducts = null;
    protected Button button;
    protected Spinner mealSpinner;

    View rootView;


   // user_panel mainActivity;
    //JSONObject user;

   // String curDate;
    String meal;


    public UserVievModel userVievModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);


        // Initialize dataset, this data would usually come from a local content provider or
        // remote server.

        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);
        userVievModel = new ViewModelProvider(requireActivity()).get(UserVievModel.class);

        //curDate = viewModel.getSelectedItem().getValue();
        meal = viewModel.getSelectedItemMeal().getValue();

        initDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        dashboardViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //mainActivity = (user_panel) getActivity();

       // user = mainActivity.getUser();


        // BEGIN_INCLUDE(initializeRecyclerView)
        // allProductsRecyclerView = (RecyclerView) rootView.findViewById(R.id.allProductsRecycleView);
        addedProductsRecycleView = (RecyclerView) rootView.findViewById(R.id.addedProductsRecycleView);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.

        //allProductsLayoutManager = new LinearLayoutManager(getActivity());
        //addedProductsLayoutManager = new LinearLayoutManager(getActivity());
        //allProductsLayoutManager = new GridLayoutManager(getActivity(), 1);
        addedProductsLayoutManager = new GridLayoutManager(getActivity(), 1);
        // allProductsRecyclerView.setLayoutManager(allProductsLayoutManager);
        addedProductsRecycleView.setLayoutManager(addedProductsLayoutManager);


        //  allProductsAdapter = new AllProductsAdapter(products);



        // Set CustomAdapter as the adapter for RecyclerView.
        //allProductsRecyclerView.setAdapter(allProductsAdapter);

        try {
            loadUserEatenProducts();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // END_INCLUDE(initializeRecyclerView)
       // addedProductsAdapter.connect(allProductsAdapter);

        // mealSpinner = (Spinner) rootView.findViewById(R.id.mealSpinner);
        //button = (Button) rootView.findViewById(R.id.addProductButton);



       /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductInMeal jablko = new ProductInMeal("jablko", 200, 20, 30, 50, 1, "dinner");
                addedProductsAdapter.add(jablko);
                addedProductsAdapter.notifyDataSetChanged();
            }
        });*/

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });


        return rootView;
    }


    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */

    private void initDataset() {

        //*********************************************

         
            products = new ArrayList<Product>();
            addedProducts = new ArrayList<Product>();


/*
        ProductInMeal jablko = new ProductInMeal("jablko", 200, 20, 30, 50, 1, "dinner");
        ProductInMeal banan = new ProductInMeal("banan", 150, 10, 30, 50, 2, "breakfast");

        addedProducts.add(banan);
        products.add(jablko);
        products.add(banan);
        addedProducts.add(banan);
        products.add(jablko);
        products.add(banan);
        addedProducts.add(banan);
        products.add(jablko);
        products.add(banan);
        addedProducts.add(banan);
        products.add(jablko);
        products.add(banan);
        addedProducts.add(banan);
        products.add(jablko);
        products.add(banan);
        addedProducts.add(banan);
        products.add(jablko);
        products.add(banan);
        addedProducts.add(banan);
        products.add(jablko);
        products.add(banan);
        addedProducts.add(banan);
        products.add(jablko);
        products.add(banan);
        addedProducts.add(banan);
        products.add(jablko);
        products.add(banan);
        addedProducts.add(banan);
        products.add(jablko);
        products.add(banan);*/

/*
        JSONObject jsonBody = new  JSONObject();
        try {
            jsonBody.put("product_name", "jajko10101010101");
            jsonBody.put("brand", "happy chicken");
            jsonBody.put("user_name", "Stefan");
            jsonBody.put("password", "qwerty");
            jsonBody.put("fat", 10);
            jsonBody.put("carbohydrates", 2);
            jsonBody.put("protein", 15);
            jsonBody.put("one_portion", 55);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody =  jsonBody.toString();

        RequestQueue queue = Volley.newRequestQueue(this.getActivity());
        String url = "http://10.0.2.2:8080/app/products2";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        System.out.println(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR","error => "+error.toString());
                        System.out.println("111111111111111111111111 " + error.getMessage());
                    }
                }
        ) {

           /*
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  jsonBody = new HashMap<String, String>();
                jsonBody.put("product_name", "jajko6");
                jsonBody.put("brand", "happy chicken");
                jsonBody.put("user_name", "Stefan");
                jsonBody.put("password", "password");
               // jsonBody.put("fat", "10");
               // jsonBody.put("carbohydrates", "2");
               // jsonBody.put("protein", "15");

                return jsonBody;
            }*/

/*
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };
        queue.add(postRequest);*/
        //************************************************


        //************************************************

/*
        JSONObject jsonBody = new  JSONObject();
        try {
            jsonBody.put("name", "Stefan");
            jsonBody.put("password", "qwerty");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody =  jsonBody.toString();

        RequestQueue queue = Volley.newRequestQueue(this.getActivity());
        String url ="http://10.0.2.2:8080/app/authenticate";;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        //******************************************************

                        String url ="http://10.0.2.2:8080/app/users2";

// Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        System.out.println("***************************************************************");
                                        System.out.println(response);
                                        System.out.println("***************************************************************");

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("Authorization", "Bearer "+ response);
                                return params;
                            }
                        };

// Add the request to the RequestQueue.
                        queue.add(stringRequest);


                        //******************************************************


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

        };

// Add the request to the RequestQueue.
        queue.add(stringRequest);

*/


        //************************************************


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // check that it is the SecondActivity with an OK result
        if (requestCode == LOADING_ALL_PRODUCTS_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // get String data from Intent
                String returnString = data.getStringExtra(Intent.EXTRA_TEXT);


                try {
                    JSONArray jsonArray = new JSONArray(returnString);

                    JSONObject jsonObject;

                    for (int i = 0; i < jsonArray.length(); ++i) {
                        jsonObject = (JSONObject) jsonArray.get(i);


                        Product product = new Product(jsonObject.getString("name"), jsonObject.getString("brand"), kcal(jsonObject.getInt("carbohydrates"), jsonObject.getInt("fat"), jsonObject.getInt("protein")), jsonObject.getInt("carbohydrates"), jsonObject.getInt("protein"), jsonObject.getInt("fat"), (int) jsonObject.getInt("id"), jsonObject.getInt("one_portion"));



                        addedProducts.add(product);


                    }

                  //  System.out.println("********* " + addedProducts);


                    String curDate = userVievModel.getSelectedItemCurrentDate().getValue();
                    JSONObject user = userVievModel.getSelectedItemUser().getValue();

                    addedProductsAdapter = new AllProductsAdapter(addedProducts, rootView,  meal, userVievModel);
                    addedProductsRecycleView.setAdapter(addedProductsAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void loadUserEatenProducts() throws JSONException {
        Intent intent = new Intent(getActivity(), WaitingForAllProductsActivity.class);
        startActivityForResult(intent, LOADING_ALL_PRODUCTS_ACTIVITY_REQUEST_CODE);
    }

    private int kcal(int carb, int fat, int prot)
    {
        return 4*carb + 4*prot + 9*fat;
    }

}




