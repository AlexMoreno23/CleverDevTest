package by.morunov.test.old_system.clients.dto;

import by.morunov.test.new_system.entity.ClientStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Alex Morunov
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OldClientDto {

    @JsonProperty
    private String agency;
    @JsonProperty
    private String guid;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private ClientStatusEnum status;
    @JsonProperty
    private LocalDate dob;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDateTime;
}
