-- Sample initialization script for Oracle database
-- This runs when the container first starts

-- Create sample table
CREATE TABLE TBD_STATUS (
    ID NUMBER PRIMARY KEY,
    STATUS_NAME VARCHAR2(100),
    STATUS_VALUE VARCHAR2(500),
    CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create sequence
CREATE SEQUENCE TBD_STATUS_SEQ START WITH 1 INCREMENT BY 1;

-- Insert sample data
INSERT INTO TBD_STATUS (ID, STATUS_NAME, STATUS_VALUE) 
VALUES (TBD_STATUS_SEQ.NEXTVAL, 'APPLICATION', 'Initialized');

INSERT INTO TBD_STATUS (ID, STATUS_NAME, STATUS_VALUE) 
VALUES (TBD_STATUS_SEQ.NEXTVAL, 'DATABASE', 'Ready');

COMMIT;

-- Grant permissions (already done by container, but included for reference)
-- GRANT SELECT, INSERT, UPDATE, DELETE ON TBD_STATUS TO tbd_user;
