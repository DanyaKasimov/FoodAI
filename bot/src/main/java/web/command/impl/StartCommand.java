package web.command.impl;

import com.pengrad.telegrambot.model.Update;
import feign.FeignException;
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
public class StartCommand implements Command {

    private final MessageSender messageSender;

    private final AppService appService;

    private final StateHandler stateHandler;

    @Override
    public boolean supports(String command) {
        return CommandType.START.getCommand().equalsIgnoreCase(command);
    }

    @Override
    public void execute(Update update) {
        String chatId = update.message().chat().id().toString();

        if (stateHandler.isActive(chatId)) {
            messageSender.send(chatId, "Вы уже зарегистрированы.");
            return;
        }

        try {
            appService.addUser(chatId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        messageSender.send(chatId, Messages.WELCOME);

        stateHandler.addActive(chatId);

    }
}
