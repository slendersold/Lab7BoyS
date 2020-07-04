package All.Instrumentum;

import java.io.Serializable;

/**
 * Enum-Класс доступных комадн сервера
 */
public enum CommandList implements Serializable {
    LOAD("LOAD"),
    HELP("help"),
    INFO("info"),
    ADD("add"),
    SHOW("show"),
    UPDATE("update"),
    REMOVE_BY_ID("remove_by_id"),
    CLEAR("clear"),
    EXECUTE_SCRIPT("execute_script"),
    ADD_IF_MAX("add_if_max"),
    ADD_IF_MIN("add_if_min"),
    REMOVE_LOWER("remove_lower"),
    FILTER_STARTS_WITH_NAME("filter_starts_with_name"),
    PRINT_FIELD_ASCENDING_ENGINE_POWER("print_field_ascending_engine_power"),
    CHECK("check_connection"),
    SAVE("save"),
    PRINT_DESCENDING("print_descending"),

    GET_UPDATES("get_updates"),
    PING("ping"),
    CODE("code"),
    LOGIN("login"),
    REG("reg");


    private String toText;

    private CommandList(String toText) {
        this.toText = toText;
    }
}
