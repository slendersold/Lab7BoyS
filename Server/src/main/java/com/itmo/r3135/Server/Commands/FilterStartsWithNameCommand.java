package com.itmo.r3135.Server.Commands;

import com.itmo.r3135.Server.DataManager;
import com.itmo.r3135.Server.Mediator;
import com.itmo.r3135.System.Command;
import com.itmo.r3135.System.ServerMessage;
import com.itmo.r3135.World.Vehicle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Класс обработки комадны filter_contains_name
 * Выводит элементы, значение поля name которых начинается с заданной подстроки.
 */
public class FilterStartsWithNameCommand extends AbstractCommand {
    public FilterStartsWithNameCommand(DataManager dataManager, Mediator serverWorker) {
        super(dataManager, serverWorker);
    }

    @Override
    public ServerMessage activate(Command command) {
        SortedSet<Vehicle> vehicles = dataManager.getVehicles();
        if (vehicles.size() > 0) {
            if (!command.getString().isEmpty() && command.getString() != null) {
                TreeSet<Vehicle> vehiclesSet = vehicles.stream().filter(vehicle -> vehicle.getName()
                                .startsWith(command.getString())).collect(Collectors.toCollection(TreeSet::new));
                long findVehicles = vehicles.parallelStream()
                        .filter(vehicle -> vehicle.getName().contains(command.getString())).count();
                return new ServerMessage("Всего найдено " + findVehicles + " элементов.", vehiclesSet);
            } else {
                return new ServerMessage("Ошибка ввода имени.");
            }
        } else {
            return new ServerMessage("Коллекция пуста.");
        }
    }
}
