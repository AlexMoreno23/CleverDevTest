package by.morunov.test.new_system.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author Alex Morunov
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "note")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String old_note_guid;

    @Column(updatable = false)
    private LocalDateTime created_date_time;

    private LocalDateTime last_modified_date_time;

    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_user_id", updatable = false)
    private User created_by_user_id;

    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_user_id")
    private User last_modified_by_user_id;

    @Column(length = 4000)
    private String note;

    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_profile_id", updatable = false)
    private PatientProfile patient_id;

    public Note(LocalDateTime created_date_time, LocalDateTime last_modified_date_time,
                User created_by_user_id, User last_modified_by_user_id,
                String note, PatientProfile patient_id) {
        this.created_date_time = created_date_time;
        this.last_modified_date_time = last_modified_date_time;
        this.created_by_user_id = created_by_user_id;
        this.last_modified_by_user_id = last_modified_by_user_id;
        this.note = note;
        this.patient_id = patient_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Note that = (Note) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
