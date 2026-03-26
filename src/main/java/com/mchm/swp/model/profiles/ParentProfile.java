package com.mchm.swp.model.profiles;

import com.mchm.swp.model.AuthUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParentProfile {

    @MapsId
    @OneToOne
    AuthUser authUser;
    @ManyToMany
    @JoinTable(
            name = "parents_students",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    Set<StudentProfile> children = new HashSet<>();
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String number;
}
