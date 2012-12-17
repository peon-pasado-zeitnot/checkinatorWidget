package nu.sawicki.hs.checkinator.api;


import java.util.ArrayList;

public class Checkins {

    private final ArrayList<User> users;
    private final Integer unknownDevices;
    private final String errorsDescription;

    public Checkins(ArrayList<User> users, int unknownDevices){
        this.users = users;
        this.unknownDevices = unknownDevices;
        this.errorsDescription = null;
    }

    public Checkins(String errorsDescription){
        this.errorsDescription = errorsDescription;
        users = null;
        unknownDevices = null;

    }

    public ArrayList<User> getUsers(){
        return users;
    }

    public int getUnknownDevices(){
        return unknownDevices;
    }

    public String getErrorsDescription(){
        return errorsDescription;
    }
}
