package developer.pro.hussain7ali.bakingapp.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

import developer.pro.hussain7ali.bakingapp.MainActivity;
import developer.pro.hussain7ali.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews rv = getRecipe(context);
        appWidgetManager.updateAppWidget(appWidgetId, rv);

    }

    private static RemoteViews getRecipe(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        // Set the GridWidgetService intent to act as the adapter for the GridView

        SharedPreferences prefs = context.getSharedPreferences(context.getText(R.string.favorite_recipe).toString(), Context.MODE_PRIVATE);
        String recipeID = prefs.getString("recipe12", context.getText(R.string.no_recipe).toString());
        String recipeName = prefs.getString("recipeName", context.getText(R.string.no_recipe).toString());

        views.setTextViewText(R.id.widget_TextView, recipeID);
        views.setTextViewText(R.id.widget_recipe_name, recipeName);

        // Set the PlantDetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_TextView, appPendingIntent);
        views.setOnClickPendingIntent(R.id.widget_recipe_name, appPendingIntent);

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

