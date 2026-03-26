package com.mchm.swp.model.profiles;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
