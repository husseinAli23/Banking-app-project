package developer.pro.hussain7ali.bakingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    public boolean mTwoPane;

    String recipeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        TextView textView = findViewById(R.id.ing_name);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        final double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);

        if (diagonalInches >= 9.5) {

            mTwoPane = true;

            RecipeFragment recipeFragment = new RecipeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_step_container, recipeFragment)
                    .commit();
        }
        Intent intent = getIntent();
        recipeID = intent.getStringExtra(getText(R.string.recipeID).toString());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (diagonalInches >= 9.5) {
                    IngredientFragment ingredientFragment = new IngredientFragment();
                    Objects.requireNonNull(getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_step_container, ingredientFragment)
                            .commit());
                } else {
                    Intent IntentToIngredient = new Intent(DetailsActivity.this, IngredientActivity.class);
                    IntentToIngredient.putExtra(getText(R.string.recipeID).toString(), recipeID);
                    startActivity(IntentToIngredient);
                }
            }
        });


    }


}
