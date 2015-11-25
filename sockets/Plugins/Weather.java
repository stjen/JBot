package sockets.Plugins;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sockets.JSon.JsonReader;

import java.io.IOException;

public class Weather {

    public static String getWeather(String location) {
        String outMsg = "";
        JSONObject json = null;
        JSONObject c_o;

        try {
            json = JsonReader.readJsonFromUrl("http://api.wunderground.com/api/b99e6e565fb38819/conditions/q/" + location + ".json");
            c_o = json.getJSONObject("current_observation");
            outMsg += "Weather (" + c_o.getJSONObject("display_location").get("full") + ")";
            outMsg += " - Temp: " + c_o.get("temp_c") + " C (" + c_o.get("temp_f") + " F)";
            outMsg += " - Conditions: " + c_o.get("weather");
            outMsg += " - Humidity: " + c_o.get("relative_humidity");
            outMsg += " - Wind: " + c_o.get("wind_string") + " (" + c_o.get("wind_kph") + " kph) from " + c_o.get("wind_dir");
            System.out.println(c_o);
        } catch (JSONException e) {
            /* No specific city found, return multi list of cities  */
            if (e.getMessage().equals("JSONObject[\"current_observation\"] not found.")) {
                try {
                    outMsg += "Multiple choices: ";
                    JSONArray multiCityChoice = json.getJSONObject("response").getJSONArray("results");
                    for (int i = 0; i < multiCityChoice.length() && i < 10; i++) { // Max 5 results
                        outMsg += multiCityChoice.getJSONObject(i).get("city");
                    }
                    return outMsg;
                } catch (JSONException e1) {
                    System.out.println("Error getting multi city choice");
                }
            }
            outMsg = "";
        } catch (IOException e) {
            System.out.println("Error loading from URL http://api.wunderground.com/api/b99e6e565fb38819/conditions/q/" + location + ".json");
            outMsg = "";
        }
        return outMsg;
    }

    public static void main(String[] args) {
        System.out.println(getWeather("New_York"));
    }

}
