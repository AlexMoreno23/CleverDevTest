package by.morunov.test.new_system.service;

import by.morunov.test.new_system.entity.Note;
import by.morunov.test.new_system.exception.ImportException;
import by.morunov.test.new_system.repo.NoteRepo;
import by.morunov.test.new_system.service.converter.OldToNewConverter;
import by.morunov.test.new_system.service.data.old_system.DataFromOldSystem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private final DataFromOldSystem data;

    private final String CRON = "* 15 0,2,4,6,8,10,12,14,16,18,20,22 * * *";


    public List<Note> getAllByPatientId(Long id) {
        return noteRepo.findByPatient_id(id);
    }

    @Scheduled(cron = CRON)
    public void saveNoteToDb() {
        try {
            List<Note> notesActivePatient = oldToNewConverter.oldToNewAllNote(data.getNotes()).stream()
                    .filter(x -> (x.getPatient_id().getStatus_id() == 200))
                    .collect(Collectors.toList());

            for (Note oldNote : notesActivePatient) {
                if (noteRepo.findByOld_note_guid(oldNote.getOld_note_guid()) == null) {

                    noteRepo.save(oldNote);
                    log.info("import note - " + oldNote.getOld_note_guid());

                } else if (noteRepo.findByOld_note_guid(oldNote.getOld_note_guid()).getLast_modified_date_time()
                        .compareTo(oldNote.getLast_modified_date_time()) < 0) {

                    noteRepo.updateNote(oldNote);
                    log.info("update note - " + oldNote.getOld_note_guid());
                }
            }
        } catch (ImportException e) {
            log.error("Write note error");
        }

    }

}
