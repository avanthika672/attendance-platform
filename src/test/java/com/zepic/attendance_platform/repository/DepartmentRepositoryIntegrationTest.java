package com.zepic.attendance_platform.repository;


import com.zepic.attendance_platform.dto.request.CreateDepartmentRequest;
import com.zepic.attendance_platform.dto.response.DepartmentSummaryResponse;
import com.zepic.attendance_platform.entity.College;
import com.zepic.attendance_platform.exception.DepartmentNotFoundException;
import com.zepic.attendance_platform.mapper.CollegeMapperImpl;
import com.zepic.attendance_platform.mapper.DepartmentMapperImpl;
import com.zepic.attendance_platform.service.CollegeService;
import com.zepic.attendance_platform.service.DepartmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DepartmentService.class, DepartmentMapperImpl.class})
class DepartmentRepositoryIntegrationTest {
    @Container
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");
    @Autowired
    CollegeRepository collegeRepository;
    @Autowired
    DepartmentService departmentService;

    @TestConfiguration
    static class TestCacheConfig {

        @Bean
        CacheManager cacheManager() {
            return new ConcurrentMapCacheManager();
        }
    }
    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

    }

    @Test
    void getDepartmentById_shouldRespectTenant() throws DepartmentNotFoundException{
        College collegeA = collegeRepository.save(
                College.builder()
                        .name("College A")
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );
        College collegeB = collegeRepository.save(
                College.builder()
                        .name("College B")
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );
        DepartmentSummaryResponse created = departmentService.createDepartment(
                collegeA.getId(),
                new CreateDepartmentRequest("CSE")
        );
        assertEquals("CSE", created.name());

        DepartmentSummaryResponse fetched = departmentService.getDepartmentById(
                collegeA.getId(),
                created.id()
        );

        assertEquals("CSE", fetched.name());

        assertThrows(DepartmentNotFoundException.class, () -> departmentService.getDepartmentById(
                collegeB.getId(),
                created.id()
                )
        );
    }

    @Test
    void getAllDepartments_shouldReturnOrderedDepartments(){
        College college = collegeRepository.save(
                College.builder()
                        .name("College C")
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );
        departmentService.createDepartment(college.getId(), new CreateDepartmentRequest("ECE")
        );
        departmentService.createDepartment(college.getId(), new CreateDepartmentRequest("CSE")
        );


        List<DepartmentSummaryResponse> departments = departmentService.getAllDepartments(college.getId());

        assertEquals(2,departments.size());
        assertEquals("ECE", departments.get(0).name());
        assertEquals("CSE",departments.get(1).name());

    }
}
