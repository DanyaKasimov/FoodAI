package web.constants;

import lombok.Getter;

@Getter
public enum CommandType {
    START("/start"),
    LIST("/list"),
    STOP("/stop");

    private final String command;

    CommandType(String command) {
        this.command = command;
    }
}
