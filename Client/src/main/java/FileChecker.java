import java.io.File;

public class FileChecker {
    /**
     * Метод проверки файла возвращает null, если есть проблемы
     */
    File checkFile(String address) {
        File script = new File(address);

        if (!script.exists() || !script.isFile()) {
            System.out.println(("Файл по указанному пути (" + script.getAbsolutePath() + ") не существует."));
            return null;
        }
        if (!script.canRead()) {
            System.out.println("Файл защищён от чтения.");
            return null;
        }
        if (script.length() == 0) {
            System.out.println("Скрипт не содержит команд.");
            return null;
        }
        return script;
    }
}
