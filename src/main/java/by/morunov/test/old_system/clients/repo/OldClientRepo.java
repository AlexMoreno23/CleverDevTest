package by.morunov.test.old_system.clients.repo;

import by.morunov.test.old_system.clients.dto.OldClientDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Alex Morunov
 */
@Repository
public class OldClientRepo {

    public List<OldClientDto> getClientsFromJson() throws IOException {

        byte[] mapData = Files.readAllBytes(Paths.get("src/main/resources/data/clients/clients.json"));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper.readValue(mapData, new TypeReference<List<OldClientDto>>() {
        });
    }


}
