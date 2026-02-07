package com.yourcompany.tbd.internal.controller;

import com.yourcompany.tbd.internal.service.InternalService;
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
    private InternalService internalService;
    
    @Autowired
    private NetworkValidator networkValidator;
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ApiResponse<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("application", "tbd-internal");
        status.put("message", internalService.getStatus());
        
        return ApiResponse.success(status);
    }
    
    /**
     * Database connectivity test
     */
    @GetMapping("/db-test")
    public ApiResponse<String> testDatabase() {
        String result = internalService.testDatabaseConnection();
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
            var results = internalService.executeSampleQuery();
            return ApiResponse.success("Query executed successfully", results);
        } catch (Exception e) {
            return ApiResponse.error("Query failed: " + e.getMessage(), "DB_ERROR");
        }
    }
}
