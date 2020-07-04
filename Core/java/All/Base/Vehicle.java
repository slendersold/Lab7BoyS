package All.Base;

import java.time.ZonedDateTime;

/**
 * Класс, хранящий основную информацию он транспортном средстве
 * Реализует интерфейс Comparable с типом Vehicle
 *
 * @author slendersold
 * @version 1.1.1
 */
public class Vehicle implements Comparable<Vehicle> {

    /**
     * Поле id
     */
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    /**
     * Поле name
     */
    private String name; //Поле не может быть null, Строка не может быть пустой
    /**
     * Поле coordinates
     */
    private Coordinates coordinates; //Поле не может быть null
    /**
     * Поле creationDate
     */
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    /**
     * Поле enginePower
     */
    private Double enginePower; //Поле может быть null, Значение поля должно быть больше 0
    /**
     * Поле distanceTravelled
     */
    private Long distanceTravelled; //Поле не может быть null, Значение поля должно быть больше 0
    /**
     * Поле type
     */
    private VehicleType type; //Поле может быть null
    /**
     * Поле fuelType
     */
    private FuelType fuelType; //Поле может быть null
    /**
     * Поле Формата
     */
    private final static String format = "%-30s%-20s%-20s%n";

    /**
     * Конструкор класса Vehicle
     *
     * @param name              - имя
     * @param x                 - координата X
     * @param y                 - координата Y
     * @param distanceTravelled - пробег
     * @see Vehicle#Vehicle(String, Double, float, Long)
     */
    public Vehicle(String name, Double x, float y, Long distanceTravelled) {
        this.name = name;
        this.coordinates = new Coordinates(x, y);
        this.distanceTravelled = distanceTravelled;
        this.id = name.hashCode() + 1;
        ZonedDateTime.now();
    }

    /**
     * Геттер поля id {@link Vehicle#id}
     *
     * @return возвращает id
     */
    public long getId() {
        return id;
    }

    /**
     * Сеттер поля id {@link Vehicle#id}
     *
     * @param id - id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Геттер поля name {@link Vehicle#name}
     *
     * @return возвращает имя
     */
    public String getName() {
        return name;
    }

