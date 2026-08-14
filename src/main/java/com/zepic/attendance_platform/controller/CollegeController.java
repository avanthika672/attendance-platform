package com.zepic.attendance_platform.controller;
import com.zepic.attendance_platform.dto.request.CreateCollegeRequest;
import com.zepic.attendance_platform.dto.request.UpdateCollegeRequest;
import com.zepic.attendance_platform.dto.response.CollegeSummaryResponse;
import com.zepic.attendance_platform.exception.CollegeNotFoundException;
import com.zepic.attendance_platform.service.CollegeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/colleges")
public class CollegeController {
    private final CollegeService collegeService;
    public CollegeController(CollegeService collegeService) {
        this.collegeService = collegeService;
    }

    @PostMapping
    public ResponseEntity<CollegeSummaryResponse> createCollege(
            @RequestBody CreateCollegeRequest request)
    {
        CollegeSummaryResponse response =
                collegeService.createCollege(request);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<CollegeSummaryResponse>> getAllColleges() {
        List<CollegeSummaryResponse> response =
                collegeService.getAllColleges();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CollegeSummaryResponse> getCollegeById(
            @PathVariable Long id)
            throws CollegeNotFoundException {
        CollegeSummaryResponse response =
                collegeService.getCollegeById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CollegeSummaryResponse> updateCollege(
            @PathVariable Long id,
            @RequestBody UpdateCollegeRequest request)
            throws CollegeNotFoundException {
        CollegeSummaryResponse response =
                collegeService.updateCollege(id, request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollege(
            @PathVariable Long id)
            throws CollegeNotFoundException {
        collegeService.deleteCollege(id);
        return ResponseEntity.noContent().build();
    }
}