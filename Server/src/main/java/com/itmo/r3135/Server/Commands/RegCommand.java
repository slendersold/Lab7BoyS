package com.itmo.r3135.Server.Commands;

import com.itmo.r3135.Server.DataManager;
import com.itmo.r3135.Server.Mediator;
import com.itmo.r3135.System.Command;
import com.itmo.r3135.System.ServerMessage;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Класс обработки комадны reg
 * Регистритует нового пользователя
 */
public class RegCommand extends AbstractCommand {
    public RegCommand(DataManager dataManager, Mediator serverWorker) {
        super(dataManager, serverWorker);
    }

    @Override
    public ServerMessage activate(Command command) {
        try {
            PreparedStatement statement = dataManager.getSqlManager().getConnection().prepareStatement(
                    "insert into users (password_hash, username) values (?, ?) "
            );
            statement.setString(2, command.getLogin());
            statement.setBytes(1, command.getPassword().getBytes());
            try {
                statement.execute();
            } catch (SQLException e) {
                logger.error("Попытка добавления по существующему ключу");
                return new ServerMessage("Пользователь с именем " + command.getLogin() + " уже существует!",false);
            }
            return new ServerMessage("Successful registration.");

        } catch (SQLException e) {
            logger.error("Бда, бда SQLException");
            return new ServerMessage("Ошибка регистрации",false);
        }
    }
}
