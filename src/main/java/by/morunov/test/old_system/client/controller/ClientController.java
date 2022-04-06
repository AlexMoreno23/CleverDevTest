package by.morunov.test.old_system.client.controller;


import by.morunov.test.old_system.client.dto.ClientDto;
import by.morunov.test.old_system.client.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public List<ClientDto> getClients() {
        return clientService.getClients();
    }
}
