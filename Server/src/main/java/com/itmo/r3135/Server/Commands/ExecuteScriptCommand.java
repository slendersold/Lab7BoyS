package com.itmo.r3135.Server.Commands;

import com.itmo.r3135.Server.DataManager;
import com.itmo.r3135.Server.Mediator;
import com.itmo.r3135.System.Command;
import com.itmo.r3135.System.ServerMessage;
import com.itmo.r3135.World.Vehicle;

import java.util.Collections;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Класс обработки комадны execute_script
 * Выполняет скрипт полученный в виде листа комманд
 */
public class ExecuteScriptCommand extends AbstractCommand {
    public ExecuteScriptCommand(DataManager dataManager, Mediator serverWorker) {
        super(dataManager, serverWorker);
    }


    @Override
    public ServerMessage activate(Command command) {
        SortedSet<Vehicle> oldVehicles = Collections.synchronizedSortedSet(new TreeSet<>(dataManager.getVehicles()));

        try {
            for (Command executeCommand : command.getExecuteCommands()) {
                executeCommand.setLoginPassword(command.getLogin(), command.getPassword());
                serverWorker.processing(executeCommand);
            }
            return new ServerMessage("Скрипт был выполнен.");
        } catch (Exception e) {
            dataManager.setVehicles(oldVehicles);
            return new ServerMessage("Скрипт не был выполнен. Коллекция не изменилась. Если проблема повторится, обратитесь в тех. поддержку, которой нет.");
        }
    }
}
