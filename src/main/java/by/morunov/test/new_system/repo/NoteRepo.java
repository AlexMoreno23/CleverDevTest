package by.morunov.test.new_system.repo;

import by.morunov.test.new_system.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alex Morunov
 */
@Repository
public interface NoteRepo extends JpaRepository<Note, Long> {

    List<Note> findAll();

    @Query("select p from Note p where p.patient_id = ?1")
    List<Note> findByPatient_id(Long id);

    @Query("select n from Note n where n.old_note_guid = ?1")
    Note findByOld_note_guid(String guid);

    @Modifying
    @Query("update Note n set n.last_modified_date_time = :#{#note.last_modified_date_time}, " +
            "n.last_modified_by_user_id = :#{#note.last_modified_by_user_id}," +
            "n.note = :#{#note.note}")
    void updateNote(@Param("note") Note note);
}
