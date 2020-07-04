package com.itmo.r3135.Server.Commands;

import com.itmo.r3135.Server.DataManager;
import com.itmo.r3135.Server.Mediator;
import com.itmo.r3135.System.Command;
import com.itmo.r3135.System.ServerMessage;
import com.itmo.r3135.World.Vehicle;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Класс обработки комадны show
 * Передаёт все элементы коллекции
 */

public class ShowCommand extends AbstractCommand {

    public ShowCommand(DataManager dataManager, Mediator serverWorker) {
        super(dataManager, serverWorker);
    }

    @Override
    public ServerMessage activate(Command command) {
        SortedSet<Vehicle> vehicles = Collections.synchronizedSortedSet(new TreeSet<>(dataManager.getVehicles()));
        return new ServerMessage(vehicles, dataManager.getDateChange());
    }
}
