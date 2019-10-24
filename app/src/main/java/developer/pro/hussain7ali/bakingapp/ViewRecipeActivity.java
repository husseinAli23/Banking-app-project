package developer.pro.hussain7ali.bakingapp;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import android.os.Bundle;

public class ViewRecipeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        RecipeFragment recipeFragment = new RecipeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.recipe_container, recipeFragment)
                .commit();

    }
}
