package com.itmo.r3135.Server;

import com.itmo.r3135.Server.SQLconnect.SQLManager;
import com.itmo.r3135.System.VehicleWithStatus;
import com.itmo.r3135.World.Vehicle;

import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Класс, хранящий всё необходимое для использования команд.
 */
public class DataManager {
    private final LocalDateTime dateInitialization = LocalDateTime.now();
    private final SortedSet<VehicleWithStatus> changeVehicles = Collections.synchronizedSortedSet(new TreeSet<>());
    private SortedSet<Vehicle> vehicles = Collections.synchronizedSortedSet(new TreeSet<>());
    private LocalDateTime dateChange = LocalDateTime.now();
    private SQLManager sqlManager;

    public DataManager() {
    }


    public SQLManager getSqlManager() {
        return sqlManager;
    }

    public void setSqlManager(SQLManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public LocalDateTime getDateChange() {
        return dateChange;
    }

    public SortedSet<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(SortedSet<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void updateDateChange() {
        this.dateChange = LocalDateTime.now();
    }

    public void addChange(Vehicle vehicle, VehicleWithStatus.ObjectStatus status) {
        updateDateChange();
        changeVehicles.add(new VehicleWithStatus(vehicle, status));

    }

    public SortedSet<VehicleWithStatus> getChangeVehicles() {
        return changeVehicles;
    }

    @Override
    public String toString() {
        return "------------------------" +
                "\nИнформация о коллекции:" +
                "\n------------------------" +
                "\n Количество элементов коллекции: " + Collections.synchronizedSortedSet(vehicles).size() +
                "\n Дата инициализации: " + dateInitialization +
                "\n Дата последнего изменения: " + dateChange;
    }

    public static class ActiveSendList {
        private static final SortedSet<SocketAddress> list = java.util.Collections.synchronizedSortedSet(new TreeSet<>());

        public static boolean isSending(SocketAddress address) {
            boolean status = list.contains(address);
            return status;
        }

        public static void setSending(SocketAddress address, boolean status) {
            if (status) {
                list.add(address);
            } else list.remove(address);
        }
    }
}
