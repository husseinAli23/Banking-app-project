package developer.pro.hussain7ali.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


import developer.pro.hussain7ali.bakingapp.DetailsActivity;
import developer.pro.hussain7ali.bakingapp.R;
import developer.pro.hussain7ali.bakingapp.model.ModelRecipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private ArrayList<ModelRecipe> recipeList;
    private Context mContext;

    public RecipeAdapter(ArrayList<ModelRecipe> recipeList, Context mContext) {
        this.recipeList = recipeList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.recipe_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, final int position) {
        final ModelRecipe modelRecipe = recipeList.get(position);

        TextView RecipeName;

        RecipeName = holder.textRecipe;

        RecipeName.setText(modelRecipe.getRecipe_name());
        holder.recipe_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(mContext, DetailsActivity.class);
                intent.putExtra("recipeID",modelRecipe.getRecipeID());
                intent.putExtra("recipeSteps",position);
                mContext.startActivity(intent);
                Toast.makeText(mContext, " " + modelRecipe.getRecipe_name(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        if (recipeList == null) {
            return 0;
        }
        return recipeList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textRecipe;
        CardView recipe_card;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textRecipe = itemView.findViewById(R.id.recipe_name);
            recipe_card = itemView.findViewById(R.id.recipe_card);
        }
    }
}
