package developer.pro.hussain7ali.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import developer.pro.hussain7ali.bakingapp.DetailsActivity;
import developer.pro.hussain7ali.bakingapp.R;
import developer.pro.hussain7ali.bakingapp.RecipeFragment;
import developer.pro.hussain7ali.bakingapp.ViewRecipeActivity;
import developer.pro.hussain7ali.bakingapp.model.ModelRecipe;
import developer.pro.hussain7ali.bakingapp.model.ModelStep;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {


    private ArrayList<ModelStep> stepList;
    private Context mContext;

    public StepsAdapter(ArrayList<ModelStep> stepList, Context mContext) {
        this.stepList = stepList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.step_item, parent, false);
        StepsAdapter.ViewHolder viewHolder = new StepsAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.ViewHolder holder, final int position) {
        final ModelStep modelStep = stepList.get(position);

        TextView short_Description, step_id;

        short_Description = holder.shortDescription;
        step_id = holder.id;

        short_Description.setText(modelStep.getShortDescription());
        step_id.setText(modelStep.getId());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DisplayMetrics metrics = new DisplayMetrics();
                ((FragmentActivity)mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);

                float yInches= metrics.heightPixels/metrics.ydpi;
                float xInches= metrics.widthPixels/metrics.xdpi;
                double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);

                if (diagonalInches>=9.5) {

                    RecipeFragment recipeFragment = new RecipeFragment();

                    Bundle bundle=new Bundle();

                    bundle.putString("recipeID", modelStep.getRecipeID());
                    bundle.putInt("item_pos",position );
                    recipeFragment.setArguments(bundle);
                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_step_container, recipeFragment)
                            .commit();
                }else {

                    Intent intent = new Intent(mContext, ViewRecipeActivity.class);

                    intent.putExtra("type", "Activity");
                    intent.putExtra("recipeID", modelStep.getRecipeID());
                    intent.putExtra("item_pos",position);
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView shortDescription, id;
        CardView cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            shortDescription = itemView.findViewById(R.id.step_name);
            id = itemView.findViewById(R.id.step_id);
            cardView = itemView.findViewById(R.id.step_card);
        }
    }
}
