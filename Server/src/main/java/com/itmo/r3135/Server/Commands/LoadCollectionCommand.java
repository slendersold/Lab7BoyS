package com.itmo.r3135.Server.Commands;

import com.itmo.r3135.Server.DataManager;
import com.itmo.r3135.Server.Mediator;
import com.itmo.r3135.System.Command;
import com.itmo.r3135.System.ServerMessage;
import com.itmo.r3135.World.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashSet;
import java.util.SortedSet;

/**
 * Класс загрузки коллекции из базы
 */
public class LoadCollectionCommand extends AbstractCommand {
    static final Logger logger = LogManager.getLogger("Loader");

    public LoadCollectionCommand(DataManager dataManager, Mediator serverWorker) {
        super(dataManager, serverWorker);
    }

    @Override
    public ServerMessage activate(Command command) {
        SortedSet<Vehicle> vehicles = Collections.synchronizedSortedSet(dataManager.getVehicles());
        try {
            logger.info("Loading collection from database: " + dataManager.getSqlManager().getConnection().getCatalog());
            PreparedStatement statement = dataManager.getSqlManager().getConnection().prepareStatement(
                    "select v.id,\n" +
                            "v.name, v.coordinates_x, \n" +
                            "v.coordinates_y, \n" +
                            "v.creation_date, \n" +
                            "v.engine_power,\n" +
                            "v.distance_travelled, \n" +
                            "v_types.vtypename, f.name as fname, u.username\n" +
                            "from vehicles v \n" +
                            "join vehicletypes v_types on v.vehicle_type_id = v_types.id \n" +
                            "join fueltypes f on f.id  = v.fuel_type_id \n" +
                            "join users u on u.id = v.user_id"
            );
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Vehicle vehicle = new Vehicle(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        new Coordinates(resultSet.getDouble("coordinates_x"), resultSet.getFloat("coordinates_y")),
                        resultSet.getTimestamp("creation_date").toLocalDateTime().atZone(ZoneId.systemDefault()),
                        resultSet.getDouble("engine_power"),
                        resultSet.getLong("distance_travelled"),
                        VehicleType.valueOf(resultSet.getString("vtypename")),
                        FuelType.valueOf(resultSet.getString("fname")));
                vehicle.setUserName(resultSet.getString("username"));
                vehicles.add(vehicle);
            }
            logger.info("Collections successfully uploaded. Added " + vehicles.size() + " items.");
            return new ServerMessage("Good.");
        } catch (SQLException e) {
            logger.fatal("SQL reading error");
            e.printStackTrace();
            return null;
        }
    }
}
