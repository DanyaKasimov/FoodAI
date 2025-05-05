package web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web.dto.ApiErrorResponse;
import web.dto.PhotoData;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("api/v1/photo")
@Tag(name = "Управление фотографиями блюд", description = "Методы для управления фотографиями блюд")
public interface PhotoApi {

    @Operation(description = "Добавление фотографии")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Фото добавлено.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @PostMapping(value = "/analyze/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    String analyzePhoto(@RequestPart("file") MultipartFile file,
                        @PathVariable("userId") @Valid String chatId);


    @Operation(description = "Получение списка блюд за день")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Блюда получены.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorResponse.class)
                    )
            )
    })
    @GetMapping("/data/{chatId}/{date}")
    @ResponseStatus(HttpStatus.OK)
    List<PhotoData> getList(final @PathVariable @Valid String chatId, final @PathVariable @Valid LocalDate date);

}