package web;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import web.command.Command;
import web.constants.CommandType;
import web.service.PhotoService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

@Slf4j
@Service
public class TelegramBotService {

    private final CommandExecutor commandExecutor;

    private final MessageSender messageSender;

    private final StateHandler stateHandler;

    private final TelegramBot telegramBot;

    private final PhotoService photoService;

    public TelegramBotService(BotCore botCore,
                              CommandExecutor commandExecutor,
                              MessageSender messageSender,
                              StateHandler stateHandler,
                              PhotoService photoService) {
        this.commandExecutor = commandExecutor;
        this.messageSender = messageSender;
        this.stateHandler = stateHandler;
        this.photoService = photoService;

        this.telegramBot = botCore.getBot();
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(this::processUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void processUpdate(Update update) {
        String chatId = update.message().chat().id().toString();
        String text = update.message().text();
        try {
            messageSender.deleteCommandMessages(chatId);
            messageSender.deleteLastMessage(chatId);

            if (update.message().photo() != null && update.message().photo().length != 0) {
                messageSender.rememberLastMessageId(chatId, update.message().messageId());

                var processingMessage = messageSender.send(chatId, "Ваш запрос обрабатывается.");

                String answer = photoService.sendPhoto(update, telegramBot);

                messageSender.deleteMessage(chatId, processingMessage.message().messageId());

                var resultMessage = messageSender.send(chatId, answer);

                messageSender.rememberLastMessageId(chatId, resultMessage.message().messageId());
                return;
            }

            if (text == null) {
                return;
            }

            if (!stateHandler.isActive(chatId) && !text.equalsIgnoreCase(CommandType.START.getCommand())) {
                messageSender.send(chatId, "Введите команду /start");
                return;
            }

            commandExecutor.execute(update);
        } catch (Exception e) {
            log.error("Непредвиденная ошибка: {}", e.getMessage());
            messageSender.send(chatId, "Произошла ошибка. Попробуйте снова.");
        }
    }
}
