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
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
@Service
@Slf4j
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final CollegeRepository collegeRepository;
    private final DepartmentMapper departmentMapper;
    private final Counter departmentCreatedCounter;
    public DepartmentService(
            DepartmentRepository departmentRepository,
            CollegeRepository collegeRepository,
            DepartmentMapper departmentMapper,
            MeterRegistry meterRegistry) {
        this.departmentRepository = departmentRepository;
        this.collegeRepository = collegeRepository;
        this.departmentMapper = departmentMapper;
        this.departmentCreatedCounter = Counter.builder("department.created")
                .description("Number of departments created")
                .register(meterRegistry);
    }
    @Transactional
    public DepartmentSummaryResponse createDepartment(
            Long collegeId,
            CreateDepartmentRequest request) {
        log.info("Creating department '{}' for college {}",request.name(),collegeId);
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
        departmentCreatedCounter.increment();
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
    @Cacheable(value="departments", key = "#collegeId+ ':' +#id")
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
    @CachePut(value="departments", key ="#collegeId+ ':' +#id")
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
    @CacheEvict(value="departments",key="#collegeId+ ':' +#id")
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