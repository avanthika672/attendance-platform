package com.zepic.attendance_platform.service;
import com.zepic.attendance_platform.dto.request.CreateCollegeRequest;
import com.zepic.attendance_platform.dto.request.UpdateCollegeRequest;
import com.zepic.attendance_platform.dto.response.CollegeSummaryResponse;
import com.zepic.attendance_platform.entity.College;
import com.zepic.attendance_platform.mapper.CollegeMapper;
import com.zepic.attendance_platform.repository.CollegeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.zepic.attendance_platform.exception.CollegeNotFoundException;

import java.time.Instant;
import java.util.List;

@Service
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final CollegeMapper collegeMapper;

    public CollegeService(
            CollegeRepository collegeRepository,
            CollegeMapper collegeMapper) {

        this.collegeRepository = collegeRepository;
        this.collegeMapper = collegeMapper;
    }

    @Transactional
    public CollegeSummaryResponse createCollege(
            CreateCollegeRequest request) {

        College college = College.builder()
                .name(request.getName())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        college = collegeRepository.save(college);

        return collegeMapper.toSummaryResponse(college);
    }

    @Transactional(readOnly = true)
    public List<CollegeSummaryResponse> getAllColleges() {

        return collegeRepository
                .findAll(Sort.by(Sort.Direction.ASC, "id"))
                .stream()
                .map(collegeMapper::toSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CollegeSummaryResponse getCollegeById(Long id)
            throws CollegeNotFoundException {

        College college = collegeRepository
                .findById(id)
                .orElseThrow(() ->
                        new CollegeNotFoundException());

        return collegeMapper.toSummaryResponse(college);
    }

    @Transactional
    public CollegeSummaryResponse updateCollege(
            Long id,
            UpdateCollegeRequest request)
            throws CollegeNotFoundException {

        College college = collegeRepository
                .findById(id)
                .orElseThrow(() ->
                        new CollegeNotFoundException());
        college.setName(request.getName());
        college.setUpdatedAt(Instant.now());

        college = collegeRepository.save(college);

        return collegeMapper.toSummaryResponse(college);
    }

    @Transactional
    public void deleteCollege(Long id)
            throws CollegeNotFoundException {

        College college = collegeRepository
                .findById(id)
                .orElseThrow(() ->
                        new CollegeNotFoundException());

        collegeRepository.delete(college);
    }
}