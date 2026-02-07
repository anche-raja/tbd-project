package com.yourcompany.tbd.external.service;

import com.yourcompany.tbd.services.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * External application service with database access
 */
@Service
public class ExternalService {
    
    private static final Logger logger = LoggerFactory.getLogger(ExternalService.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private AuditService auditService;
    
    /**
     * Get application status
     */
    public String getStatus() {
        return "TBD External Application is running";
    }
    
    /**
     * Test database connectivity
     */
    public String testDatabaseConnection() {
        try {
            String result = jdbcTemplate.queryForObject(
                "SELECT 'Database connection successful' FROM DUAL", 
                String.class
            );
            auditService.logSuccess("SYSTEM", "Database connection test");
            return result;
        } catch (Exception e) {
            logger.error("Database connection failed", e);
            auditService.logFailure("SYSTEM", "Database connection test", e.getMessage());
            return "Database connection failed: " + e.getMessage();
        }
    }
    
    /**
     * Execute a sample query
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> executeSampleQuery() {
        try {
            // This is a sample query - replace with your actual schema
            List<Map<String, Object>> results = jdbcTemplate.queryForList(
                "SELECT SYSDATE as current_time FROM DUAL"
            );
            auditService.logSuccess("SYSTEM", "Sample query execution");
            return results;
        } catch (Exception e) {
            logger.error("Query execution failed", e);
            auditService.logFailure("SYSTEM", "Sample query execution", e.getMessage());
            throw e;
        }
    }
}
