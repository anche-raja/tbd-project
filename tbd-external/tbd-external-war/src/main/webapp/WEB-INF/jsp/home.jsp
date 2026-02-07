<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>TBD Internal Application</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            border-bottom: 3px solid #007bff;
            padding-bottom: 10px;
        }
        .status {
            margin: 20px 0;
            padding: 15px;
            background: #e7f3ff;
            border-left: 4px solid #007bff;
            border-radius: 4px;
        }
        .db-status {
            margin: 20px 0;
            padding: 15px;
            background: #d4edda;
            border-left: 4px solid #28a745;
            border-radius: 4px;
        }
        .api-links {
            margin: 30px 0;
        }
        .api-links h2 {
            color: #555;
        }
        .api-links ul {
            list-style: none;
            padding: 0;
        }
        .api-links li {
            margin: 10px 0;
        }
        .api-links a {
            color: #007bff;
            text-decoration: none;
            padding: 10px 15px;
            display: inline-block;
            background: #f8f9fa;
            border-radius: 4px;
            transition: background 0.3s;
        }
        .api-links a:hover {
            background: #e9ecef;
        }
        .info {
            margin-top: 30px;
            padding-top: 20px;
            border-top: 1px solid #ddd;
            color: #666;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>TBD Internal Application</h1>
        
        <div class="status">
            <h3>Application Status</h3>
            <p><strong><s:property value="message" /></strong></p>
        </div>
        
        <div class="db-status">
            <h3>Database Status</h3>
            <p><s:property value="dbStatus" /></p>
        </div>
        
        <div class="api-links">
            <h2>Available API Endpoints</h2>
            <ul>
                <li><a href="api/health" target="_blank">Health Check</a> - Application health status</li>
                <li><a href="api/db-test" target="_blank">Database Test</a> - Test database connectivity</li>
                <li><a href="api/validate-ip?ip=192.168.1.1" target="_blank">IP Validation</a> - Validate IP address</li>
                <li><a href="api/sample-data" target="_blank">Sample Data</a> - Execute sample query</li>
            </ul>
        </div>
        
        <div class="info">
            <p><strong>Technology Stack:</strong></p>
            <ul>
                <li>Spring Framework 5.3.30</li>
                <li>Struts 2.5.32</li>
                <li>WebSphere Liberty</li>
                <li>Oracle Database</li>
            </ul>
        </div>
    </div>
</body>
</html>
