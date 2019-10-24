package developer.pro.hussain7ali.bakingapp;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer2.ExoPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import developer.pro.hussain7ali.bakingapp.Widget.RecipeWidgetProvider;
import developer.pro.hussain7ali.bakingapp.adapter.StepsAdapter;
import developer.pro.hussain7ali.bakingapp.model.ModelRecipe;
import developer.pro.hussain7ali.bakingapp.model.ModelStep;

import static developer.pro.hussain7ali.bakingapp.MainActivity.callbackList;

public class ListOfChooseFragment extends Fragment {

    private final static String GITHUB_BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private RequestQueue requestQueue;

    private TextView textView, foodName;

    private ArrayList<ModelStep> steplist = new ArrayList<>();

    private RecyclerView StepRecyclerView;

    private StepsAdapter stepsAdapter;

    private String recipe_name;

    private final static String callbackStepsList = "callbackStepsList";
    private final static String callbackfoodName = "callbackfoodName";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listofchppse, container, false);


        StepRecyclerView = view.findViewById(R.id.stepRecyclerView);
        textView = view.findViewById(R.id.ing_name);
        foodName = view.findViewById(R.id.food_name);


        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(callbackStepsList)) {

                steplist = savedInstanceState.getParcelableArrayList(callbackStepsList);
                textView.setText(getText(R.string.Recipe_ingredients));
                foodName.setText(savedInstanceState.getString(callbackfoodName));
                getStepsItem();
            }
        } else {
            StepsRequest();
        }

        return view;
    }

    private void StepsRequest() {
        requestQueue = Volley.newRequestQueue(getContext());
        Bundle extras = getActivity().getIntent().getExtras();

        StepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (extras != null) {
            Uri builtUri = Uri.parse(GITHUB_BASE_URL).buildUpon().build();
            String recipeID = extras.getString(getText(R.string.recipeID).toString());
            getRecipe(builtUri, recipeID);
        }
        stepsAdapter = new StepsAdapter(steplist, getContext());
        StepRecyclerView.setAdapter(stepsAdapter);

    }

    private void getStepsItem() {

        StepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        stepsAdapter = new StepsAdapter(steplist, getContext());
        StepRecyclerView.setAdapter(stepsAdapter);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(callbackStepsList, steplist);
        outState.putString(callbackfoodName, recipe_name);
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
                                    recipe_name = jsonObject.getString("name");
                                    textView.setText(getText(R.string.Recipe_ingredients));
                                    foodName.setText(recipe_name);


                                    JSONArray jsonArray = jsonObject.getJSONArray("steps");

                                    for (int j = 0; j < jsonArray.length(); j++) {

                                        JSONObject stepjsonObject = jsonArray.getJSONObject(j);

                                        String step_id = stepjsonObject.getString("id");
                                        String step_short = stepjsonObject.getString("shortDescription");

                                        steplist.add(new ModelStep(step_short, step_id, recipeID));
                                        StepRecyclerView.setAdapter(stepsAdapter);
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

}