    /**
     * Сеттер поля name {@link Vehicle#name}
     *
     * @param name - имя
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Геттер поля coordinates {@link Vehicle#coordinates}
     *
     * @return возвращает координаты
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Сеттер поля coordinates {@link Vehicle#coordinates}
     *
     * @param coordinates - координаты
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Геттер поля creationDate {@link Vehicle#creationDate}
     *
     * @return возвращает дату создания
     */
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Сеттер поля creationDate {@link Vehicle#creationDate}
     *
     * @param creationDate - время создания
     */
    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Геттер поля enginePower {@link Vehicle#enginePower}
     *
     * @return возвращает мощь двигателя
     */
    public Double getEnginePower() {
        return enginePower;
    }

    /**
     * Сеттер поля enginePower {@link Vehicle#enginePower}
     *
     * @param enginePower - мощь двигателя
     */
    public void setEnginePower(Double enginePower) {
        this.enginePower = enginePower;
    }

    /**
     * Геттер поля distanceTravelled {@link Vehicle#distanceTravelled}
     *
     * @return возвращает пробег
     */
    public Long getDistanceTravelled() {
        return distanceTravelled;
    }

    /**
     * Сеттер поля distanceTravelled {@link Vehicle#distanceTravelled}
     *
     * @param distanceTravelled - пробег
     */
    public void setDistanceTravelled(Long distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    /**
     * Геттер поля type {@link Vehicle#type}
     *
     * @return возвращает тип
     */
    public VehicleType getType() {
        return type;
    }

    /**
     * Сеттер поля type {@link Vehicle#type}
     *
     * @param type - тип
     */
    public void setType(String type) {
        if (type.equals("CAR") || type.equals("SHIP") || type.equals("MOTORCYCLE") || type.equals("CHOPPER") || type.equals("SPACESHIP")) {
            this.type = VehicleType.valueOf(type);
        } else {
            System.out.println("Такое в гараж не помещается (Тип ТС не был установлен)");
        }
    }

    /**
     * Геттер поля fuelType {@link Vehicle#fuelType}
     *
     * @return возвращает тип топлива
     */
    public FuelType getFuelType() {
        return fuelType;
    }

    /**
     * Сеттер поля fuelType {@link Vehicle#fuelType}
     *
     * @param fuel - тип топлива
     */
    public void setFuel(String fuel) {
        if (fuel.equals("GASOLINE") || fuel.equals("KEROSENE") || fuel.equals("ELECTRICITY") || fuel.equals("NUCLEAR") || fuel.equals("ANTIMATTER")) {
            this.fuelType = FuelType.valueOf(fuel);
        } else {
            System.out.println("Что-то мне нехорошо, ты точно ТО влил? (Тип топлива не был установлен)");
        }
    }

    /**
     * Переопределенный метод compareTo, сравнивающий значение
     * полей Y двух экземпляров класса Coordinates
     *
     * @param vehicle - агрегат
     * @return возвращает число для последующего сравнения
     */
    @Override
    public int compareTo(Vehicle vehicle) {
        int diff = 0;


        if (this.id - vehicle.getId() > 0) {
            diff++;
        } else if (this.id - vehicle.getId() < 0) {
            diff--;
        }

        diff += String.valueOf(this.getName()).compareTo(String.valueOf(vehicle.getName()));
        diff += String.valueOf(this.getCreationDate()).compareTo(String.valueOf(vehicle.getCreationDate()));
        diff += String.valueOf(this.getDistanceTravelled()).compareTo(String.valueOf(vehicle.getDistanceTravelled()));
        diff += String.valueOf(this.getEnginePower()).compareTo(String.valueOf(vehicle.getEnginePower()));

        return diff;
    }
    /**
     * Проверяет элемент на сообтетвтвовение требованиям коллекции
     */
    public boolean checkNull() {
        try {
            return name == null || name.isEmpty() || coordinates == null ||
                    coordinates.getX() == null || coordinates.getX() < -379 ||
                    creationDate == null;
        } catch (Exception ex) {
            System.out.println("В процессе проверки объекта произошла ошибка");
            return true;
        }
    }

    public void printCheck() {
        String s = String.format(format, "Поле", "Значение", "Требование");
        if (this == null) s += String.format(format, "Vehicle", "null", "Ты шо, дорашка?");
        else {
            if (name == null || name.isEmpty())
                s += String.format(format, "String name", name, "Поле не может быть null, Строка не может быть пустой");
            if (coordinates == null)
                s += String.format(format, "Coordinates coordinates", coordinates, "Поле не может быть null");
            else {
                if (coordinates.getX() == null || coordinates.getX() < -379) {
                    s += String.format(format, "(coordinates)Long y ", coordinates.getY(), "Максимальное значение поля: 82, Поле не может быть null");
                }
            }
        }
        System.out.println(s);
    }

    public static String printRequest() {
        return ("------------------------\n" +
                "Требования к элементу:\n" +
                "------------------------\n" +
                "Vehicle: {\n" +
                "    String name --- Поле не может быть null, Строка не может быть пустой\n" +
                "    Coordinates coordinates; --- Поле не может быть null\n" +
                "    Double enginePower price --- Поле может быть null, Значение поля должно быть больше 0\n" +
                "    Long distanceTravelled --- Поле не может быть null, Значение поля должно быть больше 0\n" +
                "    VehicleType vehicleType --- Поле может быть null\n" +
                "    FuelType fuelType --- Поле может быть null\n" +
                "}\n" +
                "Coordinates: {\n" +
                "    Double x --- Значение поля должно быть больше -379, Поле не может быть null\n" +
                "    float y --- Поле не может быть null\n" +
                "}\n");
    }
}

