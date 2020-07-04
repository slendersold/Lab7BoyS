package com.itmo.r3135.Server.Commands;

import com.itmo.r3135.Server.DataManager;
import com.itmo.r3135.Server.Mediator;
import com.itmo.r3135.System.Command;
import com.itmo.r3135.System.VehicleWithStatus;
import com.itmo.r3135.System.ServerMessage;
import com.itmo.r3135.World.Vehicle;

import java.sql.*;
import java.util.HashSet;
import java.util.SortedSet;

/**
 * Класс обработки комадны add
 */
public class AddCommand extends AbstractCommand {
    public AddCommand(DataManager dataManager, Mediator serverWorker) {
        super(dataManager, serverWorker);
    }


    @Override
    public ServerMessage activate(Command command) {
        int userId = dataManager.getSqlManager().getUserId(command.getLogin());
        if (userId == 0) return new ServerMessage("Ошибка авторизации!");
        Vehicle addVehicle = command.getVehicle();
        addVehicle.setCreationDate(java.time.ZonedDateTime.now());
        if (addVehicle.checkNull()) {
            return new ServerMessage(Vehicle.printRequest());
        } else {
            int id = addObjSql(addVehicle, userId);
            addVehicle.setUserName(command.getLogin().split("@")[0]);
            addVehicle.setId(id);
            if (id == -1) return new ServerMessage("Ошибка добавления элемента в базу данных");
            else {
                SortedSet<Vehicle> vehicles = dataManager.getVehicles();
                if (vehicles.add(addVehicle)) {
                    dataManager.addChange(addVehicle, VehicleWithStatus.ObjectStatus.ADD);
                    return new ServerMessage("Элемент c id " + id + " успешно добавлен.");
                } else {
                    return new ServerMessage("Ошибка добавления элемента в коллекцию. Но. В базу он добавлени" +
                            "Сообщите об этом случае в техническую поддержку.('info')");
                }
            }
        }
    }

    private int addObjSql(Vehicle vehicle, int userId) {
        return addVehicleSQL(vehicle, userId);
    }

    private int addVehicleSQL(Vehicle vehicle, int userId) {
        int id = -1;
        try {
            PreparedStatement statement = dataManager.getSqlManager().getConnection().prepareStatement(
                    "insert into vehicles" +
                            "(name, coordinates_x, coordinates_y, creation_date, engine_power, distance_travelled, vehicle_type_id, fuel_type_id, user_id) " +
                            "values (?,?,?,?,?,?,(select id from vehicletypes where vtypename = ?),(select id from fueltypes where name = ?),?) returning id"
            );
            statement.setString(1, vehicle.getName());
            statement.setDouble(2, vehicle.getCoordinates().getX());
            statement.setDouble(3, vehicle.getCoordinates().getY());
            statement.setTimestamp(4, new Timestamp(vehicle.getCreationDate().toEpochSecond() * 1000));
            statement.setDouble(5, vehicle.getEnginePower());
            statement.setLong(6, vehicle.getDistanceTravelled());
            if (vehicle.getVehicleType() != null) {
                statement.setString(7, vehicle.getVehicleType().toString());
            } else statement.setNull(7, Types.VARCHAR);
            if (vehicle.getFuelType() != null) {
                statement.setString(8, vehicle.getFuelType().toString());
            } else statement.setNull(8, Types.VARCHAR);
            statement.setInt(9, userId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
                logger.info("Added Vehicle id: " + id);
            }
        } catch (SQLException lal) {
            lal.printStackTrace();
        }
        return id;
    }
}
