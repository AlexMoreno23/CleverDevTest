package by.morunov.test.old_system.note.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Client note request")
public class ClientNoteRequestDto {
    @NotBlank
    @Schema(description = "Agency to specify search context")
    private String agency;
    @NotNull
    @Schema(description = "Note date creation from (inclusive)")
    private LocalDate dateFrom;
    @NotNull
    @Schema(description = "Note date creation to (inclusive)")
    private LocalDate dateTo;
    @NotBlank
    @Schema(description = "Agency client guid")
    private String clientGuid;
}
