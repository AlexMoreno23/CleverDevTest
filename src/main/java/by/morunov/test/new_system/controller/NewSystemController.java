package by.morunov.test.new_system.controller;

import by.morunov.test.new_system.entity.Note;
import by.morunov.test.new_system.entity.PatientProfile;
import by.morunov.test.new_system.service.NoteImportService;
import by.morunov.test.new_system.service.UserAndPatientImportService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Alex Morunov
 */
@RestController
@RequestMapping
@AllArgsConstructor
public class NewSystemController {

    @Autowired
    private final UserAndPatientImportService userAndPatientImportService;

    @Autowired
    private final NoteImportService noteImportService;

    @GetMapping("clients")
    public ResponseEntity<List<PatientProfile>> getAllPatient() {
        List<PatientProfile> allPatient = userAndPatientImportService.getAllPatient();
        return new ResponseEntity<>(allPatient, HttpStatus.OK);
    }

    @GetMapping("clients/{id}")
    public ResponseEntity<List<Note>> getAllNoteByPatientId(@PathVariable("id") Long id) {
        List<Note> allNoteByPatient = noteImportService.getAllByPatientId(id);
        return new ResponseEntity<>(allNoteByPatient, HttpStatus.OK);
    }

}
