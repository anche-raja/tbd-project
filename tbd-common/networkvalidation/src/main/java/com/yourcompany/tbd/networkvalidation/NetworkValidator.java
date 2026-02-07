package com.yourcompany.tbd.networkvalidation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Network validation utilities for IP addresses, ports, etc.
 */
@Component
public class NetworkValidator {
    
    private static final Logger logger = LoggerFactory.getLogger(NetworkValidator.class);
    
    private static final Pattern IPV4_PATTERN = Pattern.compile(
        "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );
    
    /**
     * Validates if the given string is a valid IPv4 address
     */
    public boolean isValidIPv4(String ip) {
        if (StringUtils.isBlank(ip)) {
            logger.debug("IP address is blank");
            return false;
        }
        
        boolean valid = IPV4_PATTERN.matcher(ip.trim()).matches();
        logger.debug("IP validation for '{}': {}", ip, valid);
        return valid;
    }
    
    /**
     * Validates if the given port number is valid (1-65535)
     */
    public boolean isValidPort(int port) {
        boolean valid = port > 0 && port <= 65535;
        logger.debug("Port validation for '{}': {}", port, valid);
        return valid;
    }
    
    /**
     * Validates if the given string is a valid port number
     */
    public boolean isValidPort(String port) {
        if (StringUtils.isBlank(port)) {
            return false;
        }
        
        try {
            int portNum = Integer.parseInt(port.trim());
            return isValidPort(portNum);
        } catch (NumberFormatException e) {
            logger.debug("Invalid port format: {}", port);
            return false;
        }
    }
}
