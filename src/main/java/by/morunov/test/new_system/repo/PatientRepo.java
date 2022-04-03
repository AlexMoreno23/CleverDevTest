package by.morunov.test.new_system.repo;

import by.morunov.test.new_system.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Alex Morunov
 */
@Repository
public interface PatientRepo extends JpaRepository<PatientProfile, Long> {


    @Query("select distinct p from PatientProfile p where p.old_client_guid = ?1")
    PatientProfile findPatientProfileByOld_client_guid(String oldGuid);

}
