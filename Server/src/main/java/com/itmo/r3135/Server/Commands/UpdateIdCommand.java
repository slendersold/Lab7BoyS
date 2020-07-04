package com.itmo.r3135.Server.Commands;

import com.google.gson.JsonSyntaxException;
import com.itmo.r3135.Server.DataManager;
import com.itmo.r3135.Server.Mediator;
import com.itmo.r3135.System.Command;
import com.itmo.r3135.System.VehicleWithStatus;
import com.itmo.r3135.System.ServerMessage;
import com.itmo.r3135.World.Vehicle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.SortedSet;

/**
 * Класс обработки комадны update
 * Заменяет в колеекции элемент с заданным id.
 */
public class UpdateIdCommand extends AbstractCommand {
    public UpdateIdCommand(DataManager dataManager, Mediator serverWorker) {
        super(dataManager, serverWorker);
    }

    @Override
    public ServerMessage activate(Command command) {

        int userId = dataManager.getSqlManager().getUserId(command.getLogin());
        if (userId == -1) return new ServerMessage("Ошибка авторизации!");
        try {
            int id = command.getIntValue();
            Vehicle newVehicle = command.getVehicle();
            if (newVehicle.checkNull()) {
                return new ServerMessage("Элемент не удовлетворяет требованиям коллекции");
            } else {
                if (updateVehicleSQL(newVehicle, id) != -1) {
                    SortedSet<Vehicle> vehicles = Collections.synchronizedSortedSet(dataManager.getVehicles());
                    for (Vehicle vehicleForUpdate : vehicles) {
                        if (vehicleForUpdate.getId() == id) {
                            vehicleForUpdate.updateVehicle(newVehicle);
                            dataManager.addChange(vehicleForUpdate, VehicleWithStatus.ObjectStatus.UPDATE);
                            return new ServerMessage("Элемент успешно обновлён.");
                        }
                    }
                    return new ServerMessage("Как такое могло произойти?! В базе обновлён, а в коллекции - нет?!");
                } else
                    return new ServerMessage("При замене элементов что-то пошло не так.\n" +
                            " Возможно, объект Вам не принаджежит");
            }
        } catch (JsonSyntaxException ex) {
            return new ServerMessage("Возникла ошибка при замене элемента");
        }
    }

    private int updateVehicleSQL(Vehicle vehicle, int id) {
        try {
            PreparedStatement statement = dataManager.getSqlManager().getConnection().prepareStatement(
                    "UPDATE vehicles " +
                            "SET name = ?, coordinates_x=?, coordinates_y=?, creation_date=?, engine_power=?, distance_travelled=?, vehicle_type_id= (select id from vehicletypes where vtypename = ?), fuel_type_id= (select id from fueltypes where name = ?) " +
                            "WHERE id = ? returning id"
            );
            statement.setString(1, vehicle.getName());
            statement.setDouble(2, vehicle.getCoordinates().getX());
            statement.setDouble(3, vehicle.getCoordinates().getY());
            statement.setTimestamp(4, new Timestamp(vehicle.getCreationDate().toEpochSecond() * 1000));
            statement.setDouble(5, vehicle.getEnginePower());
            statement.setLong(6, vehicle.getDistanceTravelled());
            statement.setString(7, vehicle.getVehicleType().toString());
            statement.setString(8, vehicle.getFuelType().toString());
            statement.setInt(9, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
                System.out.println("Updated Vehicle id: " + id);
            }
        } catch (SQLException lal) {
            lal.printStackTrace();
        }
        return id;
    }
}
