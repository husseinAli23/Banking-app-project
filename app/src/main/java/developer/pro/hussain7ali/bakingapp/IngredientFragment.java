package developer.pro.hussain7ali.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import developer.pro.hussain7ali.bakingapp.Widget.RecipeWidgetProvider;
import developer.pro.hussain7ali.bakingapp.adapter.IngredientAdapter;
import developer.pro.hussain7ali.bakingapp.model.ModelIngredient;

public class IngredientFragment extends Fragment {

    private Menu menu;

    private final static String GITHUB_BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private RequestQueue requestQueue;
    private String recipeID, SPrecipeID;

    private final static String callbackIngList = "callbackIngList";
    private final static String callbackSPrecipeID = "callbackSPrecipeID";
    private final static String callbackRecipeID= "callbackRecipeID";

    private ArrayList<ModelIngredient> ingList = new ArrayList<>();

    private RecyclerView IngredientRecyclerView;
    private IngredientAdapter ingredientAdapter;

    private String recipeIng ="",recipeName ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        setHasOptionsMenu(true);

        IngredientRecyclerView = view.findViewById(R.id.Ingredient_RecyclerView);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(callbackIngList)) {
                ingList = savedInstanceState.getParcelableArrayList(callbackIngList);
                SPrecipeID=savedInstanceState.getString(callbackSPrecipeID);
                recipeID=savedInstanceState.getString(callbackRecipeID);
                getIngItem();
            }
        } else {
            IngRequest();
        }

        return view;
    }

    private void IngRequest() {

        requestQueue = Volley.newRequestQueue(getContext());
        Bundle extras = getActivity().getIntent().getExtras();
        IngredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (extras != null) {
            Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon().build();
            recipeID = extras.getString("recipeID");
            getRecipe(builtUri, recipeID);
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("favoriteRecipe", Context.MODE_PRIVATE);
        SPrecipeID = prefs.getString("recipeID", "");

        ingredientAdapter = new IngredientAdapter(getContext(), ingList);
        IngredientRecyclerView.setAdapter(ingredientAdapter);
    }

    private void getIngItem() {
        IngredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ingredientAdapter = new IngredientAdapter(getContext(), ingList);
        IngredientRecyclerView.setAdapter(ingredientAdapter);

    }

    private void getRecipe(Uri builtUri, final String recipeID) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                builtUri.toString(),
                (String) null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String recipe_id = jsonObject.getString("id");

                                if (recipe_id.equals(recipeID)) {

                                    recipeName=jsonObject.getString("name");
                                    JSONArray jsonArray = jsonObject.getJSONArray("ingredients");

                                    for (int j = 0; j < jsonArray.length(); j++) {

                                        JSONObject stepjsonObject = jsonArray.getJSONObject(j);

                                        String quantity = stepjsonObject.getString("quantity");
                                        String measure = stepjsonObject.getString("measure");
                                        String ingredient = stepjsonObject.getString("ingredient");

                                        recipeIng += (ingredient + " " + measure+"\n");

                                        ingList.add(new ModelIngredient(quantity, measure, ingredient));
                                        IngredientRecyclerView.setAdapter(ingredientAdapter);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        // Add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        getFav();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(callbackIngList, ingList);
        outState.putString(callbackSPrecipeID, SPrecipeID);
        outState.putString(callbackRecipeID, recipeID);
    }

    private void getFav() {
        if (SPrecipeID.equals(recipeID)) {
            MenuItem favItem = menu.findItem(R.id.fav_item);
            favItem.setIcon(R.drawable.ic_favorite_black);
            favItem.setTitle("fav");
        } else {
            MenuItem favItem = menu.findItem(R.id.fav_item);
            favItem.setIcon(R.drawable.ic_favorite_border_black);
            favItem.setTitle(getText(R.string.notfav));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.fav_item:
                MenuItem favItem = menu.findItem(R.id.fav_item);
                if (favItem.getTitle().equals(getText(R.string.notfav))) {

                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(getText(R.string.favorite_recipe).toString(), Context.MODE_PRIVATE).edit();
                    editor.clear();
                    editor.putString("recipeName", recipeName);
                    editor.putString("recipe12", recipeIng);
                    editor.apply();
                    favItem.setIcon(R.drawable.ic_favorite_black);
                    favItem.setTitle("fav");

                    // Put changes on the Widget
                    ComponentName provider = new ComponentName(getContext(), RecipeWidgetProvider.class);
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getContext());
                    int[] ids = appWidgetManager.getAppWidgetIds(provider);
                    RecipeWidgetProvider bakingWidgetProvider = new RecipeWidgetProvider();
                    bakingWidgetProvider.onUpdate(getContext(), appWidgetManager, ids);

                } else {
                    favItem.setTitle(getText(R.string.notfav));
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(getText(R.string.favorite_recipe).toString(), Context.MODE_PRIVATE).edit();
                    editor.clear();
                    editor.remove("recipe12");
                    editor.remove("recipeName");
                    editor.apply();
                    favItem.setIcon(R.drawable.ic_favorite_border_black);
                }


                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
