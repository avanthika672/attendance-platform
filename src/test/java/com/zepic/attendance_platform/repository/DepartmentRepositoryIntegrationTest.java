package com.zepic.attendance_platform.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DepartmentRepositoryIntegrationTest {
    @Container
    static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine");
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    JdbcTemplate jdbc;
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
    void findByIdAndCollegeId_shouldRespectTenant(){
        jdbc.update("INSERT INTO college (name) VALUES (?)", "College A");
        jdbc.update("INSERT INTO college (name) VALUES (?)", "College B");
        jdbc.update("""
                 INSERT INTO department (college_id, name) 
                 VALUES (1, 'CSE'), (2, 'ECE') 
                 """);

        assertTrue(departmentRepository.findByIdAndCollege_Id(1L,1L).isPresent());
        assertTrue(departmentRepository.findByIdAndCollege_Id(1L,2L).isEmpty());

    }
    @Test
    void findAllByCollegeId_shouldReturnOrderedDepartments(){
        jdbc.update("INSERT INTO college (name) VALUES (?)", "College C");
        jdbc.update("""
                INSERT INTO department (college_id, name) VALUES (3, 'ECE'), (3, 'CSE')
                """);

        List<?> departments = departmentRepository.findAllByCollege_IdOrderByIdAsc(3L);
        assertEquals(2,departments.size());

    }
}
