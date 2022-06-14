package com.example.gymstat;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsInMealAdapter extends
        RecyclerView.Adapter<ProductsInMealAdapter.ViewHolder>{



    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productNameTextView;
        public TextView kcalTextView;
        public TextView proteinsTextView;
        public TextView fatsTextView;
        public TextView carbohydratesTextView;
        public Button productButton;


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
            productButton = (Button) itemView.findViewById(R.id.productButton);


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
    }

    private List<ProductInMeal> mProducts;
    private boolean isSelected = false;
    private JSONObject user;
    String meal;
    String date;
    int eaten_products_id;
    UserVievModel userVievModel;



    public ProductsInMealAdapter(List<ProductInMeal> mProducts, JSONObject user, UserVievModel userVievModel) {
        this.mProducts = mProducts;
        this.user = user;
        this.userVievModel = userVievModel;
    }

    public void add(ProductInMeal product) {
        this.mProducts.add(product);
    }



    @NonNull
    @Override
    public ProductsInMealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View productView = inflater.inflate(R.layout.one_eaten_product_item, parent, false);

        ProductsInMealAdapter.ViewHolder viewHolder = new ProductsInMealAdapter.ViewHolder(productView);



        viewHolder.productButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isSelected)
                {
                    isSelected = true;
                    showPopUpWindow(context, viewHolder.getBindingAdapterPosition());
                }

            }
        });



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        if(mProducts != null)
        {
            if(mProducts.size()>0)
            {

                Product product = mProducts.get(position);

              //  int portion = product.get

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

    public  List<ProductInMeal> getmProducts() {
        return mProducts;
    }



    private void showPopUpWindow(Context context, int position)
    {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.meal_deleter, null);

        Button cancelButtonDeleter = popupView.findViewById(R.id.cancelButtonDeleter);
        Button okButtonDeleter =  popupView.findViewById(R.id.okButtonDeleter);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        //boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        cancelButtonDeleter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelected = false;
                popupWindow.dismiss();
            }
        });

        okButtonDeleter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mProducts.remove(position);
               // notifyItemRemoved(position);
                //notifyItemRangeChanged(position, mProducts.size());

                deleteMeal(context, position);

                isSelected = false;
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isSelected = false;
            }
        });

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

    }

    public void deleteMeal(Context context, int position)
    {

        RequestQueue queue = Volley.newRequestQueue(context);
        ProductInMeal product = mProducts.get(position);


        JSONObject jsonBody = new  JSONObject();
        try {

            //jsonBody.put("product_id", product.getId());
            jsonBody.put("name", user.getString("name"));
            jsonBody.put("password", user.getString("password"));
           // jsonBody.put("date", date);
           // jsonBody.put("meal", meal);
            jsonBody.put("eaten_products_id", product.getEaten_products_id());




        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("jsonBody  " + jsonBody.toString());

        final String mRequestBody =  jsonBody.toString();


        String url = "http://10.0.2.2:8080/app/eaten_products/delete";
        StringRequest deleteRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        System.out.println("response:"  + response);

                        JSONArray productsInMeals = userVievModel.getSelectedItemProductsInMeals().getValue();
                        JSONObject productJSON;
                        int index = -1;

                        for (int i=0; i< productsInMeals.length(); ++i)
                        {
                            try {
                                productJSON=productsInMeals.getJSONObject(i);

                                if(productJSON.getInt("eaten_products_id") == product.getEaten_products_id())
                                {
                                    index = i;
                                    break;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if(index != -1)
                        {
                            productsInMeals = RemoveJSONArray(productsInMeals, index);
                            userVievModel.selectItemProductsInMeals(productsInMeals);
                        }

                        mProducts.remove(position);
                        notifyDataSetChanged();
                        queue.stop();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println("error:"  + error);
                        queue.stop();
                    }
                }
        ){
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

        queue.add(deleteRequest);
    }


    public static JSONArray RemoveJSONArray( JSONArray jarray,int pos) {

        JSONArray Njarray=new JSONArray();
        try{
            for(int i=0;i<jarray.length();i++){
                if(i!=pos)
                    Njarray.put(jarray.get(i));
            }
        }catch (Exception e){e.printStackTrace();}
        return Njarray;

    }
}
