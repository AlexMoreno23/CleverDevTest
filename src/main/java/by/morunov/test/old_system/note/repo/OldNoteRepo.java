package by.morunov.test.old_system.note.repo;

import by.morunov.test.old_system.note.dto.OldClientNoteDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Alex Morunov
 */
@Repository
public class OldNoteRepo {

    public List<OldClientNoteDto> getNotesFromJson() throws IOException {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(Files.readAllBytes(Paths.get("src/main/resources/data/notes/v01/notes.json")));
        stream.write(Files.readAllBytes(Paths.get("src/main/resources/data/notes/v02/notes.json")));
        stream.write(Files.readAllBytes(Paths.get("src/main/resources/data/notes/v03/notes.json")));
        stream.write(Files.readAllBytes(Paths.get("src/main/resources/data/notes/v04/notes.json")));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(stream.toByteArray(), new TypeReference<List<OldClientNoteDto>>() {
        });
    }
}
