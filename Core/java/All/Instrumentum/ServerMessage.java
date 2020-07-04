package All.Instrumentum;

import All.Base.Vehicle;
import java.io.Serializable;
import java.util.ArrayList;

public class ServerMessage implements Serializable {
    private String message;
    private ArrayList<Vehicle> vehicles;

    public ServerMessage(String message) {
        this.message = message;
    }
    public ServerMessage(String message, ArrayList<Vehicle> vehicles){
        this.message = message;
        this.vehicles = vehicles;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public String getMessage() {
        return message;
    }


}
