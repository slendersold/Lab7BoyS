package com.itmo.r3135.Server.SQLconnect;

import com.itmo.r3135.System.Command;
import com.itmo.r3135.World.FuelType;
import com.itmo.r3135.World.VehicleType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Random;

/**
 * Класс для работы с базай данных
 */
public class SQLManager {
    static final Logger logger = LogManager.getLogger("SQLManager");
    private Connection connection;

    /**
     * Проводит подключение к базе данных
     *
     * @param host         Адрес для подключения к базе
     * @param port         Порт подключения
     * @param dataBaseName Имя базы данных
     * @param user         Имя пользователя в базе
     * @param password     Пароль пользователя
     * @return Статус подключения
     */
    public boolean initDatabaseConnection(String host, int port, String dataBaseName, String user, String password) {
        logger.info("Database connect...");
        String databaseUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dataBaseName;
        try {
            logger.info("Database URL: " + databaseUrl);
            connection = DriverManager.getConnection(databaseUrl, user, password);
            logger.info("Database '" + connection.getCatalog() + "' is connected! ");
            return true;
        } catch (SQLException e) {
            logger.fatal("Error SQL connection: " + e.toString());
            return false;
        }

    }

    /**
     * Инициализирует таблицы в базе
     *
     * @return Статус инициализации
     */
    public boolean initTables() {
        try {
            Statement statement = connection.createStatement();
            //Таблица данных пользователей
            statement.execute("create table if not exists users (" +
                    "id serial primary key not null, username text unique, password_hash bytea)"
            );
            //таблица с color
            statement.execute("CREATE TABLE if not exists fuelTypes " +
                    "(Id serial primary key not null ,name varchar(20) NOT NULL UNIQUE )");
            FuelType[] fuelTypes = {FuelType.ANTIMATTER, FuelType.ELECTRICITY, FuelType.GASOLINE, FuelType.KEROSENE, FuelType.NUCLEAR};

            try {
                for (FuelType fuelType : fuelTypes)
                    statement.execute("insert into fuelTypes(name) values('" + fuelType + "') ");
            } catch (SQLException ignore) {//пока не знаю, как избежать ошибок дубликата, потому так.
            }

            //таблица с unitOfMeasure
            statement.execute("CREATE TABLE if not exists vehicleTypes " +
                    "(Id serial primary key not null ,vtypename varchar(20) NOT NULL UNIQUE )");
            VehicleType[] vehicleTypes =
                    {VehicleType.CAR, VehicleType.CHOPPER, VehicleType.MOTORCYCLE, VehicleType.SHIP, VehicleType.SPACESHIP};
            try {
                for (VehicleType vehicleType : vehicleTypes)
                    statement.execute("insert into vehicleTypes(vtypename) values('" + vehicleType + "') ");
            } catch (SQLException ignore) {//пока не знаю, как избежать ошибок дубликата, потому так.
            }
            statement.execute("CREATE TABLE if not exists vehicles ( " +
                    " id serial NOT NULL,\n" +
                    " \"name\" varchar(255) NULL,\n" +
                    " coordinates_x numeric(4) NULL,\n" +
                    " coordinates_y numeric(4) NULL,\n" +
                    " creation_date timestamp NULL,\n" +
                    " engine_power numeric(4) NULL,\n" +
                    " distance_travelled bigint NULL,\n" +
                    " vehicle_type_id int NULL,\n" +
                    " fuel_type_id int NULL,\n" +
                    " user_id int NULL\n , foreign key (user_id) references users(id)" +
                    ");");

            return true;
        } catch (SQLException e) {
            logger.fatal("Error in tables initialization.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Ищет id пользователя по заданному имени
     *
     * @param loginName Логин Пользователя
     * @return id Пользователя или -1, если пользователь не найден
     */
    public int getUserId(String loginName) {
        int userId = -1;
        try (PreparedStatement s = connection
                .prepareStatement("select id from users where username=?")) {
            s.setString(1, loginName);
            try (ResultSet resultSet = s.executeQuery()) {
                if (resultSet.next()) {
                    userId = resultSet.getInt("id");
                }
            }
        } catch (SQLException ignore) {
        }
        return userId;
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean checkAccount(Command command) {
        try (PreparedStatement statement = connection.prepareStatement(
                "select * from users where username =? and password_hash = ?"
        )) {
            statement.setString(1, command.getLogin());
            statement.setBytes(2, command.getPassword().getBytes());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            logger.error(e);
            return false;
        }
    }
}