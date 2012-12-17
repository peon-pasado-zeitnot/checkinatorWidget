package nu.sawicki.hs.checkinator.api;

public class User {

    public String getLogin() {
        return login;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public String getPrettyTime() {
        return prettyTime;
    }



    private final String login;
    private final Integer timestamp;
    private final String prettyTime;

    public User(String login, int timestamp, String prettyTime){
        this.login = login;
        this.timestamp = timestamp;
        this.prettyTime = prettyTime;

    }


}
