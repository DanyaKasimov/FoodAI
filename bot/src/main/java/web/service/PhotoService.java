package web.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import web.client.AppService;
import web.config.BotConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Slf4j
@Component
@RequiredArgsConstructor
public class PhotoService {

    private final BotConfig botConfig;

    private final AppService appService;

    private final String BASE_URL = "https://api.telegram.org/file/bot";

    public String sendPhoto(Update update, TelegramBot telegramBot) {
        PhotoSize[] photos = update.message().photo();
        PhotoSize largestPhoto = photos[photos.length - 1];
        String fileId = largestPhoto.fileId();

        GetFile getFile = new GetFile(fileId);
        File file = telegramBot.execute(getFile).file();

        String fileUrl = BASE_URL + botConfig.telegramToken() + "/" + file.filePath();

        try (InputStream inputStream = new URL(fileUrl).openStream()) {
            byte[] bytes = inputStream.readAllBytes();
            MultipartFile multipartFile = new MockMultipartFile("file", "photo.jpg", "image/jpeg", bytes);

            try {
                return appService.uploadPhoto(multipartFile, update.message().chat().id().toString());
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } catch (IOException e) {
            log.error("Ошибка загрузки файла: {}", e.getMessage());
            throw new RuntimeException("Ошибка загрузки файла");
        }
    }
}
