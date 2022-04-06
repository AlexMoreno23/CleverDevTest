package by.morunov.test.old_system.note.service.repository;


import by.morunov.test.old_system.note.dto.ClientNoteDto;
import by.morunov.test.old_system.note.utils.DateCompareUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientNoteRepository {
    public static final String AGENCY_NOT_FOUND_MESSAGE_TEMPLATE = "Agency \"%s\" is not found";

    private final Map<String, List<ClientNoteDto>> notesByAgency = new ConcurrentHashMap<>();

    public List<ClientNoteDto> getNotesByClientGuidAndAgencyInDateRange(final String clientGuid, final String agency,
                                                                        final LocalDate from, final LocalDate to) {
        if (!notesByAgency.containsKey(agency)) {
            throw new IllegalArgumentException(String.format(AGENCY_NOT_FOUND_MESSAGE_TEMPLATE, agency));
        }
        final List<ClientNoteDto> clientNotes = notesByAgency.get(agency);
        return getNotesByPeriodAndClientGuid(clientNotes, clientGuid, from, to);
    }

    public void initAgencyClientNotes(final String agency, final List<ClientNoteDto> clientNotes) {
        notesByAgency.put(agency, List.copyOf(clientNotes));
    }

    private List<ClientNoteDto> getNotesByPeriodAndClientGuid(final List<ClientNoteDto> notes, final String clientGuid,
                                                              final LocalDate from, final LocalDate to) {
        return notes.stream()
                .filter(note -> Objects.equals(clientGuid, note.getClientGuid())
                        && DateCompareUtils.isDateInPeriod(note.getCreatedDateTime().toLocalDate(),
                        from, to))
                .collect(Collectors.toList());
    }


    @SneakyThrows
    public List<ClientNoteDto> getAllNotes() {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(Files.readAllBytes(Paths.get("src/main/resources/data/notes/v01/notes.json")));
        stream.write(Files.readAllBytes(Paths.get("src/main/resources/data/notes/v02/notes.json")));
        stream.write(Files.readAllBytes(Paths.get("src/main/resources/data/notes/v03/notes.json")));
        stream.write(Files.readAllBytes(Paths.get("src/main/resources/data/notes/v04/notes.json")));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(stream.toByteArray(), new TypeReference<List<ClientNoteDto>>() {
        });
    }

}
