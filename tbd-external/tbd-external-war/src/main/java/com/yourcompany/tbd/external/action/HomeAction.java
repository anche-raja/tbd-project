package com.yourcompany.tbd.external.action;

import com.opensymphony.xwork2.ActionSupport;
import com.yourcompany.tbd.external.service.ExternalService;
import com.yourcompany.tbd.networkvalidation.NetworkValidator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Struts 2 action for web pages
 */
public class HomeAction extends ActionSupport {
    
    private static final long serialVersionUID = 1L;
    
    @Autowired
    private ExternalService externalService;
    
    @Autowired
    private NetworkValidator networkValidator;
    
    private String message;
    private String dbStatus;
    
    /**
     * Execute action
     */
    @Override
    public String execute() {
        message = "Welcome to TBD External Application";
        dbStatus = externalService.testDatabaseConnection();
        return SUCCESS;
    }
    
    // Getters and setters for Struts
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getDbStatus() {
        return dbStatus;
    }
    
    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }
}
