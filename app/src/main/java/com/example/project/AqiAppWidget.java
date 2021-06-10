package com.example.project;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE;

/**
 * Implementation of App Widget functionality.
 */
public class AqiAppWidget extends AppWidgetProvider {
    public static String myCity, myState, myCountry, weatherIconCode, category;
    public static double temperature, humidity, windSpeed, windDirection, cityLatitude, cityLongitude;
    public static int pressure, aqi;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.aqi_app_widget);
        views.setTextViewText(R.id.city1, myCity);

        views.setTextViewText(R.id.state1, myState);

        views.setTextViewText(R.id.country1, myCountry);

        views.setTextViewText(R.id.aqi1, String.valueOf(aqi));

        views.setTextViewText(R.id.country1, myCountry);

        views.setTextViewText(R.id.category1, category);

        views.setTextViewText(R.id.temperature_value1, temperature +"°C");


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (ACTION_APPWIDGET_UPDATE.equals(action)) {
            // Update your widget here.
            aqiexecute Nearest = new aqiexecute();
            Nearest.execute();
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.aqi_app_widget);
//            views.setTextViewText(R.id.city1, myCity);

        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            aqiexecute Nearest = new aqiexecute();
            Nearest.execute();
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
    public static class aqiexecute extends AsyncTask<URL,String,String> {


        @Override
        protected String doInBackground(URL... urls) {
            URL url;
            try {
                String cityurl = "http://api.airvisual.com/v2/nearest_city?key=57982133-689b-483d-88d6-775026357106";
                url = new URL(cityurl);

            }catch (MalformedURLException exception){
                Log.e("errorTag","Error with url",exception);
                return null;
            }
            String response="";
            try {
                StringBuilder output = new StringBuilder();
                HttpURLConnection urlConnection;
                InputStream inputStream;
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                }
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while(line != null){
                    output.append(line);
                    System.out.println(line);
                    line = reader.readLine();

                }

                JSONObject parentObject;
                JSONObject dataObject;
                JSONObject locationObject;
                JSONArray coordinateArray;
                JSONObject weatherObject;
                JSONObject pollutionObject;
                JSONObject currentObject;
                try {
                    parentObject = new JSONObject(output.toString());
                    dataObject = parentObject.getJSONObject("data");
                    myCity = dataObject.getString("city");
                    myState = dataObject.getString("state");
                    myCountry = dataObject.getString("country");
                    locationObject = dataObject.getJSONObject("location");
                    coordinateArray = locationObject.getJSONArray("coordinates");
                    cityLongitude = coordinateArray.getDouble(0);
                    cityLatitude = coordinateArray.getDouble(1);

                    currentObject = dataObject.getJSONObject("current");
                    weatherObject = currentObject.getJSONObject("weather");
                    temperature = weatherObject.getDouble("tp");
                    pressure = weatherObject.getInt("pr");
                    humidity = weatherObject.getDouble("hu");
                    windSpeed = weatherObject.getDouble("ws");
                    windDirection = weatherObject.getDouble("wd");
                    weatherIconCode = weatherObject.getString("ic");

                    pollutionObject = currentObject.getJSONObject("pollution");
                    aqi = pollutionObject.getInt("aqius");

               System.out.println(myCity+myState+myCountry+pressure+humidity+windSpeed+windDirection+weatherIconCode+aqi);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if ((aqi > 0) && (aqi <= 50)) {
                category = "Good";
            } else if ((aqi > 50) && (aqi <= 100)) {
                category = "Moderate";

            } else if ((aqi > 100) && (aqi <= 150)) {
                category = "unhealthy for sensitive groups";

            } else if ((aqi > 150) && (aqi <= 200)) {
                category = "unhealthy";

            } else if ((aqi > 200) && (aqi <= 300)) {
                category = "Very unhealthy";

            } else if (aqi > 300) {
                category = "hazardous";

            }



        }
    }
}