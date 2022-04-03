package by.morunov.test.new_system.service;

import by.morunov.test.new_system.entity.PatientProfile;
import by.morunov.test.new_system.entity.User;
import by.morunov.test.new_system.repo.PatientRepo;
import by.morunov.test.new_system.repo.UserRepo;
import by.morunov.test.new_system.service.converter.OldToNewConverter;
import by.morunov.test.old_system.clients.repo.OldClientRepo;
import by.morunov.test.old_system.note.repo.OldNoteRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

/**
 * @author Alex Morunov
 */
@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class UserAndPatientImportService {

    @Autowired
    private final PatientRepo patientRepo;

    @Autowired
    private final UserRepo userRepo;

    @Autowired
    private final OldToNewConverter oldToNewConverter;

    @Autowired
    private final OldNoteRepo oldNoteRepo;

    @Autowired
    private final OldClientRepo oldClientRepo;

    private final String CRON = "* 15 0,2,4,6,8,10,12,14,16,18,20,22 * * *";

    @Scheduled(cron = CRON)
    public void saveAllUsers() {
        try {
            for (User user : oldToNewConverter.oldToNewUsers(oldNoteRepo.getNotesFromJson())) {
                if (!userRepo.findAll().contains(user)) {
                    userRepo.save(user);
                    log.info("write new user - " + user.getLogin());
                } else {
                    log.warn(user.getLogin() + " - this user exist");
                }
            }

        } catch (IOException e) {
            log.error("user write error");
        }
    }

    @Scheduled(cron = CRON)
    public void saveAllPatients() {
        try {
            if (patientRepo.findAll().isEmpty()) {
                patientRepo.saveAll(oldToNewConverter.oldToNewAllPatient(oldClientRepo.getClientsFromJson()));
                log.info("import all old patients");
            } else if (!patientRepo.findAll().isEmpty()) {
                for (PatientProfile newPatient : patientRepo.findAll()) {
                    for (PatientProfile oldProfile : oldToNewConverter.oldToNewAllPatient(oldClientRepo.getClientsFromJson())) {
                        if (oldProfile.getOld_client_guid().compareTo(newPatient.getOld_client_guid()) != 0) {
                            patientRepo.save(oldProfile);
                            log.info("add new patient - " + oldProfile.getId());
                        } else {
                            log.warn("This patient exist");
                        }
                    }
                }
            }


        } catch (IOException e) {
            log.error("patient write error");
        }
    }

    public List<PatientProfile> getAllPatient() {
        return patientRepo.findAll();
    }


}
