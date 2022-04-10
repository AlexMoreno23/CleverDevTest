package by.morunov.test.new_system.service;

import by.morunov.test.new_system.entity.PatientProfile;
import by.morunov.test.new_system.entity.User;
import by.morunov.test.new_system.exception.ImportException;
import by.morunov.test.new_system.repo.PatientRepo;
import by.morunov.test.new_system.repo.UserRepo;
import by.morunov.test.new_system.service.converter.OldToNewConverter;
import by.morunov.test.new_system.service.data.old_system.DataFromOldSystem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private final DataFromOldSystem data;


    private final String CRON = "* 15 0,2,4,6,8,10,12,14,16,18,20,22 * * *";


    @Scheduled(cron = CRON)
    public void saveAllUsers() {
        List<User> userListFromOldSystem = oldToNewConverter.oldToNewUsers(data.getNotes());
        try {
            for (User user : userListFromOldSystem) {
                if (!userRepo.findAll().contains(user)) {
                    userRepo.save(user);

                    log.info("write new user - " + user.getLogin());
                } else {
                    log.warn(user.getLogin() + " - this user exist");
                }
            }

        } catch (ImportException e) {
            log.error("user write error");
        }
    }

    @Scheduled(cron = CRON)
    public void saveAllPatients() {
        try {
            List<PatientProfile> patientProfilesFromOldSystem = oldToNewConverter.oldToNewAllPatient(data.getClients());
            for (PatientProfile oldProfile : patientProfilesFromOldSystem) {

                if (patientRepo.findPatientProfileByOld_client_guid(oldProfile.getOld_client_guid()) == null) {

                    patientRepo.saveAll(patientProfilesFromOldSystem);
                    log.info("import all old patients");

                } else {
                    log.warn("Patient exist ");
                }
            }

        } catch (
                ImportException e) {
            log.error("patient write error");
        }
    }


    public List<PatientProfile> getAllPatient() {
        return patientRepo.findAll();
    }


}
