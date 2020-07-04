package com.itmo.r3135.Server.Commands;


import com.itmo.r3135.Server.DataManager;
import com.itmo.r3135.Server.Mediator;
import com.itmo.r3135.System.Command;
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
 * Класс обработки комадны remove_lower
 * Удаляет из коллекции все элементы пользователя меньшие, чем заданный
 */
public class RemoveLowerCommand extends AbstractCommand {

    public RemoveLowerCommand(DataManager dataManager, Mediator serverWorker) {
        super(dataManager, serverWorker);
    }

    @Override
    public ServerMessage activate(Command command) {
        int userId = dataManager.getSqlManager().getUserId(command.getLogin());
        if (userId == -1) return new ServerMessage("Ошибка авторизации!");

        try {
            PreparedStatement statement = dataManager.getSqlManager().getConnection().prepareStatement(
                    "delete from vehicles where user_id = ? and coordinates_y < ? returning vehicles.id"
            );
            statement.setInt(1, userId);
            statement.setDouble(2, command.getVehicle().getCoordinates().getY());
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Long> ids = new ArrayList<>();
            while (resultSet.next())
                ids.add(resultSet.getLong("id"));
            SortedSet<Vehicle> vehicles = Collections.synchronizedSortedSet(dataManager.getVehicles());
            vehicles.removeAll((vehicles.parallelStream().filter(vehicle -> ids.contains(vehicle.getId()))
                    .collect(Collectors.toCollection(HashSet::new))));
            return new ServerMessage("Все элементы меньше " + command.getVehicle().getCoordinates().getY() + " удалены.");
        } catch (SQLException e) {
            return new ServerMessage("Ошибка поиска объектов пользователя в базе.");
        }
    }
}
