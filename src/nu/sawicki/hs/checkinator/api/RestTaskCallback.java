package nu.sawicki.hs.checkinator.api;

import org.json.JSONObject;

/**
 * Class definition for a callback to be invoked when the HTTP request
 * representing the REST API Call completes.
 */
abstract class RestTaskCallback{
    /**
     * Called when the HTTP request completes.
     *
     * @param result The result of the HTTP request.
     */
    public abstract void onTaskComplete(AsyncTaskResult<JSONObject> result);
}