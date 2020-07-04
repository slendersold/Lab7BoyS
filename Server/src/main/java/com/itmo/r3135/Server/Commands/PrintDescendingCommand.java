package com.itmo.r3135.Server.Commands;

import com.itmo.r3135.Server.DataManager;
import com.itmo.r3135.Server.Mediator;
import com.itmo.r3135.System.Command;
import com.itmo.r3135.System.ServerMessage;
import com.itmo.r3135.World.Vehicle;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс обработки комадны print_descending
 * Передаёт коллекцию, отсортированную по цене в порядке убывания
 */
public class PrintDescendingCommand extends AbstractCommand {

    public PrintDescendingCommand(DataManager dataManager, Mediator serverWorker) {
        super(dataManager, serverWorker);
    }

    @Override
    public ServerMessage activate(Command command) {
        SortedSet<Vehicle> vehicles = Collections.synchronizedSortedSet(dataManager.getVehicles());
        if (!vehicles.isEmpty()) {
            TreeSet<Vehicle> set = vehicles.stream().sorted((o1, o2) -> (o2.compareTo(o1)) * 100000).collect(Collectors.toCollection(TreeSet::new));
            return new ServerMessage("Сортировка в порядке возрастания мощности двигателя:", set);

        } else {
            return new ServerMessage("Коллекция пуста.");
        }
    }
}