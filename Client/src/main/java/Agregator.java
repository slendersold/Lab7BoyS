import All.Base.Vehicle;
import All.Instrumentum.Command;
import All.Instrumentum.CommandList;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

public class Agregator {
    private Gson gson;

    {
        gson = new Gson();
    }

    public Command getCommandFromString(String stringCommand) {
        Command command;
        int id;
        Vehicle vehicle;
        String[] trimCommand = stringCommand.trim().split(" ", 2);
        try {
            try {
                switch (trimCommand[0]) {
                    case "":
                        System.out.println("Команда отсутствует");
                        command = null;
                        break;
                    case "help":
                        command = new Command(CommandList.HELP);
                        break;
                    case "info":
                        command = new Command(CommandList.INFO);
                        break;
                    case "show":
                        command = new Command(CommandList.SHOW);
                        break;
                    case "add":
                        vehicle = gson.fromJson(trimCommand[1], Vehicle.class);
                        if (!vehicle.checkNull()) {
                            command = new Command(CommandList.ADD, vehicle);
                        } else {
                            System.out.println("Элемент " + vehicle.getName() + " не удовлетворяет требованиям коллекции.");
                            vehicle.printCheck();
                            command = null;
                        }
                        break;
                    case "update":
                        id = Integer.valueOf(trimCommand[1].trim().split(" ", 2)[0]);
                        vehicle = gson.fromJson(trimCommand[1].trim().split(" ", 2)[1], Vehicle.class);
                        if (!vehicle.checkNull())
                            command = new Command(CommandList.UPDATE, vehicle, id);
                        else {
                            System.out.println("Элемент " + vehicle.getName() + " не удовлетворяет требованиям коллекции.");
                            vehicle.printCheck();
                            command = null;
                        }
                        break;
                    case "remove_by_id":
                        id = Integer.valueOf(trimCommand[1]);
                        command = new Command(CommandList.REMOVE_BY_ID, id);
                        break;
                    case "clear":
                        command = new Command(CommandList.CLEAR);
                        break;
                    case "execute_script":
                        ArrayList<Command> commands = ScriptReader.read(trimCommand[1]);
                        if (!commands.isEmpty()) {
                            command = new Command(CommandList.EXECUTE_SCRIPT, commands);
                        } else {
                            System.out.println("Скрипт пуст.");
                            command = null;
                        }
                        break;
                    case "exit":
                        System.exit(0);
                        command = null;
                        break;
                    case "add_if_max":
                        vehicle = gson.fromJson(trimCommand[1], Vehicle.class);
                        if (!vehicle.checkNull()) command = new Command(CommandList.ADD_IF_MAX, vehicle);
                        else {
                            System.out.println("Элемент " + vehicle.getName() + " не удовлетворяет требованиям коллекции.");
                            vehicle.printCheck();
                            command = null;
                        }
                    case "add_if_min":
                        vehicle = gson.fromJson(trimCommand[1], Vehicle.class);
                        if (!vehicle.checkNull()) command = new Command(CommandList.ADD_IF_MIN, vehicle);
                        else {
                            System.out.println("Элемент " + vehicle.getName() + " не удовлетворяет требованиям коллекции.");
                            vehicle.printCheck();
                            command = null;
                        }
                        break;
                    case "remove_lower":
                        vehicle = gson.fromJson(trimCommand[1], Vehicle.class);
                        if (!vehicle.checkNull()) return new Command(CommandList.REMOVE_LOWER, vehicle);
                        else {
                            System.out.println("Элемент " + vehicle.getName() + " не удовлетворяет требованиям коллекции.");
                            System.out.println(Vehicle.printRequest());
                            command = null;
                        }
                        break;
                    case "filter_starts_with_name":
                        command = new Command(CommandList.FILTER_STARTS_WITH_NAME, trimCommand[1]);
                        break;
                    case "print_field_ascending_engine_power":
                        command = new Command(CommandList.PRINT_FIELD_ASCENDING_ENGINE_POWER);
                        break;
                    case "print_descending":
                        command = new Command(CommandList.PRINT_DESCENDING);
                        break;
                    default:
                        System.out.println("Неопознанная команда. Наберите 'help' для получения доступных команд.");
                        command = null;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Где-то проблема с форматом записи числа.Команда не выполнена");
                command = null;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Отсутствует аргумент.");
            command = null;
        } catch (JsonSyntaxException e) {
            System.out.println("Ошибка синтаксиса json.");
            command = null;
        }
        return command;
    }
}
