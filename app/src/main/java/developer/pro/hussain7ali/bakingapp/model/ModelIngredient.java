package developer.pro.hussain7ali.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelIngredient implements Parcelable {
    private String quantity,measure,ingredient;

    public ModelIngredient(String quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    protected ModelIngredient(Parcel in) {
        quantity = in.readString();
        measure = in.readString();
        ingredient = in.readString();
    }

    public static final Creator<ModelIngredient> CREATOR = new Creator<ModelIngredient>() {
        @Override
        public ModelIngredient createFromParcel(Parcel in) {
            return new ModelIngredient(in);
        }

        @Override
        public ModelIngredient[] newArray(int size) {
            return new ModelIngredient[size];
        }
    };

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }
}
