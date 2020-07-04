package All.Base;

/**
 * Класс, экземпляры которого призваны хранить координаты транспортного средства.
 * Реализует интерфейс Comparable с типом Сoordinates
 *
 * @author slendersold
 * @version 1.1.1
 */
public class Coordinates implements Comparable<Coordinates> {
    /**
     * Поле координаты X
     */
    private Double x; //Значение поля должно быть больше -379, Поле не может быть null
    /**
     * Поле координаты Y
     */
    private float y;

    /**
     * Конструкор класса Coordinates
     *
     * @param x - координата X
     * @param y - координата Y
     * @see Coordinates#Coordinates(Double, float)
     */
    public Coordinates(Double x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Геттер поля координаты Х {@link Coordinates#x}
     *
     * @return возвращает координату Х
     */
    public Double getX() {
        return x;
    }

    /**
     * Сеттер поля координаты X {@link Coordinates#x}
     *
     * @param x - координата X
     */
    public void setX(Double x) {
        this.x = x;
    }

    /**
     * Геттер поля координаты Y {@link Coordinates#y}
     *
     * @return возвращает координату Y
     */
    public float getY() {
        return y;
    }

    /**
     * Сеттер поля координаты Y {@link Coordinates#y}
     *
     * @param y - координата Y
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Переопределенный метод compareTo, сравнивающий сзначение
     * полей Y двух экземпляров класса Coordinates
     *
     * @param o - поле Coordinates
     * @return возвращает -1, если передаваемое значение больше вызываемого,
     * 0, если эквивалентны,
     * 1 если передаваемое значение меньше вызываемого
     */
    @Override
    public int compareTo(Coordinates o) {
        if (this.getY() == o.getY()) {
            return 0;
        } else {
            if (this.getY() - o.getY() > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
