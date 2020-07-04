import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Класс для извлечения команд из скрипта.
 */
public class VirtualStack {
    FileChecker checkFile = new FileChecker();
    private final ArrayList<File> activeScriptList;
    private final ArrayList<String> commandStack;
    private final ArrayList<String> lastRemove;
    private final TreeMap<ArrayList<File>, String> derevo;
    private File currentFile;

    {
        activeScriptList = new ArrayList<>();
        commandStack = new ArrayList<>(100000);
        lastRemove = new ArrayList<>();
        derevo = new TreeMap<>();
    }

    /**
     * Составляет список комадн из текста файла
     *
     * @param scriptAddress Адрес файла скрипта
     * @return Лист команд
     * @throws IOException
     */
    protected ArrayList stackGenerate(String scriptAddress) throws IOException {
        int i = 0;
        if (checkFile.checkFile(scriptAddress) == null) {
            return commandStack;
        }
        commandStack.addAll(i, readFile(checkFile.checkFile(scriptAddress)));
        int stackSize = commandStack.size();
        while (i < stackSize) {
            if (commandCheck(commandStack.get(i))) {
                scriptAddress = getAddressScript(commandStack.get(i));
                commandStack.remove(commandStack.get(i));
                while (getExecuteFromLastFile(activeScriptList.get(activeScriptList.size() - 1)).equals(lastRemove)) {
                    lastRemove.clear();
                    lastRemove.add(activeScriptList.get(activeScriptList.size() - 1).getAbsolutePath());
                    activeScriptList.remove(activeScriptList.size() - 1);

                }
                if (checkFile.checkFile(scriptAddress) != null) {
                    insertScript(readFile(checkFile.checkFile(scriptAddress)), i);
                }
                stackSize = commandStack.size();
            } else i++;
        }
        return commandStack;
    }

    /**
     * Преобразует файлы скрипта в лист строк, проверяет циклы
     *
     * @param script Адрес скрипта
     * @return Лист строк
     * @throws IOException
     */
    private LinkedList readFile(File script) throws IOException {
        currentFile = script;
        LinkedList<String> CommandList = new LinkedList<>();
        if (activeScriptList.indexOf(script) == -1) {
            activeScriptList.add(script);
            lastRemove.clear();
            try (BufferedReader scriptReader = new BufferedReader(new FileReader(script))) {
                System.out.println("Анализ файла " + script.getAbsolutePath());
                String scriptCommand = scriptReader.readLine();
                while (scriptCommand != null) {
                    if (commandCheck(scriptCommand)) {
                        scriptCommand = relativeToAbsolutePath(scriptCommand);
                    }
                    CommandList.addLast(scriptCommand);
                    scriptCommand = scriptReader.readLine();
                }
            }
        } else {
            System.out.println("Обнаружен цикл!");
            lastRemove.add(script.getAbsolutePath());
        }
        return CommandList;
    }

    private ArrayList getExecuteFromLastFile(File script) throws IOException {
        ArrayList<String> CommandList = new ArrayList<>();

        try (BufferedReader scriptReader = new BufferedReader(new FileReader(script))) {
            String scriptCommand = scriptReader.readLine();
            while (scriptCommand != null) {
                if (commandCheck(scriptCommand)) {
                    scriptCommand = relativeToAbsolutePath(scriptCommand);
                    CommandList.add(getAddressScript(scriptCommand));
                }
                scriptCommand = scriptReader.readLine();
            }
        }
        return CommandList;
    }

    /**
     * Заменяет отновительный путь на абсолютный
     */
    private String relativeToAbsolutePath(String nextExecute) {
        String nextFilePath = nextExecute.trim().split(" ", 2)[1];
        if (nextFilePath != null) {
            File nextFile = new File(nextFilePath);
            if (!nextFile.isAbsolute()) {
                return "execute_script " + currentFile.getAbsolutePath().replace(currentFile.getName(), nextFile.getPath());
            }
        }
        return nextExecute;
    }

    private String getAddressScript(String command) {
        String[] trimScriptCommand;
        trimScriptCommand = command.trim().split(" ", 2);
        return trimScriptCommand[1];
    }

    private Boolean commandCheck(String command) {
        if (command != null) {
            String[] trimScriptCommand;
            trimScriptCommand = command.trim().split(" ", 2);
            return trimScriptCommand[0].equals("execute_script");
        }
        return false;
    }

    private void insertScript(LinkedList commandList, Integer index) {
        commandStack.addAll(index, commandList);
    }


}