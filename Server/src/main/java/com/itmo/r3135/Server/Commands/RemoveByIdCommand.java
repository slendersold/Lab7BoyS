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
import java.util.Collections;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.stream.Collectors;

public class RemoveByIdCommand extends AbstractCommand {

    /**
     * Класс обработки комадны remove_by_id
     */
    public RemoveByIdCommand(DataManager dataManager, Mediator serverWorker) {
        super(dataManager, serverWorker);
    }

    /**
     * Удаляет элемент по его id.
     */
    @Override
    public ServerMessage activate(Command command) {
        int userId = dataManager.getSqlManager().getUserId(command.getLogin());
        if (userId == -1) return new ServerMessage("Ошибка авторизации!");

        SortedSet<Vehicle> vehicles = Collections.synchronizedSortedSet(dataManager.getVehicles());
        int startSize = vehicles.size();
        if (vehicles.size() > 0) {
            int id = command.getIntValue();
            try {
                PreparedStatement statement = dataManager.getSqlManager().getConnection().prepareStatement(
                        "delete from vehicles where user_id = ? and id = ? returning vehicles.id"
                );
                statement.setInt(1, userId);
                statement.setInt(2, id);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    HashSet v = (vehicles.parallelStream().filter(vehicle -> vehicle.getId() == id)
                            .collect(Collectors.toCollection(HashSet::new)));
                    vehicles.removeAll(v);
                    for (Object pp : v) {
                        Vehicle sending = new Vehicle();
                        sending.setId(((Vehicle) pp).getId());
                        dataManager.addChange(sending, VehicleWithStatus.ObjectStatus.REMOVE);
                    }
                }
            } catch (SQLException e) {
                return new ServerMessage(" Ошибка работы с базой данных");
            }
            if (startSize == vehicles.size()) {
                return new ServerMessage("Элемент с id " + id + " не существует. Или принадлежит не Вам.");
            }
            dataManager.updateDateChange();
            return new ServerMessage("Элемент коллекции успешно удалён.");
        } else {
            return new ServerMessage("Коллекция пуста.");
        }
    }
}
