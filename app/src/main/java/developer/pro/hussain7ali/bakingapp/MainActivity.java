package developer.pro.hussain7ali.bakingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;

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

import developer.pro.hussain7ali.bakingapp.Utils.JsonUtils;
import developer.pro.hussain7ali.bakingapp.adapter.RecipeAdapter;
import developer.pro.hussain7ali.bakingapp.model.ModelRecipe;

public class MainActivity extends AppCompatActivity {

    RecipeAdapter recipeAdapter;
    RecyclerView MainRecyclerView;

    ArrayList<ModelRecipe> recipeslist = new ArrayList<>();
    RequestQueue requestQueue;

    final static String GITHUB_BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    final static String callbackList = "callbackReviwe";

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainRecyclerView = findViewById(R.id.main_recylerview);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(callbackList)) {
                recipeslist = savedInstanceState.getParcelableArrayList(callbackList);
                getRecipeItem();
                JsonUtils.setIdleResourceTo(true);
            }
        } else {
            RecipeRequest();
        }

        JsonUtils.setIdleResourceTo(false);
    }

    private void RecipeRequest() {
        requestQueue = Volley.newRequestQueue(this);
        Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon().build();

        MainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getRecipe(builtUri, mIdlingResource);
        recipeAdapter = new RecipeAdapter(recipeslist, this);
        MainRecyclerView.setAdapter(recipeAdapter);

    }

    private void getRecipeItem() {
        MainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeAdapter = new RecipeAdapter(recipeslist, this);
        MainRecyclerView.setAdapter(recipeAdapter);
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList(callbackList, recipeslist);
    }

    public void getRecipe(Uri builtUri, @Nullable final SimpleIdlingResource idlingResource) {

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
                                String RecipeName = jsonObject.getString("name");

                                recipeslist.add(new ModelRecipe(recipe_id, RecipeName));
                                MainRecyclerView.setAdapter(recipeAdapter);
                                JsonUtils.setIdleResourceTo(true);
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
}
