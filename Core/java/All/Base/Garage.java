package All.Base;

import java.time.LocalDateTime;
import java.util.TreeSet;

/**
 * Класс управления коллекциями
 *
 * @author slendersold
 * @version 1.1.1
 */
public class Garage extends TreeSet<Vehicle> {
    /**
     * Поле времени
     */
    private LocalDateTime authDateTime;

    /**
     * Геттер поля authDateTime {@link Garage#authDateTime}
     *
     * @return возвращает время создания
     */
    public LocalDateTime getAuthDateTime() {
        return this.authDateTime;
    }

    /**
     * Сеттер поля authDateTime {@link Garage#authDateTime}
     *
     * @param authDateTime - тип топлива
     */
    public void setAuthDateTime(LocalDateTime authDateTime) {
        this.authDateTime = authDateTime;
    }

    /**
     * Переопределенный метод clear, очищающий весь гараж
     *
     * @see Garage#clear()
     */
    @Override
    public void clear() {
        super.clear();
        System.out.println("Это была ужасная потеря для всех нас...");
    }

}
