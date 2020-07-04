package com.itmo.r3135.Server.Commands;

import com.itmo.r3135.Server.DataManager;
import com.itmo.r3135.Server.Mediator;
import com.itmo.r3135.System.Command;
import com.itmo.r3135.System.VehicleWithStatus;
import com.itmo.r3135.System.ServerMessage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Класс формирует сообщение о измененияв в коллекции пользователю
 */
public class GetUpdatesCommand extends AbstractCommand {

    public GetUpdatesCommand(DataManager dataManager, Mediator serverWorker) {
        super(dataManager, serverWorker);
    }

    @Override
    public ServerMessage activate(Command command) {
        if (command.getLastUpdate() != null) {
            LocalDateTime lastUpdate = command.getLastUpdate();
            try {
                if (dataManager.getDateChange().compareTo(lastUpdate) > 0) {
                    SortedSet<VehicleWithStatus> changeVehicles = dataManager.getChangeVehicles();
                    TreeSet<VehicleWithStatus> changeVehiclesSet = changeVehicles.stream()
                            .filter(changeVehicle -> changeVehicle.getAddDateTime().compareTo(lastUpdate) > 0)
                            .collect(Collectors.toCollection(TreeSet::new));
                    return new ServerMessage(dataManager.getDateChange(), changeVehiclesSet);
                } else return new ServerMessage("В последнее время изменений не произошло.");
            } finally {
            }
        } else return new ServerMessage("Некорректное время последнего обновления.");

    }
}
