package by.morunov.test.old_system.note.service;


import by.morunov.test.old_system.client.service.ClientService;
import by.morunov.test.old_system.note.dto.ClientNoteDto;
import by.morunov.test.old_system.note.dto.ClientNoteRequestDto;
import by.morunov.test.old_system.note.service.repository.ClientNoteRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Collections;
import java.util.List;

@Service
public class ClientNoteService {
    @Value("${base.path}")
    private final String basePath;
    private final ResourceLoader resourceLoader;
    private final ClientNoteRepository clientNoteRepository;
    private final ClientService clientService;
    private final ObjectMapper objectMapper;

    public ClientNoteService(@Value("${base.path}") String basePath, ResourceLoader resourceLoader, ClientNoteRepository clientNoteRepository,
                             ClientService clientService, ObjectMapper objectMapper) {
        this.basePath = basePath;
        this.resourceLoader = resourceLoader;
        this.clientNoteRepository = clientNoteRepository;
        this.clientService = clientService;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    private void loadClientNotes() {
        for (final String agencyName : clientService.getAgencies()) {
            clientNoteRepository.initAgencyClientNotes(agencyName, readAgencyClientNotes(agencyName));
        }
    }

    @SneakyThrows
    private List<ClientNoteDto> readAgencyClientNotes(final String agency) {
        if (!StringUtils.isAlphanumeric(agency)) {
            return Collections.emptyList();
        }
        final String path = String.format("classpath:%s/data/notes/%s/notes.json", basePath, agency);
        final Resource resource = resourceLoader.getResource(path);
        if (resource.exists()) {
            return objectMapper.readValue(resource.getFile(), new TypeReference<>() {
            });
        }
        return Collections.emptyList();
    }

    public List<ClientNoteDto> getNotes(final ClientNoteRequestDto requestDto) {
        return clientNoteRepository.getNotesByClientGuidAndAgencyInDateRange(
                requestDto.getClientGuid(), requestDto.getAgency(),
                requestDto.getDateFrom(), requestDto.getDateTo()
        );
    }

    public List<ClientNoteDto> getAllNotes() {
        return clientNoteRepository.getAllNotes();

    }
}
