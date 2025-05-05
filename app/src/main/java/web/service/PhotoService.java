package web.service;

import org.springframework.web.multipart.MultipartFile;
import web.dto.PhotoData;
import web.entity.Photo;
import web.entity.User;

import java.time.LocalDate;
import java.util.List;

public interface PhotoService {

    Photo savePhoto(User user, String path);

    String analyzePhoto(MultipartFile file, String chatId);

    void deleteAllUserPhotos(final User user);

    List<PhotoData> getDataByDay(final String chatId, final LocalDate date);
}
