package web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.dto.PhotoData;

import java.time.LocalDate;
import java.util.List;

@FeignClient(name = "app-service")
@Component
public interface AppService {

    @PostMapping("api/v1/user/add/{id}")
    void addUser(final @PathVariable String id);

    @DeleteMapping("api/v1/user/{id}")
    void deleteUser(final @PathVariable String id);

    @GetMapping("api/v1/photo/data/{chatId}/{date}")
    List<PhotoData> getList(final @PathVariable String chatId, final @PathVariable LocalDate date);

    @PostMapping(value = "api/v1/photo/analyze/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String uploadPhoto(@RequestPart("file") MultipartFile file,
                     @PathVariable("userId") String chatId);
}
