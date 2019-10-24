package developer.pro.hussain7ali.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelRecipe implements Parcelable {

    private String RecipeID,Recipe_name;

    public ModelRecipe(String recipeID, String recipe_name) {
        RecipeID = recipeID;
        Recipe_name = recipe_name;
    }

    protected ModelRecipe(Parcel in) {
        RecipeID = in.readString();
        Recipe_name = in.readString();
    }

    public static final Creator<ModelRecipe> CREATOR = new Creator<ModelRecipe>() {
        @Override
        public ModelRecipe createFromParcel(Parcel in) {
            return new ModelRecipe(in);
        }

        @Override
        public ModelRecipe[] newArray(int size) {
            return new ModelRecipe[size];
        }
    };

    public String getRecipeID() {
        return RecipeID;
    }

    public void setRecipeID(String recipeID) {
        RecipeID = recipeID;
    }

    public String getRecipe_name() {
        return Recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        Recipe_name = recipe_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(RecipeID);
        parcel.writeString(Recipe_name);
    }
}
