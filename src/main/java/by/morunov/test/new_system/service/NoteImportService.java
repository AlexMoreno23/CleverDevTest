package by.morunov.test.new_system.service;

import by.morunov.test.new_system.entity.Note;
import by.morunov.test.new_system.repo.NoteRepo;
import by.morunov.test.new_system.service.converter.OldToNewConverter;
import by.morunov.test.old_system.note.repo.OldNoteRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alex Morunov
 */
@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class NoteImportService {

    @Autowired
    private final NoteRepo noteRepo;

    @Autowired
    private final OldToNewConverter oldToNewConverter;

    @Autowired
    private final OldNoteRepo oldNoteRepo;

    private final String CRON = "* 15 0,2,4,6,8,10,12,14,16,18,20,22 * * *";


    public List<Note> getAllByPatientId(Long id) {
        return noteRepo.findByPatient_id(id);
    }

    @Scheduled(initialDelay = 5000, cron = CRON)
    public void saveNoteToDb() {
        try {
            List<Note> notesActivePatient = oldToNewConverter.oldToNewAllNote(oldNoteRepo.getNotesFromJson()).stream()
                    .filter(x -> (x.getPatient_id().getStatus_id() == 200)).collect(Collectors.toList());
            if (noteRepo.findAll().isEmpty()) {
                noteRepo.saveAll(notesActivePatient);
                log.info("import all old notes");
            } else if (!noteRepo.findAll().isEmpty()) {
                for (Note newNote : noteRepo.findAll()) {
                    for (Note oldNote : notesActivePatient) {
                        if (newNote.getOld_note_guid().compareTo(oldNote.getOld_note_guid()) != 0) {
                            noteRepo.save(oldNote);
                        } else if (newNote.getLast_modified_date_time().compareTo(oldNote.getLast_modified_date_time()) < 0) {
                            noteRepo.updateNote(oldNote);
                            log.info("update note - " + newNote.getId());
                        }
                    }
                }
            } else {
                log.warn("this note exist");
            }
        } catch (IOException e) {
            log.error("Write note error");
        }

    }

}
