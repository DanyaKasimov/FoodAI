package web.command;

import com.pengrad.telegrambot.model.Update;

public interface Command {
    boolean supports(String command);
    void execute(Update update);
}
