package com.zepic.attendance_platform;


import com.zepic.attendance_platform.dto.request.CreateDepartmentRequest;
import com.zepic.attendance_platform.dto.request.UpdateDepartmentRequest;
import com.zepic.attendance_platform.dto.response.DepartmentSummaryResponse;
import com.zepic.attendance_platform.entity.College;
import com.zepic.attendance_platform.entity.Department;
import com.zepic.attendance_platform.exception.DepartmentNotFoundException;
import com.zepic.attendance_platform.mapper.DepartmentMapper;
import com.zepic.attendance_platform.repository.CollegeRepository;
import com.zepic.attendance_platform.repository.DepartmentRepository;
import com.zepic.attendance_platform.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private CollegeRepository collegeRepository;
    @Mock
    private DepartmentMapper departmentMapper;
    @InjectMocks
    private DepartmentService departmentService;

    @Test
    void createDepartment_shouldCreateSuccessfully() {

        Long collegeId = 1L;
        CreateDepartmentRequest request = new CreateDepartmentRequest("Computer Science");

        College college = createCollege(collegeId);
        Department department = createDepartment(10L, college);
        DepartmentSummaryResponse expected = createResponse(10L, "Computer Science");

        when(collegeRepository.findById(collegeId))
                .thenReturn(Optional.of(college));

        when(departmentRepository.save(any(Department.class)))
                .thenReturn(department);

        when(departmentMapper.toSummaryResponse(department))
                .thenReturn(expected);

        DepartmentSummaryResponse actual = departmentService.createDepartment(collegeId, request);

        assertEquals(expected, actual);
    }


    @Test
    void createDepartment_shouldThrowExceptionWhenCollegeDoesNotExist() {

        Long collegeId = 999L;
        CreateDepartmentRequest request = new CreateDepartmentRequest("Computer Science");

        when(collegeRepository.findById(collegeId))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> departmentService.createDepartment(collegeId, request)
        );
    }


    @Test
    void getDepartmentById_shouldReturnDepartmentForCorrectCollege()
            throws DepartmentNotFoundException {

        Long collegeId = 1L;
        Long departmentId = 10L;

        College college = createCollege(collegeId);
        Department department = createDepartment(departmentId, college);
        DepartmentSummaryResponse expected = createResponse(departmentId, "Computer Science");

        when(departmentRepository.findByIdAndCollege_Id(departmentId, collegeId))
                .thenReturn(Optional.of(department));

        when(departmentMapper.toSummaryResponse(department))
                .thenReturn(expected);

        DepartmentSummaryResponse actual =
                departmentService.getDepartmentById(collegeId, departmentId);

        assertEquals(expected, actual);
    }


    @Test
    void getDepartmentById_shouldThrowExceptionForWrongCollege() {

        Long collegeId = 2L;
        Long departmentId = 10L;

        when(departmentRepository.findByIdAndCollege_Id(departmentId, collegeId))
                .thenReturn(Optional.empty());

        assertThrows(
                DepartmentNotFoundException.class,
                () -> departmentService.getDepartmentById(
                        collegeId, departmentId)
        );
    }
    @Test
    void getAllDepartments_shouldReturnDepartmentsForCollege() {
        Long collegeId = 1L;
        College college = createCollege(collegeId);

        Department d1 = createDepartment(10L, college);
        Department d2 = createDepartment(11L, college);

        DepartmentSummaryResponse r1 = createResponse(10L, "Computer Science");
        DepartmentSummaryResponse r2 = createResponse(11L, "IT");

        when(departmentRepository.findAllByCollege_IdOrderByIdAsc(collegeId)).thenReturn(java.util.List.of(d1, d2));
        when(departmentMapper.toSummaryResponse(d1)).thenReturn(r1);
        when(departmentMapper.toSummaryResponse(d2)).thenReturn(r2);

        assertEquals(
                java.util.List.of(r1, r2),
                departmentService.getAllDepartments(collegeId)
        );
    }
    @Test
    void updateDepartment_shouldUpdateSuccessfully()
            throws DepartmentNotFoundException {

        Long collegeId = 1L;
        Long departmentId = 10L;
        College college = createCollege(collegeId);
        Department department = createDepartment(departmentId, college);

        UpdateDepartmentRequest request = new UpdateDepartmentRequest("Information Technology");
        DepartmentSummaryResponse expected = createResponse(departmentId, "Information Technology");

        when(departmentRepository.findByIdAndCollege_Id(departmentId, collegeId))
                .thenReturn(Optional.of(department));
        when(departmentRepository.save(department)).thenReturn(department);
        when(departmentMapper.toSummaryResponse(department)).thenReturn(expected);

        assertEquals(expected, departmentService.updateDepartment(collegeId, departmentId, request));

        assertEquals("Information Technology", department.getName());
    }
    @Test
    void updateDepartment_shouldThrowExceptionWhenNotFound() {

        Long collegeId = 1L;
        Long departmentId = 999L;

        when(departmentRepository.findByIdAndCollege_Id(departmentId, collegeId))
                .thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class,
                () -> departmentService.updateDepartment(collegeId, departmentId,
                        new UpdateDepartmentRequest("IT"))
        );
    }
    @Test
    void deleteDepartment_shouldDeleteSuccessfully()
            throws DepartmentNotFoundException {

        Long collegeId = 1L;
        Long departmentId = 10L;
        Department department =
                createDepartment(departmentId, createCollege(collegeId));

        when(departmentRepository.findByIdAndCollege_Id(departmentId, collegeId))
                .thenReturn(Optional.of(department));

        departmentService.deleteDepartment(collegeId, departmentId);

        verify(departmentRepository).delete(department);
    }
    @Test
    void deleteDepartment_shouldThrowExceptionWhenNotFound() {

        Long collegeId = 1L;
        Long departmentId = 999L;

        when(departmentRepository.findByIdAndCollege_Id(departmentId, collegeId))
                .thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class,
                () -> departmentService.deleteDepartment(collegeId, departmentId)
        );

        verify(departmentRepository, never()).delete(any(Department.class));
    }

    private College createCollege(Long id) {
        College college = new College();
        college.setId(id);
        return college;
    }

    private Department createDepartment(Long id, College college) {
        return Department.builder()
                .id(id)
                .college(college)
                .name("Computer Science")
                .build();
    }

    private DepartmentSummaryResponse createResponse(
            Long id, String name) {
        return new DepartmentSummaryResponse(id, name);
    }
}

