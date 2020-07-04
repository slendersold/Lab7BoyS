package com.itmo.r3135.Server.Commands;

import com.itmo.r3135.Server.DataManager;
import com.itmo.r3135.Server.Mediator;
import com.itmo.r3135.System.Command;
import com.itmo.r3135.System.CommandList;
import com.itmo.r3135.System.ServerMessage;
import com.itmo.r3135.World.Vehicle;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.SortedSet;

/**
 * Класс обработки комадны add_if_max
 * Добавляет новый элемент в коллекцию, если его значение больше, чем у наибольшего элемента этой коллекции.
 */
public class AddIfMaxCommand extends AbstractCommand {
    public AddIfMaxCommand(DataManager dataManager, Mediator serverWorker) {
        super(dataManager, serverWorker);
    }

    @Override
    public ServerMessage activate(Command command) {
        Vehicle addVehicle = command.getVehicle();

        SortedSet<Vehicle> vehicles = Collections.synchronizedSortedSet(dataManager.getVehicles());
        try {
            if (vehicles.size() != 0) {
                Vehicle maxElem = vehicles.stream().max(Vehicle::compareTo).get();
                if (addVehicle.compareTo(maxElem) > 0) {
                    Command addCommand = new Command(CommandList.ADD, addVehicle);
                    addCommand.setLoginPassword(command.getLogin(), command.getPassword());
                    return serverWorker.processing(addCommand);
                } else {
                    return new ServerMessage("Элемент не максимальный!");
                }
            } else {
                return new ServerMessage("Коллекция пуста, максимальный элемент отсутствует.");
            }
        } catch (SQLException ex) {
            return new ServerMessage("Возникла ошибка SQL Json. Элемент не был добавлен");
        }
    }
}
