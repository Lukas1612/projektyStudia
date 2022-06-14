package com.example.gymstat;


import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
    public class AllProductsAdapter extends
        RecyclerView.Adapter<AllProductsAdapter.ViewHolder> {


    // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productNameTextView;
        public TextView kcalTextView;
        public TextView proteinsTextView;
        public TextView fatsTextView;
        public TextView carbohydratesTextView;

        public Button addButton;

        Integer curPosition;


            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            private ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);
                productNameTextView = (TextView) itemView.findViewById(R.id.productNameTextView);
                kcalTextView = (TextView) itemView.findViewById(R.id.kcalTextView);
                proteinsTextView = (TextView) itemView.findViewById(R.id.proteinsTextView);
                fatsTextView = (TextView) itemView.findViewById(R.id.fatsTextView);
                carbohydratesTextView = (TextView) itemView.findViewById(R.id.carbohydratesTextView);
                addButton = (Button) itemView.findViewById(R.id.addButton);


            }

        public TextView getProductNameTextView() {
            return productNameTextView;
        }

        public TextView getKcalTextView() {
            return kcalTextView;
        }

        public TextView getProteinsTextView() {
            return proteinsTextView;
        }

        public TextView getFatsTextView() {
            return fatsTextView;
        }

        public TextView getCarbohydratesTextView() {
            return carbohydratesTextView;
        }


        public Button getAddButton() {
            return addButton;
        }
    }

        private List<Product> mProducts;
        private View view;

       // user_panel mainActivity;
        UserVievModel vievModel;

       // String date;
       // JSONObject user;
        String meal;

        private boolean isPopUpWindowOpen = false;


        public AllProductsAdapter(List<Product> mProducts, View view, String meal, UserVievModel vievModel) {
            this.mProducts = mProducts;
            this.view = view;
           // this.date = date;
           // this.user = user;
            this.meal = meal;
            this.vievModel = vievModel;
        }

        public void add(Product product) {
            this.mProducts.add(product);
        }


    @NotNull
    @Override
    public AllProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View productView = inflater.inflate(R.layout.item_product_adder, parent, false);

        ViewHolder viewHolder = new ViewHolder(productView);

        viewHolder.addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!isPopUpWindowOpen)
                {
                    isPopUpWindowOpen = true;
                    showPopUpWindow(context, viewHolder.curPosition);
                }

            }
        });

        return viewHolder;

    }

    private void showPopUpWindow(Context context, Integer position)
    {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_product_quantity_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        //boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        EditText editText = (EditText) popupView.findViewById(R.id.productQuantityEditText);
        Button cancelButton =(Button) popupView.findViewById(R.id.cancelButtonDeleter);
        Button okButton =(Button) popupView.findViewById(R.id.okButtonDeleter);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isPopUpWindowOpen = false;
            }
        });


        //if onclick written here, it gives null pointer exception.
        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                isPopUpWindowOpen = false;
                popupWindow.dismiss();
            }
        });


        okButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                isPopUpWindowOpen = false;

                int portions = Integer.valueOf(String.valueOf(editText.getText()));

                JSONObject user = vievModel.getSelectedItemUser().getValue();
                String date = vievModel.getSelectedItemCurrentDate().getValue();
                addEatenProduct(context, mProducts.get(position), user, date, meal, portions);

                popupWindow.dismiss();
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus == true){
                            InputMethodManager inputMgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                            inputMgr.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                        }
                    }
                });
            }
        });


        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

    @Override
    public void onBindViewHolder(AllProductsAdapter.ViewHolder holder, int position) {

        if(mProducts != null)
        {
            if(mProducts.size()>0)
            {
                Product product = mProducts.get(position);

                holder.curPosition = position;

                holder.getCarbohydratesTextView().setText( String.valueOf(product.getCarbohydrates()));
                holder.getFatsTextView().setText(String.valueOf(product.getFat()));
                holder.getKcalTextView().setText( String.valueOf(product.getKcal()));
                holder.getProteinsTextView().setText(String.valueOf(product.getProtein()));
                holder.getProductNameTextView().setText(product.getName());

            }
        }

    }


    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    public List<Product> getmProducts() {
        return mProducts;
    }


    public void addEatenProduct(Context context, Product product, JSONObject user, String date, String meal, int portions) {
        RequestQueue queue = Volley.newRequestQueue(context);


        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("product_id", product.getId());
            jsonBody.put("name", user.getString("name"));
            jsonBody.put("password", user.getString("password"));
            jsonBody.put("day", date);
            jsonBody.put("meal", meal);
            jsonBody.put("portions", portions);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String mRequestBody =  jsonBody.toString();

        String url = "http://10.0.2.2:8080/app/eaten_products";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {

                        JSONObject productInMeal = new JSONObject();

                        try {
                            productInMeal.put("id", product.getId());
                            productInMeal.put("name", product.getName());
                            productInMeal.put("one_portion", product.getOne_portion());
                            productInMeal.put("meal", meal);
                            productInMeal.put("portions", portions);
                            productInMeal.put("fat", product.getFat());
                            productInMeal.put("carbohydrates", product.getCarbohydrates());
                            productInMeal.put("protein", product.getProtein());
                            productInMeal.put("eaten_products_id", Integer.valueOf(response));
                            productInMeal.put("brand", product.getBrand());


                            JSONArray productsArray = vievModel.getSelectedItemProductsInMeals().getValue();

                            System.out.println("before:      " + productsArray.toString());
                            productsArray.put(productInMeal);
                            System.out.println("before:      " + productsArray.toString());

                            vievModel.selectItemProductsInMeals(productsArray);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        queue.stop();

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                queue.stop();
            }
        }){

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody(){
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

        };

        queue.add(stringRequest);
    }

}


