package web.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import web.client.AiClient;
import web.dto.DescriptionDto;
import web.dto.PhotoAnalyzeDto;
import web.dto.PhotoData;
import web.dto.PhotoDto;
import web.entity.Description;
import web.entity.Photo;
import web.entity.User;
import web.exception.NoDataFoundException;
import web.repository.DescriptionRepository;
import web.repository.PhotoRepository;
import web.repository.UserRepository;
import web.service.PhotoService;
import web.util.FileHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    private final DescriptionRepository descriptionRepository;

    private final UserRepository userRepository;

    private final AiClient aiClient;

    private final FileHandler fileHandler;

    @Override
    public Photo savePhoto(final User user, final String path) {
        Photo photo = Photo.builder()
                .user(user)
                .photoUrl(path)
                .build();

        return photoRepository.save(photo);
    }

    @Override
    public String analyzePhoto(final MultipartFile file, final String chatId) {
        val user = userRepository.findByUserId(chatId).orElseThrow(() -> new NoDataFoundException("Пользователь не найден."));

        val dateTime = LocalDateTime.now().toLocalTime();
        val fileName = user.getUserId() + "_" + dateTime + ".jpg";
        val filePath = fileHandler.loadFile(file, fileName);

        val photo = savePhoto(user, filePath);

        val content = aiClient.photoAnalyze(new PhotoAnalyzeDto(filePath));

        DescriptionDto dto = DescriptionDto.parsePhotoAnalysis(content);
        Description description = Description.builder()
                .photo(photo)
                .carbs(dto.getCarbs())
                .title(dto.getTitle())
                .fats(dto.getFats())
                .calories(dto.getCalories())
                .proteins(dto.getProteins())
                .weight(dto.getWeight())
                .message(dto.getMessage())
                .build();

        descriptionRepository.save(description);

        return dto.toString();
    }

    @Override
    public void deleteAllUserPhotos(final User user) {
        List<Photo> userPhotos = photoRepository.findAllByUser(user);

        if (userPhotos.isEmpty()) return;

        userPhotos.forEach(photo -> {
            Description description = descriptionRepository.findByPhoto(photo);
            descriptionRepository.delete(description);
            fileHandler.deleteFile(photo.getPhotoUrl());
        });

        photoRepository.deleteAll(userPhotos);
    }


    @Override
    public List<PhotoData> getDataByDay(final String chatId, final LocalDate date) {
        User user = userRepository.findByUserId(chatId)
                .orElseThrow(() -> new NoDataFoundException("Пользователь не найден."));

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusNanos(1);

        List<Photo> photos = photoRepository.findAllByUserAndCreatedAtBetween(user, startOfDay, endOfDay);

        if (photos.isEmpty()) {
            return null;
        }

        return photos.stream()
                .map(photo -> {
                    PhotoDto photoDto = PhotoDto.builder()
                            .id(photo.getId())
                            .photoUrl(photo.getPhotoUrl())
                            .createdAt(photo.getCreatedAt())
                            .build();
                    Description description = descriptionRepository.findByPhoto(photo);
                    DescriptionDto descriptionDto = DescriptionDto.builder()
                            .title(description.getTitle())
                            .calories(description.getCalories())
                            .weight(description.getWeight())
                            .carbs(description.getCarbs())
                            .message(description.getMessage())
                            .fats(description.getFats())
                            .proteins(description.getProteins())
                            .build();
                    return PhotoData.builder()
                            .photo(photoDto)
                            .description(descriptionDto)
                            .build();
                })
                .toList();
    }
}