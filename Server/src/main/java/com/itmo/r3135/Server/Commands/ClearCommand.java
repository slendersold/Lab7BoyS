package com.itmo.r3135.Server.Commands;

import com.itmo.r3135.Server.DataManager;
import com.itmo.r3135.Server.Mediator;
import com.itmo.r3135.System.Command;
import com.itmo.r3135.System.VehicleWithStatus;
import com.itmo.r3135.System.ServerMessage;
import com.itmo.r3135.World.Vehicle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.stream.Collectors;

/**
 * Класс обработки комадны clear
 * Удаляет все элементы пользователя
 */
public class ClearCommand extends AbstractCommand {
    public ClearCommand(DataManager dataManager, Mediator serverWorker) {
        super(dataManager, serverWorker);
    }


    @Override
    public ServerMessage activate(Command command) {
        int userId = dataManager.getSqlManager().getUserId(command.getLogin());
        if (userId == -1) return new ServerMessage("Ошибка авторизации!");

        try {
            PreparedStatement statement = dataManager.getSqlManager().getConnection().prepareStatement(
                    "delete from vehicles where user_id = ? returning vehicles.id"
            );
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Long> ids = new ArrayList<>();
            while (resultSet.next())
                ids.add(resultSet.getLong("id"));
            if (!ids.isEmpty()) {
                SortedSet<Vehicle> vehicles = Collections.synchronizedSortedSet(dataManager.getVehicles());
                HashSet v = (vehicles.parallelStream().filter(vehicle -> ids.contains(vehicle.getId()))
                        .collect(Collectors.toCollection(HashSet::new)));
                vehicles.removeAll(v);
                for (Object vv : v) {
                    Vehicle sending = new Vehicle();
                    sending.setId(((Vehicle) vv).getId());
                    dataManager.addChange(sending, VehicleWithStatus.ObjectStatus.REMOVE);
                }
            }
        } catch (SQLException e) {
            return new ServerMessage("Ошибка поиска объектов пользователя в базе.");
        }
        return new ServerMessage("Ваши объекты удалены.");

    }
}
