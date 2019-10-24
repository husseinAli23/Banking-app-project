package developer.pro.hussain7ali.bakingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import developer.pro.hussain7ali.bakingapp.R;
import developer.pro.hussain7ali.bakingapp.model.ModelIngredient;
import developer.pro.hussain7ali.bakingapp.model.ModelRecipe;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private Context mContext;
    private List<ModelIngredient> IngList;

    public IngredientAdapter(Context mContext, List<ModelIngredient> ingList) {
        this.mContext = mContext;
        IngList = ingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.ingredient_item, parent, false);
        IngredientAdapter.ViewHolder viewHolder = new IngredientAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ModelIngredient modelIngredient= IngList.get(position);

        TextView ingredientMeasure,ingredientQuantity,ingredientName;

        ingredientMeasure = holder.ingredientMeasure;
        ingredientQuantity= holder.ingredientQuantity;
        ingredientName= holder.ingredientName;

        ingredientName.setText(modelIngredient.getIngredient());
        ingredientMeasure.setText(modelIngredient.getMeasure());
        ingredientQuantity.setText(modelIngredient.getQuantity());

    }

    @Override
    public int getItemCount() {
        return IngList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
       TextView ingredientName,ingredientMeasure,ingredientQuantity;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ingredientName=itemView.findViewById(R.id.ingredient_name);
            ingredientMeasure=itemView.findViewById(R.id.ingredient_measure);
            ingredientQuantity=itemView.findViewById(R.id.ingredient_quantity);
        }
    }
}
