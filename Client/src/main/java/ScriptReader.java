import All.Instrumentum.Command;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Класс выделения набора команд из скрипта
 */
public class ScriptReader {
    static FileChecker fck = new FileChecker();

    /**
     * Проверяет и анализирует файл скрипта.
     *
     * @param script Адрес файла скрипта
     * @return Лист команд для сервера
     */
    public static ArrayList<Command> read(String script) {
        ArrayList<Command> executeCommands = new ArrayList<>();
        VirtualStack virtualStack = new VirtualStack();
        try {
            fck.checkFile(script);
            System.out.println("Начинается анализ скрипта. Это может занять некоторое время");
            Agregator agregator = new Agregator();
            ArrayList<String> executeStringCommands = virtualStack.stackGenerate(script);
            System.out.println("Анализ содердимого команд:");
            for (String executeStringCommand : executeStringCommands) {
                Command executeCommand = agregator.getCommandFromString(executeStringCommand);
                if (executeCommand != null)
                    executeCommands.add(executeCommand);
            }
            System.out.println("Анализ завершён.");
            if (!executeStringCommands.isEmpty()) {
                return executeCommands;
            } else {
                System.out.println("Невозможно прочитать скрипт или скрипт пуст.");
                return null;
            }
        } catch (IOException e) {
            System.out.println("Ошибка работы с файлами.");
            return executeCommands;
        }
    }
}