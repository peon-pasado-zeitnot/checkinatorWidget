package nu.sawicki.hs.checkinator.api;


import org.json.JSONObject;

public class Client {
    public void getCheckins(final GetResponseCallback callback)
    {
        String restUrl = Utils.constructRestUrlForCheckins();
        new GetTask(restUrl, new RestTaskCallback (){
            @Override
            public void onTaskComplete(AsyncTaskResult<JSONObject> result)  {
                Checkins checkins = Utils.parseResponseAsCheckins(result);
                callback.onDataReceived(checkins);
            }
        }).execute();
    }

}
