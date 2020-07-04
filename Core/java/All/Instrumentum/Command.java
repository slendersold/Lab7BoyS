package All.Instrumentum;

import All.Base.Vehicle;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Класс запроса на сервер
 */
public class Command implements Serializable {
    private final CommandList command;
    private LocalDateTime lastUpdate;
    private ArrayList<Command> executeCommands;
    private Vehicle vehicle;
    private String string;
    private int intValue;
    private String login;
    private String password;

    {
        login = "";
        password = "";
    }

    public Command(CommandList command) {
        this.command = command;
    }

    public Command(CommandList command, Vehicle vehicle) {
        this.command = command;
        this.vehicle = vehicle;
    }

    public Command(CommandList command, String string) {
        this.command = command;
        this.string = string;
    }

    public Command(CommandList command, int intValue) {
        this.command = command;
        this.intValue = intValue;
    }

    public Command(CommandList command, Vehicle vehicle, int intValue) {
        this.vehicle = vehicle;
        this.command = command;
        this.intValue = intValue;
    }

    public Command(CommandList command, ArrayList<Command> executeCommands) {
        this.command = command;
        this.executeCommands = executeCommands;
    }

    public void setLoginPassword(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Command> getExecuteCommands() {
        return executeCommands;
    }

    public CommandList getCommand() {
        return command;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getIntValue() {
        return intValue;
    }

    public String getString() {
        return string;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
