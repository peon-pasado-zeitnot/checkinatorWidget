package nu.sawicki.hs.checkinator.api;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An AsyncTask implementation for performing GETs on the Hypothetical REST APIs.
 */
class GetTask extends AsyncTask<String,String,AsyncTaskResult<JSONObject>> {

    private final String mRestUrl;
    private final RestTaskCallback mCallback;

    /**
     * Creates a new instance of GetTask with the specified URL and callback.
     *
     * @param restUrl The URL for the REST API.
     * @param callback The callback to be invoked when the HTTP request
     *            completes.
     *
     */
    public GetTask(String restUrl, RestTaskCallback callback){
        this.mRestUrl = restUrl;
        this.mCallback = callback;
    }

    @Override
    protected AsyncTaskResult<JSONObject> doInBackground(String... params) {
        AsyncTaskResult<JSONObject> result = null;



        String response = null;
        InputStream in = null;
        HttpURLConnection urlConnection = null;

        try {

            URL url = new URL(mRestUrl);
            urlConnection = (HttpURLConnection) url.openConnection();


            in = new BufferedInputStream(urlConnection.getInputStream());
            byte[] contents = new byte[1024];

            int bytesRead;
            while( (bytesRead = in.read(contents)) != -1){
                response = new String(contents, 0, bytesRead);
            }

            JSONObject checkinsJSON = new JSONObject(response);
            result = new AsyncTaskResult<JSONObject>(checkinsJSON);

        } catch (MalformedURLException e) {
            result = new AsyncTaskResult<JSONObject>(e);
            Log.e("AsyncTaskResult", "There was MalformedURLException please report this bug to viroos@hackerspace.pl");
            e.printStackTrace();
        } catch (JSONException e) {
            result = new AsyncTaskResult<JSONObject>(e);
            Log.e("AsyncTaskResult", "There were some problems while parsing data - server did not return valid json"
                    + "response. Please check if your network is not walled garden please"
                    + "let know bofh@hackerspace.pl");
            e.printStackTrace();
        } catch (IOException e) {
            result = new AsyncTaskResult<JSONObject>(e);
            Log.e("AsyncTaskResult", "There were some problems while reciving data from server");
            e.printStackTrace();
        } finally {
            try{
                if(urlConnection !=null){
                    urlConnection.disconnect();
                }
                if(in != null){
                    in.close();
                }
            } catch (IOException ignore) {
                ignore.printStackTrace();
            }
        }



        return result;
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<JSONObject> result) {
        mCallback.onTaskComplete(result);
        super.onPostExecute(result);
    }
}

