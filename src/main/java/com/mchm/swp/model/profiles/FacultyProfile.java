package com.mchm.swp.model.profiles;


import com.mchm.swp.model.AuthUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FacultyProfile {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    private AuthUser authUser;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "faculty")
    private List<FacultySubjectEnrollment> enrollments;

    public String getAuthUsername() {
        return authUser.getUsername();
    }
}
