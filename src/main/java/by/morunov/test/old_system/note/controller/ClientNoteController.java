package by.morunov.test.old_system.note.controller;


import by.morunov.test.old_system.note.dto.ClientNoteDto;
import by.morunov.test.old_system.note.dto.ClientNoteRequestDto;
import by.morunov.test.old_system.note.service.ClientNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notes")
public class ClientNoteController {
    private final ClientNoteService clientNoteService;

    @PostMapping
    public List<ClientNoteDto> getNotes(@RequestBody @Validated @NotNull final ClientNoteRequestDto clientNoteRequestDto) {
        return clientNoteService.getNotes(clientNoteRequestDto);
    }

    @GetMapping("/all")
    public List<ClientNoteDto> getAllNotes() {
        return clientNoteService.getAllNotes();
    }
}
