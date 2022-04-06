package by.morunov.test.old_system.client.repository;


import by.morunov.test.old_system.client.dto.ClientDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientRepository {
    private final ObjectMapper objectMapper;

    private List<ClientDto> clients;
    private List<String> agencies;

    @Value("classpath:${base.path}/data/clients/clients.json")
    private Resource resourceFile;

    @PostConstruct
    private void initClients() {
        final List<ClientDto> data = readClientsFile();
        initClients(data);
    }

    @SneakyThrows
    private List<ClientDto> readClientsFile() {
        final File file = resourceFile.getFile();
        return objectMapper.readValue(file, new TypeReference<>() {
        });
    }

    private void initClients(List<ClientDto> data) {
        this.clients = data;
        this.agencies = data.stream()
                .map(ClientDto::getAgency)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<ClientDto> getClients() {
        return clients;
    }

    public List<String> getAgencies() {
        return agencies;
    }

}
