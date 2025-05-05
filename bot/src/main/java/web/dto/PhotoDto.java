package web.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PhotoDto {

    private UUID id;

    private String photoUrl;

    private LocalDateTime createdAt;
}