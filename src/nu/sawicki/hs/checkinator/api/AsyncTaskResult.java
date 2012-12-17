package nu.sawicki.hs.checkinator.api;


public class AsyncTaskResult<T> {
    private final T result;
    private final Exception error;



    public T getResult() {
        return result;
    }
    public Exception getError() {
        return error;
    }


    public AsyncTaskResult(T result) {
        this.result = result;
        error = null;
    }


    public AsyncTaskResult(Exception error) {
        this.error = error;
        result = null;
    }
}