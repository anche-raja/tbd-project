package com.yourcompany.tbd.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Audit logging service
 */
@Service
public class AuditService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);
    
    /**
     * Log an audit event
     */
    public void logEvent(String username, String action, String resource) {
        LocalDateTime timestamp = LocalDateTime.now();
        
        // In a real application, this would write to a database
        logger.info("AUDIT: [{}] User: {}, Action: {}, Resource: {}", 
            timestamp, username, action, resource);
    }
    
    /**
     * Log a successful operation
     */
    public void logSuccess(String username, String operation) {
        logEvent(username, "SUCCESS:" + operation, "");
    }
    
    /**
     * Log a failed operation
     */
    public void logFailure(String username, String operation, String reason) {
        logger.warn("AUDIT: User: {}, Failed Operation: {}, Reason: {}", 
            username, operation, reason);
    }
}
