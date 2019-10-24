package developer.pro.hussain7ali.bakingapp.Utils;

import androidx.test.espresso.IdlingResource;

import developer.pro.hussain7ali.bakingapp.SimpleIdlingResource;

public class JsonUtils {


    private static SimpleIdlingResource sIdlingResource;

    public static IdlingResource getIdlingResource() {
        if (sIdlingResource == null) {
            sIdlingResource = new SimpleIdlingResource();
        }
        return sIdlingResource;
    }

    public static void setIdleResourceTo(boolean isIdleNow){
        if (sIdlingResource == null) {
            sIdlingResource = new SimpleIdlingResource();
        }
        sIdlingResource.setIdleState(isIdleNow);
    }
}
