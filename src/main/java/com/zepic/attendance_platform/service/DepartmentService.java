package com.zepic.attendance_platform.service;
import com.zepic.attendance_platform.dto.request.CreateDepartmentRequest;
import com.zepic.attendance_platform.dto.request.UpdateDepartmentRequest;
import com.zepic.attendance_platform.dto.response.DepartmentSummaryResponse;
import com.zepic.attendance_platform.entity.College;
import com.zepic.attendance_platform.entity.Department;
import com.zepic.attendance_platform.exception.DepartmentNotFoundException;
import com.zepic.attendance_platform.mapper.DepartmentMapper;
import com.zepic.attendance_platform.repository.CollegeRepository;
import com.zepic.attendance_platform.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final CollegeRepository collegeRepository;
    private final DepartmentMapper departmentMapper;
    public DepartmentService(
            DepartmentRepository departmentRepository,
            CollegeRepository collegeRepository,
            DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.collegeRepository = collegeRepository;
        this.departmentMapper = departmentMapper;
    }
    @Transactional
    public DepartmentSummaryResponse createDepartment(
            Long collegeId,
            CreateDepartmentRequest request) {
        College college = collegeRepository.findById(collegeId)
                .orElseThrow(() ->
                        new IllegalArgumentException("College not found"));
        Department department = Department.builder()
                .college(college)
                .name(request.name())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        department = departmentRepository.save(department);
        return departmentMapper.toSummaryResponse(department);
    }

    @Transactional(readOnly = true)
    public List<DepartmentSummaryResponse> getAllDepartments(
            Long collegeId) {
        return departmentRepository
                .findAllByCollege_IdOrderByIdAsc(collegeId)
                .stream()
                .map(departmentMapper::toSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DepartmentSummaryResponse getDepartmentById(
            Long collegeId,
            Long id)
            throws DepartmentNotFoundException {
        Department department = departmentRepository
                .findByIdAndCollege_Id(id, collegeId)
                .orElseThrow(() ->
                        new DepartmentNotFoundException());
        return departmentMapper.toSummaryResponse(department);
    }

    @Transactional
    public DepartmentSummaryResponse updateDepartment(
            Long collegeId,
            Long id,
            UpdateDepartmentRequest request)
            throws DepartmentNotFoundException {
        Department department = departmentRepository
                .findByIdAndCollege_Id(id, collegeId)
                .orElseThrow(() ->
                        new DepartmentNotFoundException());
        department.setName(request.name());
        department.setUpdatedAt(Instant.now());
        department = departmentRepository.save(department);
        return departmentMapper.toSummaryResponse(department);
    }

    @Transactional
    public void deleteDepartment(
            Long collegeId,
            Long id)
            throws DepartmentNotFoundException {
        Department department = departmentRepository
                .findByIdAndCollege_Id(id, collegeId)
                .orElseThrow(() ->
                        new DepartmentNotFoundException());
        departmentRepository.delete(department);
    }
}