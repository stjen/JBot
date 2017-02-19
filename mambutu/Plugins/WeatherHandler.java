package mambutu.Plugins;

import mambutu.Handler.Handler;
import mambutu.Handler.MessageDistributor;
import util.org.json.JSONArray;
import util.org.json.JSONException;
import util.org.json.JSONObject;
import mambutu.Config;
import mambutu.JSon.JsonReader;

import java.io.IOException;

public class WeatherHandler extends Handler {

    private static String handles = "fc, w";

    public WeatherHandler(MessageDistributor messageDistributor) {
        super(handles, messageDistributor);
        messageDistributor.registerCommandHandler(this);
    }

    /**
     * TODO:
     * - Handle city not found gracefully
     */


    public String w(String location, String nick) {
        return getWeather(location, nick);
    }

    public String fc(String location, String nick) {
        return getForecast(location, nick);
    }

    private String getWeather(String location, String nick) {
        String outMsg = "";
        JSONObject json = null;
        JSONObject c_o;

        // Format input string
        location = location.replace(" ", "_");

        try {
            json = JsonReader.readJsonFromUrl("http://api.wunderground.com/api/" + Config.WUNDERGROUND_API_KEY + "/conditions/q/" + location + ".json");
            c_o = json.getJSONObject("current_observation");
            outMsg += "Weather (" + c_o.getJSONObject("display_location").get("full") + ")";
            outMsg += " - Temp: " + c_o.get("temp_c") + " C (" + c_o.get("temp_f") + " F)";
            outMsg += " - Conditions: " + c_o.get("weather");
            outMsg += " - Humidity: " + c_o.get("relative_humidity");
            outMsg += " - Wind: " + c_o.get("wind_string");
        } catch (JSONException e) {
            /* No specific city found, return multi list of cities  */
            if (e.getMessage().equals("JSONObject[\"current_observation\"] not found.")) {
                try {
                    outMsg += "Multiple choices: ";
                    JSONArray multiCityChoice = json.getJSONObject("response").getJSONArray("results");
                    for (int i = 0; i < multiCityChoice.length() && i < 10; i++) { // Max 5 results
                        outMsg += multiCityChoice.getJSONObject(i).get("city");
                        /* State might not exist, so dont add it */
                        if (!multiCityChoice.getJSONObject(i).get("state").equals(""))
                            outMsg += " (" + multiCityChoice.getJSONObject(i).get("state") + ")";
                        outMsg += ", " + multiCityChoice.getJSONObject(i).get("country");
                        /* Only add - between cities not at the end */
                        if (i < 10 - 1 && i < multiCityChoice.length() - 1)
                            outMsg += " - ";
                    }
                    return outMsg;
                } catch (JSONException e1) {
                    System.out.println("Error getting multi city choice");
                    return outMsg = "Error finding city";
                }
            }
            outMsg = "";
        } catch (IOException e) {
            System.out.println("Error loading from URL http://api.wunderground.com/api/" + Config.WUNDERGROUND_API_KEY + "/conditions/q/" + location + ".json");
            outMsg = "";
        }
        return outMsg;
    }

    private String getForecast(String location, String nick) {
        String outMsg = "";
        JSONObject json = null;
        JSONObject c_o;

        // Format input string
        location = location.replace(" ", "_");

        try {
            json = JsonReader.readJsonFromUrl("http://api.wunderground.com/api/" + Config.WUNDERGROUND_API_KEY + "/forecast/q/" + location + ".json");
            JSONArray forecast = json.getJSONObject("forecast").getJSONObject("txt_forecast").getJSONArray("forecastday");
            System.out.println(forecast);
            System.out.println(location);
            for (int i = 0; i < forecast.length() && i < 4; i++) { // Max 5 results
                outMsg += forecast.getJSONObject(i).get("title");
                        /* State might not exist, so dont add it */
                outMsg += ", " + forecast.getJSONObject(i).get("fcttext_metric");
                        /* Only add - between cities not at the end */
                if (i < 4 - 1 && i < forecast.length() - 1)
                    outMsg += " - ";
            }
        } catch (JSONException e) {
            /* No specific city found, return multi list of cities  */
            if (e.getMessage().equals("JSONObject[\"forecast\"] not found.")) {
                outMsg += "Multiple choices: ";
                return "No such city";
            }
            outMsg = "";
        } catch (IOException e) {
            System.out.println("Error loading from URL http://api.wunderground.com/api/" + Config.WUNDERGROUND_API_KEY + "/forecast/q/" + location + ".json");
            outMsg = "";
        }
        return outMsg;
    }
}
