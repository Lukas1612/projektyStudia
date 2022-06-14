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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EatenProductsAdapter extends
        RecyclerView.Adapter<EatenProductsAdapter.ViewHolder>{


    //android:id="@+id/mealTextView"
    //eatenProductsRecycrelView

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mealTextView;
        public RecyclerView eatenProductsRecycrelView;
        protected RecyclerView.LayoutManager eatenProductsLayoutManager;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        private ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            mealTextView = (TextView) itemView.findViewById(R.id.mealTextView);
            eatenProductsRecycrelView = (RecyclerView) itemView.findViewById(R.id.eatenProductsRecycrelView);
            eatenProductsLayoutManager = new LinearLayoutManager(this.getRecycrelView().getContext());
            eatenProductsRecycrelView.setLayoutManager(eatenProductsLayoutManager);

        }

        public TextView getTextView() {
            return mealTextView;
        }
        public RecyclerView getRecycrelView() { return eatenProductsRecycrelView; }

        public void setAdapter(ProductsInMealAdapter productsInMealAdapter) {
            eatenProductsRecycrelView.setAdapter(productsInMealAdapter);
        }
    }


    private List<ProductInMeal> mProducts;
    private List<String> meals;
    private JSONObject user;
    String date;
    UserVievModel userVievModel;

    public EatenProductsAdapter(List<ProductInMeal> mProducts, JSONObject user, String date, UserVievModel userVievModel) {
        this.mProducts = mProducts;
        this.user = user;
        this.date = date;
        this.userVievModel = userVievModel;

        meals = new ArrayList<String>();

        meals.add("breakfast");
        meals.add("dinner");
        meals.add("lunch");
        meals.add("tea");
        meals.add("supper");
    }

    public void setData(List<ProductInMeal> mProducts)
    {
        this.mProducts = mProducts;
        notifyDataSetChanged();
    }

/*
    public EatenProductsAdapter() {
        //this.eatenProductsInMeal = eatenProductsInMeal;
        this.eatenProductsInMeal = new HashMap<String, Product>();

        this.meals = new ArrayList<String>();


        meals.add("breakfast");
        meals.add("dinner");
        meals.add("lunch");
        meals.add("tea");
        meals.add("supper");

    }*/

    public void add(ProductInMeal product) {
        this.mProducts.add(product);
    }

    //public void connect(AllProductsAdapter connectedAdapter){this.connectedAdapter = connectedAdapter; }

    @NotNull
    @Override
    public EatenProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View productView = inflater.inflate(R.layout.eaten_products_item, parent, false);
        EatenProductsAdapter.ViewHolder viewHolder = new EatenProductsAdapter.ViewHolder(productView);

        return viewHolder;

    }




    @Override
    public void onBindViewHolder(EatenProductsAdapter.ViewHolder holder, int position) {
        //get current meal name
        String meal = meals.get(position);

        //divide products into meals
        int vertexCount = 5;
        ArrayList<ArrayList<ProductInMeal>> productsInMeals = new ArrayList<>(vertexCount);
        List<ProductsInMealAdapter> productsInMealAdapters = new ArrayList<ProductsInMealAdapter>();

        for(int i=0; i < vertexCount; i++) {
            productsInMeals.add(new ArrayList());
        }

        for(int I=0; I<mProducts.size(); ++I)
        {
            switch(mProducts.get(I).getMeal()) {
                case "breakfast":
                    productsInMeals.get(0).add(mProducts.get(I));
                    break;
                case "lunch":
                    productsInMeals.get(1).add(mProducts.get(I));
                    break;
                case "dinner":
                    productsInMeals.get(2).add(mProducts.get(I));
                    break;
                case "tea":
                    productsInMeals.get(3).add(mProducts.get(I));
                    break;
                case "supper":
                    productsInMeals.get(4).add(mProducts.get(I));
                    break;
            }
        }

        TextView textView = holder.getTextView();
        textView.setText(meal);

        productsInMealAdapters.add(new ProductsInMealAdapter(productsInMeals.get(0), user, userVievModel));
        productsInMealAdapters.add(new ProductsInMealAdapter(productsInMeals.get(1), user, userVievModel));
        productsInMealAdapters.add(new ProductsInMealAdapter(productsInMeals.get(2), user, userVievModel));
        productsInMealAdapters.add(new ProductsInMealAdapter(productsInMeals.get(3), user, userVievModel));
        productsInMealAdapters.add(new ProductsInMealAdapter(productsInMeals.get(4), user, userVievModel));

        //set adapter to the current meal's RecyclerView
        switch(meal) {
            case "breakfast":
               holder.setAdapter(productsInMealAdapters.get(0));
                break;
            case "lunch":
                holder.setAdapter(productsInMealAdapters.get(1));
                break;
            case "dinner":
                holder.setAdapter(productsInMealAdapters.get(2));
                break;
            case "tea":
                holder.setAdapter(productsInMealAdapters.get(3));
                break;
            case "supper":
                holder.setAdapter(productsInMealAdapters.get(4));
                break;
        }

    }

    public List<ProductInMeal> getList()
    {
        return this.mProducts;
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }




}
