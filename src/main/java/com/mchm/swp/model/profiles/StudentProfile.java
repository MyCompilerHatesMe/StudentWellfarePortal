package com.mchm.swp.model.profiles;

import com.mchm.swp.model.AuthUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentProfile {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    private AuthUser authUser;

    @Column(nullable = false, unique = true)
    private String rollNo;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @ElementCollection
    @CollectionTable(name = "student_marks", joinColumns = @JoinColumn(name = "student_id"))
    @MapKeyColumn(name = "subject")
    @Column(name = "score")
    private Map<String, BigDecimal> marks = new HashMap<>();

    public BigDecimal getCgpa() {
        if (marks.isEmpty()) return BigDecimal.ZERO;
        BigDecimal sum = marks.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(marks.size()), 2, RoundingMode.HALF_UP);
    }

    public String getAuthUsername() {
        return authUser.getUsername();
    }
}
