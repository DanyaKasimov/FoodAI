package web.command.impl;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import web.MessageSender;
import web.StateHandler;
import web.client.AppService;
import web.command.Command;
import web.constants.CommandType;
import web.constants.Messages;

@Slf4j
@Component
@RequiredArgsConstructor
public class StopCommand implements Command {

    private final MessageSender messageSender;

    private final AppService appService;

    private final StateHandler stateHandler;

    @Override
    public boolean supports(String command) {
        return CommandType.STOP.getCommand().equalsIgnoreCase(command);
    }

    @Override
    public void execute(Update update) {
        String chatId = update.message().chat().id().toString();

        if (!stateHandler.isActive(chatId)) {
            messageSender.send(chatId, "Вы не зарегистрированы.");
            return;
        }

        try {
            appService.deleteUser(chatId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        messageSender.send(chatId, Messages.STOPPED);

        stateHandler.deleteActive(chatId);

    }
}
