package web.command.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import web.MessageSender;
import web.StateHandler;
import web.client.AppService;
import web.command.Command;
import web.constants.CommandType;
import web.dto.DescriptionDto;
import web.dto.PhotoData;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListCommand implements Command {

    private final MessageSender messageSender;
    private final AppService appService;
    private final StateHandler stateHandler;

    @Override
    public boolean supports(String command) {
        return CommandType.LIST.getCommand().equalsIgnoreCase(command);
    }

    @Override
    public void execute(Update update) {
        String chatId = update.message().chat().id().toString();
        String text = update.message().text();
        Integer userMessageId = update.message().messageId();

        if (!stateHandler.isActive(chatId)) {
            SendResponse response = messageSender.send(chatId, "Вы не зарегистрированы.");
            messageSender.rememberCommandMessageId(chatId, response.message().messageId());
            return;
        }

        String[] parts = text.trim().split("\\s+");
        LocalDate date;
        if (parts.length < 2) {
            date = LocalDate.now();
        } else {
            try {
                date = LocalDate.parse(parts[1]);
            } catch (Exception e) {
                SendResponse err = messageSender.send(chatId, "Неверный формат даты. Используйте ГГГГ-ММ-ДД");
                messageSender.rememberCommandMessageId(chatId, err.message().messageId());
                return;
            }
        }


        List<PhotoData> dataList = appService.getList(chatId, date);

        if (dataList == null) {
            SendResponse none = messageSender.send(chatId, "Нет данных за выбранную дату.");
            messageSender.rememberCommandMessageId(chatId, none.message().messageId());
            return;
        }

        for (PhotoData data : dataList) {
            String photoPath = data.getPhoto().getPhotoUrl();
            File photoFile = new File(photoPath);

            if (!photoFile.exists()) {
                throw new RuntimeException("Файл не найден: " + photoPath);
            }

            StringBuilder caption = new StringBuilder();
            DescriptionDto d = data.getDescription();
            if (d.getTitle() != null && d.getWeight() != null && d.getCalories() != null &&
                    d.getProteins() != null && d.getCarbs() != null && d.getFats() != null) {

                caption.append("Блюдо: ").append(d.getTitle()).append("\n")
                        .append("Вес: ").append(d.getWeight()).append("\n")
                        .append("Калории: ").append(d.getCalories()).append("\n")
                        .append("Белки: ").append(d.getProteins()).append("\n")
                        .append("Углеводы: ").append(d.getCarbs()).append("\n")
                        .append("Жиры: ").append(d.getFats());

            } else {
                caption.append(d.getMessage());
            }

            SendResponse sent = messageSender.sendPhoto(chatId, photoFile, caption.toString());
            messageSender.rememberCommandMessageId(chatId, sent.message().messageId());
        }

        messageSender.rememberCommandMessageId(chatId, userMessageId);


    }
}