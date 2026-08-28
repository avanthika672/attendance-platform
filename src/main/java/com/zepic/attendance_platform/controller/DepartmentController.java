package com.zepic.attendance_platform.controller;
import com.zepic.attendance_platform.dto.request.CreateDepartmentRequest;
import com.zepic.attendance_platform.dto.request.UpdateDepartmentRequest;
import com.zepic.attendance_platform.dto.response.DepartmentSummaryResponse;
import com.zepic.attendance_platform.exception.DepartmentNotFoundException;
import com.zepic.attendance_platform.security.TenantContext;
import com.zepic.attendance_platform.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final DepartmentService departmentService;
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }
    @PostMapping
    public ResponseEntity<DepartmentSummaryResponse> createDepartment(
            @RequestBody CreateDepartmentRequest request) {
        DepartmentSummaryResponse response =
                departmentService.createDepartment(TenantContext.get(), request
                );
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<DepartmentSummaryResponse>> getAllDepartments() {

        List<DepartmentSummaryResponse> response =
                departmentService.getAllDepartments(TenantContext.get());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentSummaryResponse> getDepartmentById(
            @PathVariable Long id)
            throws DepartmentNotFoundException {
        DepartmentSummaryResponse response =
                departmentService.getDepartmentById(TenantContext.get(), id);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentSummaryResponse> updateDepartment(
            @PathVariable Long id,
            @RequestBody UpdateDepartmentRequest request)
            throws DepartmentNotFoundException {
        DepartmentSummaryResponse response =
                departmentService.updateDepartment(TenantContext.get(), id, request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable Long id)
            throws DepartmentNotFoundException {
        departmentService.deleteDepartment(TenantContext.get(), id);
        return ResponseEntity.noContent().build();
    }
}