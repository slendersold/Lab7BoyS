package com.itmo.r3135.Server.Commands;

import com.itmo.r3135.Server.DataManager;
import com.itmo.r3135.Server.Mediator;
import com.itmo.r3135.System.Command;
import com.itmo.r3135.System.ServerMessage;
import com.itmo.r3135.World.Vehicle;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс обработки комадны print_field_descending_price
 * Передаёт коллекцию, отсортированную по цене в порядке возрастания мощности двигателя
 */
public class PrintFieldAscendingEnginePowerCommand extends AbstractCommand {

    public PrintFieldAscendingEnginePowerCommand(DataManager dataManager, Mediator serverWorker) {
        super(dataManager, serverWorker);
    }

    @Override
    public ServerMessage activate(Command command) {
        SortedSet<Vehicle> vehicles = Collections.synchronizedSortedSet(dataManager.getVehicles());
        if (!vehicles.isEmpty()) {
            TreeSet<Vehicle> set = vehicles.stream().sorted((o1, o2) -> (int) ((o1.getEnginePower() - o2.getEnginePower()) * 100000)).collect(Collectors.toCollection(TreeSet::new));
            return new ServerMessage("Сортировка в порядке возрастания мощности двигателя:", set);

        } else {
            return new ServerMessage("Коллекция пуста.");
        }
    }
}
