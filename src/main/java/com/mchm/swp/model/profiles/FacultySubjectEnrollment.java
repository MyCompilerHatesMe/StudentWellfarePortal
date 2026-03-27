package com.mchm.swp.model.profiles;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"faculty_id", "student_id", "subject"})
})
public class FacultySubjectEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private FacultyProfile faculty;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentProfile student;

    @Column(nullable = false)
    private String subject;

}
