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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClientNoteRepository {
    public static final String AGENCY_NOT_FOUND_MESSAGE_TEMPLATE = "Agency \"%s\" is not found";

    private final Path path = Paths.get("src/main/resources/data/notes/");

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


    public static List<Path> listFiles(Path path) throws IOException {

        List<Path> result;
        try (Stream<Path> walk = Files.walk(path)) {
            result = walk.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }

        return result;

    }

    @SneakyThrows
    public List<ClientNoteDto> getAllNotes() {
        List<Path> paths = listFiles(path);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        paths.forEach(p -> {
            try {
                stream.write(Files.readAllBytes(Paths.get(p.toString())));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(stream.toByteArray(), new TypeReference<List<ClientNoteDto>>() {
        });

    }


}
