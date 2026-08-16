package com.zepic.attendance_platform.controller;
import com.zepic.attendance_platform.dto.request.CreateDepartmentRequest;
import com.zepic.attendance_platform.dto.request.UpdateDepartmentRequest;
import com.zepic.attendance_platform.dto.response.DepartmentSummaryResponse;
import com.zepic.attendance_platform.exception.DepartmentNotFoundException;
import com.zepic.attendance_platform.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/colleges/{collegeId}/departments")
public class DepartmentController {
    private final DepartmentService departmentService;
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<DepartmentSummaryResponse> createDepartment(
            @PathVariable Long collegeId,
            @RequestBody CreateDepartmentRequest request) {
        DepartmentSummaryResponse response =
                departmentService.createDepartment(collegeId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentSummaryResponse>> getAllDepartments(
            @PathVariable Long collegeId) {
        List<DepartmentSummaryResponse> response =
                departmentService.getAllDepartments(collegeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentSummaryResponse> getDepartmentById(
            @PathVariable Long collegeId,
            @PathVariable Long id)
            throws DepartmentNotFoundException {
        DepartmentSummaryResponse response =
                departmentService.getDepartmentById(collegeId, id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentSummaryResponse> updateDepartment(
            @PathVariable Long collegeId,
            @PathVariable Long id,
            @RequestBody UpdateDepartmentRequest request)
            throws DepartmentNotFoundException {
        DepartmentSummaryResponse response =
                departmentService.updateDepartment(collegeId, id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable Long collegeId,
            @PathVariable Long id)
            throws DepartmentNotFoundException {
        departmentService.deleteDepartment(collegeId, id);
        return ResponseEntity.noContent().build();
    }
}