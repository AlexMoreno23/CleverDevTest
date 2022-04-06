package by.morunov.test.old_system.note.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Client note")
public class ClientNoteDto {
    @Schema(description = "Note text")
    private String comments;
    @Schema(description = "Note guid")
    private String guid;
    @Schema(description = "Note last modified datetime", format = "yyyy-MM-dd HH:mm:ss", example = "2020-11-15T09:48:32")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedDateTime;
    @Schema(description = "Client guid")
    private String clientGuid;
    @Schema(description = "Created datetime", format = "yyyy-MM-dd HH:mm:ss", example = "2020-01-31 22:55:15")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDateTime;
    @Schema(description = "User that created note")
    private String loggedUser;
}
