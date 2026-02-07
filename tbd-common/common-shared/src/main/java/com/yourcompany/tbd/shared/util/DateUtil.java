package com.yourcompany.tbd.shared.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Date and time utilities
 */
public class DateUtil {
    
    public static final DateTimeFormatter DEFAULT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DEFAULT_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private DateUtil() {
        // Utility class
    }
    
    /**
     * Format LocalDate to string using default format
     */
    public static String format(LocalDate date) {
        return date != null ? date.format(DEFAULT_DATE_FORMAT) : null;
    }
    
    /**
     * Format LocalDateTime to string using default format
     */
    public static String format(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DEFAULT_DATETIME_FORMAT) : null;
    }
    
    /**
     * Parse string to LocalDate using default format
     */
    public static LocalDate parseDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        
        try {
            return LocalDate.parse(dateStr.trim(), DEFAULT_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr, e);
        }
    }
    
    /**
     * Parse string to LocalDateTime using default format
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (StringUtils.isBlank(dateTimeStr)) {
            return null;
        }
        
        try {
            return LocalDateTime.parse(dateTimeStr.trim(), DEFAULT_DATETIME_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid datetime format: " + dateTimeStr, e);
        }
    }
}
