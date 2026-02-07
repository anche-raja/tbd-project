package com.yourcompany.tbd.external.config;

import org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Web application initializer - replaces web.xml
 */
public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Create Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);
        
        // Add Spring context loader listener
        servletContext.addListener(new ContextLoaderListener(rootContext));
        
        // Register Spring MVC DispatcherServlet
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
            "springDispatcher", 
            new DispatcherServlet(rootContext)
        );
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/api/*");
        
        // Register Struts 2 filter
        FilterRegistration.Dynamic strutsFilter = servletContext.addFilter(
            "struts2",
            new StrutsPrepareAndExecuteFilter()
        );
        strutsFilter.addMappingForUrlPatterns(null, false, "*.action");
        
        // Add context parameters
        servletContext.setInitParameter("contextConfigLocation", "");
    }
}
