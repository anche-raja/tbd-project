package com.yourcompany.tbd.external.controller;

import com.yourcompany.tbd.external.service.ExternalService;
import com.yourcompany.tbd.networkvalidation.NetworkValidator;
import com.yourcompany.tbd.shared.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST API controller using Spring MVC
 */
@RestController
@RequestMapping("/api")
public class ApiController {
    
    @Autowired
    private ExternalService externalService;
    
    @Autowired
    private NetworkValidator networkValidator;
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ApiResponse<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("application", "tbd-external");
        status.put("message", externalService.getStatus());
        
        return ApiResponse.success(status);
    }
    
    /**
     * Database connectivity test
     */
    @GetMapping("/db-test")
    public ApiResponse<String> testDatabase() {
        String result = externalService.testDatabaseConnection();
        return ApiResponse.success("Database test completed", result);
    }
    
    /**
     * Validate IP address
     */
    @GetMapping("/validate-ip")
    public ApiResponse<Map<String, Object>> validateIp(@RequestParam String ip) {
        boolean isValid = networkValidator.isValidIPv4(ip);
        
        Map<String, Object> result = new HashMap<>();
        result.put("ip", ip);
        result.put("valid", isValid);
        
        if (isValid) {
            return ApiResponse.success("IP address is valid", result);
        } else {
            return ApiResponse.error("IP address is invalid");
        }
    }
    
    /**
     * Sample data endpoint
     */
    @GetMapping("/sample-data")
    public ApiResponse<?> getSampleData() {
        try {
            var results = externalService.executeSampleQuery();
            return ApiResponse.success("Query executed successfully", results);
        } catch (Exception e) {
            return ApiResponse.error("Query failed: " + e.getMessage(), "DB_ERROR");
        }
    }
}
