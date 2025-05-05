package web;

import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import web.command.Command;
import web.exception.UnknownCommandException;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandExecutor {
    private final List<Command> commands;

    private final MessageSender messageSender;

    public void execute(Update update) {
        String text = update.message().text();
        String chatId = update.message().chat().id().toString();
        String command = text.trim().split(" ")[0];
        try {
            commands.stream()
                .filter(s -> s.supports(command))
                .findFirst()
                .orElseThrow(() -> new UnknownCommandException("Неизвестная команда. Введите /help."))
                .execute(update);
        } catch (UnknownCommandException e) {
            log.warn("Неизвестная команда: {}", text);
            messageSender.send(chatId, e.getMessage());
        }

    }
}
