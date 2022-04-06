package by.morunov.test.old_system.client.service;


import by.morunov.test.old_system.client.dto.ClientDto;
import by.morunov.test.old_system.client.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    public List<ClientDto> getClients() {
        return clientRepository.getClients();
    }

    public List<String> getAgencies() {
        return clientRepository.getAgencies();
    }

}
