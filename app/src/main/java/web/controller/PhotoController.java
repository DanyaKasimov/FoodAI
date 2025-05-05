package web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import web.api.PhotoApi;
import web.dto.PhotoData;
import web.service.PhotoService;

import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PhotoController implements PhotoApi {

    private final PhotoService photoService;

    @Override
    public String analyzePhoto(final MultipartFile file, final String chatId) {
        log.info("Получено фото от chatId {}", chatId);

        return photoService.analyzePhoto(file, chatId);
    }

    @Override
    public List<PhotoData> getList(final String chatId, final LocalDate date) {
        log.info("Поступил запрос от chatId {} для получения списка блюда. Date: {}", chatId, date);

        return photoService.getDataByDay(chatId, date);
    }
}
