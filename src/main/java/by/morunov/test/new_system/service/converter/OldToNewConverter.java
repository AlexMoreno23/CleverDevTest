package by.morunov.test.new_system.service.converter;

import by.morunov.test.new_system.entity.ClientStatusEnum;
import by.morunov.test.new_system.entity.PatientProfile;
import by.morunov.test.new_system.entity.User;
import by.morunov.test.new_system.entity.Note;
import by.morunov.test.new_system.repo.PatientRepo;
import by.morunov.test.new_system.repo.UserRepo;
import by.morunov.test.old_system.clients.dto.OldClientDto;
import by.morunov.test.old_system.note.dto.OldClientNoteDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Alex Morunov
 */
@AllArgsConstructor
@Component
public class OldToNewConverter {

    @Autowired
    private final PatientRepo patientRepo;

    @Autowired
    private final UserRepo userRepo;

    public int converterStatus(ClientStatusEnum clientStatusEnum) {
        int result = 0;
        if (clientStatusEnum.name().equals("ACTIVE")) {
            result = 200;
        }
        if (clientStatusEnum.name().equals("PENDING")) {
            result = 210;
        }
        if (clientStatusEnum.name().equals("INACTIVE")) {
            result = 230;
        }
        return result;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = new HashSet<>();
        return t -> seen.add(keyExtractor.apply(t));
    }


    public PatientProfile oldToNewPatient(OldClientDto oldClientDto) {
        PatientProfile patientProfile = new PatientProfile();
        patientProfile.setFirst_name(oldClientDto.getFirstName());
        patientProfile.setLast_name(oldClientDto.getLastName());
        patientProfile.setOld_client_guid(oldClientDto.getGuid());
        patientProfile.setStatus_id(converterStatus(oldClientDto.getStatus()));
        return patientProfile;
    }

    public List<PatientProfile> oldToNewAllPatient(List<OldClientDto> clientDtoList) {
        return clientDtoList.stream().map(this::oldToNewPatient).collect(Collectors.toList());
    }

    public User oldToNewUser(OldClientNoteDto oldClientNoteDto) {
        User user = new User();
        user.setLogin(oldClientNoteDto.getLoggedUser());
        return user;
    }

    public List<User> oldToNewUsers(List<OldClientNoteDto> noteDtoList) {
        return noteDtoList.stream().filter(distinctByKey(OldClientNoteDto::getLoggedUser)).map(this::oldToNewUser).collect(Collectors.toList());
    }

    public Note oldToNewNote(OldClientNoteDto oldClientNoteDto) {
        Note note = new Note();
        note.setNote(oldClientNoteDto.getComments());
        note.setOld_note_guid(oldClientNoteDto.getGuid());
        note.setCreated_date_time(oldClientNoteDto.getCreatedDateTime());
        note.setLast_modified_date_time(oldClientNoteDto.getModifiedDateTime());
        note.setPatient_id(patientRepo.findPatientProfileByOld_client_guid(oldClientNoteDto.getClientGuid()));
        note.setCreated_by_user_id(userRepo.findByLogin(oldClientNoteDto.getLoggedUser()));
        note.setLast_modified_by_user_id(userRepo.findByLogin(oldClientNoteDto.getLoggedUser()));
        return note;
    }

    public List<Note> oldToNewAllNote(List<OldClientNoteDto> oldClientNoteDtoList) {
        return oldClientNoteDtoList.stream().map(this::oldToNewNote).collect(Collectors.toList());
    }


}
