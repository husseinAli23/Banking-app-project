package developer.pro.hussain7ali.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelStep implements Parcelable {

    private String shortDescription, id, recipeID;

    public ModelStep(String shortDescription, String id, String recipeID) {
        this.shortDescription = shortDescription;
        this.id = id;
        this.recipeID = recipeID;
    }


    protected ModelStep(Parcel in) {
        shortDescription = in.readString();
        id = in.readString();
        recipeID = in.readString();
    }

    public static final Creator<ModelStep> CREATOR = new Creator<ModelStep>() {
        @Override
        public ModelStep createFromParcel(Parcel in) {
            return new ModelStep(in);
        }

        @Override
        public ModelStep[] newArray(int size) {
            return new ModelStep[size];
        }
    };

    public String getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(String recipeID) {
        this.recipeID = recipeID;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shortDescription);
        parcel.writeString(id);
        parcel.writeString(recipeID);
    }
}
