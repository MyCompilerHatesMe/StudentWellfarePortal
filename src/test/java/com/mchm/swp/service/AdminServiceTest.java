package com.mchm.swp.service;

import com.mchm.swp.model.dto.response.EnrollmentConnectionResponse;
import com.mchm.swp.model.profiles.FacultyProfile;
import com.mchm.swp.model.profiles.FacultySubjectEnrollment;
import com.mchm.swp.model.profiles.StudentProfile;
import com.mchm.swp.repo.FacultySubjectEnrollmentRepo;
import com.mchm.swp.repo.ParentProfileRepo;
import com.mchm.swp.utils.DtoMapper;
import com.mchm.swp.utils.ProfileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private FacultySubjectEnrollmentRepo enrollmentRepo;
    @Mock
    private ProfileUtils utils;
    @Mock
    private DtoMapper mapper;
    @Mock
    private ParentProfileRepo parentRepo;

    @InjectMocks
    private AdminService adminService;

    @Test
    void enroll_ShouldThrowException_WhenEnrollmentAlreadyExists() {
        when(enrollmentRepo.existsByFaculty_AuthUser_UsernameAndStudent_AuthUser_UsernameAndSubject(
                "faculty1", "student1", "Math")).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                adminService.enroll("student1", "faculty1", "Math"));

        assertEquals("Student-Faculty-Subject Enrollment already exists", exception.getMessage());
        verify(enrollmentRepo, never()).save(any());
    }

    @Test
    void enroll_ShouldSaveSuccessfully_WhenValidDataProvided() {
        when(enrollmentRepo.existsByFaculty_AuthUser_UsernameAndStudent_AuthUser_UsernameAndSubject(
                anyString(), anyString(), anyString())).thenReturn(false);

        StudentProfile mockStudent = new StudentProfile();
        FacultyProfile mockFaculty = new FacultyProfile();

        when(utils.getStudentProfile("student1")).thenReturn(mockStudent);
        when(utils.getVerifiedFacultyProfile("faculty1")).thenReturn(mockFaculty);

        FacultySubjectEnrollment savedMock = new FacultySubjectEnrollment();
        when(enrollmentRepo.save(any(FacultySubjectEnrollment.class))).thenReturn(savedMock);
        when(mapper.toResponse(savedMock)).thenReturn(new EnrollmentConnectionResponse("student1", "faculty1", "Math"));

        EnrollmentConnectionResponse response = adminService.enroll("student1", "faculty1", "Math");

        assertNotNull(response);
        assertEquals("Math", response.subject());
        verify(enrollmentRepo, times(1)).save(any(FacultySubjectEnrollment.class));
    }
}
