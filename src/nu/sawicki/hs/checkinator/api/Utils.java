package nu.sawicki.hs.checkinator.api;


import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

class Utils {

    private static final String UrlBase = "http://at.hackerspace.pl/api";

    public static String constructRestUrlForCheckins(){
        return UrlBase;

    }

    public static Checkins parseResponseAsCheckins(AsyncTaskResult<JSONObject> checkinsResult) {
        JSONObject checkinsJSON = checkinsResult.getResult();
        Checkins checkins;
        ArrayList<User> users = new ArrayList<User>();

        if(checkinsJSON != null){
            try {
                int unknownDevice = checkinsJSON.getInt("unknown");
                JSONArray jsonUsers = checkinsJSON.getJSONArray("users");
                for(int i=0; i < jsonUsers.length(); ++i){
                     JSONObject JSONUser = jsonUsers.getJSONObject(i);
                     String login = JSONUser.getString("login");
                     int timestamp = JSONUser.getInt("timestamp");
                     String prettyTime = JSONUser.getString("pretty_time");
                     User user = new User(login,timestamp,prettyTime);
                     users.add(user);
                }

                checkins = new Checkins(users,unknownDevice);

            } catch (JSONException e) {
                checkins = new Checkins("error: can not parse data");
                Log.e("Utils", "There were some problems while parsing data - JSON schema is invalid!"
                        + "Please contact bofh@hackerspace.pl");
                e.printStackTrace();
            }
        }
        else {
            Exception exception = checkinsResult.getError();
            if(exception instanceof MalformedURLException){
                checkins = new Checkins("error: this error should never occur please contact viroos@hackerspace.pl" );
            } else if (exception instanceof JSONException){
                checkins = new Checkins("error: there is some problem with data returned from server."
                        + "Please checki if your network is not Walled Garden, else contact bofh@hackerspace.pl");
            } else if (exception instanceof IOException){
                checkins = new Checkins("error: can not download data, you are probably offline");
            } else {
                checkins = new Checkins("error: there was some unknown error :( ");
            }
        }

        return checkins;
    }

}
